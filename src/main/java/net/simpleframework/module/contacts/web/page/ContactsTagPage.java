package net.simpleframework.module.contacts.web.page;

import static net.simpleframework.common.I18n.$m;

import java.util.Map;

import net.simpleframework.ado.query.IDataQuery;
import net.simpleframework.common.StringUtils;
import net.simpleframework.common.coll.KVMap;
import net.simpleframework.ctx.permission.PermissionDept;
import net.simpleframework.ctx.trans.Transaction;
import net.simpleframework.module.contacts.ContactsTag;
import net.simpleframework.module.contacts.IContactsContext;
import net.simpleframework.module.contacts.IContactsContextAware;
import net.simpleframework.mvc.AbstractMVCPage;
import net.simpleframework.mvc.IForward;
import net.simpleframework.mvc.JavascriptForward;
import net.simpleframework.mvc.PageParameter;
import net.simpleframework.mvc.common.element.ButtonElement;
import net.simpleframework.mvc.common.element.EElementEvent;
import net.simpleframework.mvc.common.element.ElementList;
import net.simpleframework.mvc.common.element.LinkButton;
import net.simpleframework.mvc.common.element.LinkElement;
import net.simpleframework.mvc.common.element.SpanElement;
import net.simpleframework.mvc.component.ComponentParameter;
import net.simpleframework.mvc.component.base.ajaxrequest.AjaxRequestBean;
import net.simpleframework.mvc.component.base.validation.EValidatorMethod;
import net.simpleframework.mvc.component.base.validation.Validator;
import net.simpleframework.mvc.component.ext.deptselect.DeptSelectBean;
import net.simpleframework.mvc.component.ui.menu.MenuBean;
import net.simpleframework.mvc.component.ui.menu.MenuItem;
import net.simpleframework.mvc.component.ui.menu.MenuItems;
import net.simpleframework.mvc.component.ui.pager.AbstractTablePagerSchema;
import net.simpleframework.mvc.component.ui.pager.EPagerBarLayout;
import net.simpleframework.mvc.component.ui.pager.TablePagerBean;
import net.simpleframework.mvc.component.ui.pager.TablePagerColumn;
import net.simpleframework.mvc.component.ui.pager.TablePagerUtils;
import net.simpleframework.mvc.component.ui.pager.db.AbstractDbTablePagerHandler;
import net.simpleframework.mvc.component.ui.propeditor.EInputCompType;
import net.simpleframework.mvc.component.ui.propeditor.InputComp;
import net.simpleframework.mvc.component.ui.propeditor.PropEditorBean;
import net.simpleframework.mvc.component.ui.propeditor.PropField;
import net.simpleframework.mvc.component.ui.propeditor.PropFields;
import net.simpleframework.mvc.template.AbstractTBTemplatePage;
import net.simpleframework.mvc.template.lets.FormPropEditorTemplatePage;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public class ContactsTagPage extends AbstractTBTemplatePage implements IContactsContextAware {

	@Override
	protected void onForward(final PageParameter pp) throws Exception {
		super.onForward(pp);

		addTablePagerBean(pp);

		// 添加
		final AjaxRequestBean ajaxRequest = addAjaxRequest(pp, "ContactsTagPage_addPage",
				getTagEditClass(pp));
		addWindowBean(pp, "ContactsTagPage_add", ajaxRequest).setTitle($m("ContactsTagPage.0"))
				.setHeight(300).setWidth(360);

		// 删除
		addDeleteAjaxRequest(pp, "ContactsTagPage_del");

		// 移动
		addAjaxRequest(pp, "ContactsTagPage_Move").setHandlerMethod("doMove");
	}

	protected Class<? extends AbstractMVCPage> getTagEditClass(final PageParameter pp) {
		return AddTagPage.class;
	}

	protected TablePagerBean addTablePagerBean(final PageParameter pp) {
		final TablePagerBean tablePager = (TablePagerBean) super
				.addTablePagerBean(pp, "ContactsTagPage_tbl", ContactsTagTbl.class).setShowHead(false)
				.setPagerBarLayout(EPagerBarLayout.none).setContainerId("idContactsTagPage_tbl");
		tablePager.addColumn(new TablePagerColumn("text")).addColumn(TablePagerColumn.OPE(70));
		return tablePager;
	}

	@Override
	public ElementList getLeftElements(final PageParameter pp) {
		return ElementList.of(LinkButton.addBtn().setOnclick("$Actions['ContactsTagPage_add']();"));
	}

	@Transaction(context = IContactsContext.class)
	public IForward doDelete(final ComponentParameter cp) {
		final Object[] ids = StringUtils.split(cp.getParameter("id"));
		_contactsTagService.delete(ids);
		return new JavascriptForward("$Actions['ContactsTagPage_tbl']();");
	}

	@Transaction(context = IContactsContext.class)
	public IForward doMove(final ComponentParameter cp) {
		_contactsTagService.exchange(TablePagerUtils.getExchangeBeans(cp, _contactsTagService));
		return new JavascriptForward("$Actions['ContactsTagPage_tbl']();");
	}

	@Override
	protected String toContentHTML(final PageParameter pp) {
		final StringBuilder sb = new StringBuilder();
		sb.append("<div id='idContactsTagPage_tbl'></div>");
		return sb.toString();
	}

	public static class ContactsTagTbl extends AbstractDbTablePagerHandler {
		@Override
		public IDataQuery<?> createDataObjectQuery(final ComponentParameter cp) {
			return _contactsTagService.queryOrgTags(ContactsUtils.getDomainId(cp));
		}

		@Override
		protected Map<String, Object> getRowData(final ComponentParameter cp, final Object dataObject) {
			final ContactsTag tag = (ContactsTag) dataObject;
			final KVMap kv = new KVMap();
			kv.add("text", toTitleHTML(cp, tag)).add(TablePagerColumn.OPE, toOpeHTML(cp, tag));
			return kv;
		}

		protected String toTitleHTML(final ComponentParameter cp, final ContactsTag tag) {
			final StringBuilder sb = new StringBuilder();
			sb.append(new LinkElement(tag.getText())
					.setOnclick("$Actions['ContactsTagPage_add']('tagId=" + tag.getId() + "');"));
			final int contacts = tag.getContacts();
			if (contacts == 0) {
				sb.append(" (").append(contacts).append(")");
			}
			PermissionDept org;
			if (cp.isLmanager() && (org = cp.getDept(tag.getOrgId())).exists()) {
				sb.append("<br>").append(SpanElement.color777(org));
			}
			return sb.toString();
		}

		protected String toOpeHTML(final ComponentParameter cp, final ContactsTag tag) {
			final StringBuilder sb = new StringBuilder();
			sb.append(ButtonElement.deleteBtn().setOnclick(
					"$Actions['ContactsTagPage_del']('id=" + tag.getId() + "');"));
			sb.append(AbstractTablePagerSchema.IMG_DOWNMENU);
			return sb.toString();
		}

		@Override
		public MenuItems getContextMenu(final ComponentParameter cp, final MenuBean menuBean,
				final MenuItem menuItem) {
			if (menuItem == null) {
				final MenuItems items = MenuItems.of();
				items.append(MenuItem.TBL_MOVE_UP("ContactsTagPage_Move"));
				items.append(MenuItem.TBL_MOVE_UP2("ContactsTagPage_Move"));
				items.append(MenuItem.TBL_MOVE_DOWN("ContactsTagPage_Move"));
				items.append(MenuItem.TBL_MOVE_DOWN2("ContactsTagPage_Move"));
				return items;
			}
			return null;
		}
	}

	public static class AddTagPage extends FormPropEditorTemplatePage {

		@Override
		protected void onForward(final PageParameter pp) throws Exception {
			super.onForward(pp);

			addFormValidationBean(pp).addValidators(
					new Validator(EValidatorMethod.required, "#tag_name"));
		}

		@Transaction(context = IContactsContext.class)
		@Override
		public JavascriptForward onSave(final ComponentParameter cp) throws Exception {
			ContactsTag tag = _contactsTagService.getBean(cp.getParameter("tag_id"));
			final boolean insert = tag == null;
			if (insert) {
				tag = _contactsTagService.createBean();
				tag.setOrgId(ContactsUtils.getDomainId(cp));
			}
			tag.setText(cp.getParameter("tag_name"));
			tag.setDescription(cp.getParameter("tag_description"));
			if (cp.isLmanager()) {
				final PermissionDept org = cp.getDept(cp.getParameter("org_id"));
				tag.setOrgId(org.getId());
			}
			if (insert) {
				_contactsTagService.insert(tag);
			} else {
				_contactsTagService.update(tag);
			}
			return new JavascriptForward(
					"$Actions['ContactsTagPage_add'].close(); $Actions['ContactsTagPage_tbl']();");
		}

		@Override
		protected void initPropEditor(final PageParameter pp, final PropEditorBean propEditor) {
			final ContactsTag tag = _contactsTagService.getBean(pp.getParameter("tagId"));
			final InputComp tagId = InputComp.hidden("tag_id");
			final InputComp tag_name = new InputComp("tag_name");
			final InputComp tag_description = new InputComp("tag_description").setType(
					EInputCompType.textarea).setAttributes("rows:5");
			if (tag != null) {
				tagId.setDefaultValue(tag.getId());
				tag_name.setDefaultValue(tag.getText());
				tag_description.setDefaultValue(tag.getDescription());
			}

			final PropFields fields = propEditor.getFormFields();
			fields.add(new PropField($m("AddTagPage.0")).addComponents(tagId, tag_name));
			if (isShowOrgEdit(pp)) {
				pp.addComponentBean("ContactsTagPage_deptSelect", DeptSelectBean.class).setOrg(true)
						.setBindingId("org_id").setBindingText("org_text");
				final InputComp org_id = InputComp.hidden("org_id");
				final InputComp org_text = InputComp.textButton("org_text").setAttributes("readonly")
						.addEvent(EElementEvent.click, "$Actions['ContactsTagPage_deptSelect']();");
				PermissionDept org = null;
				if (tag != null) {
					org = pp.getDept(tag.getOrgId());
				} else {
					org = AbstractMVCPage.getPermissionOrg(pp);
				}
				if (org != null) {
					org_id.setDefaultValue(org.getId());
					org_text.setDefaultValue(org.getText());
				}
				fields.add(new PropField($m("AddTagPage.1")).addComponents(org_id, org_text));
			}
			fields.add(new PropField($m("Description")).addComponents(tag_description));
		}

		protected boolean isShowOrgEdit(final PageParameter pp) {
			return pp.isLmanager();
		}
	}
}