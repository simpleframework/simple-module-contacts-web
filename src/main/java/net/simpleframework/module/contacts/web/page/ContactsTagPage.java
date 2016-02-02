package net.simpleframework.module.contacts.web.page;

import static net.simpleframework.common.I18n.$m;

import java.util.Map;

import net.simpleframework.ado.query.IDataQuery;
import net.simpleframework.common.coll.KVMap;
import net.simpleframework.module.contacts.ContactsTag;
import net.simpleframework.module.contacts.IContactsContextAware;
import net.simpleframework.mvc.JavascriptForward;
import net.simpleframework.mvc.PageParameter;
import net.simpleframework.mvc.common.element.ButtonElement;
import net.simpleframework.mvc.common.element.ElementList;
import net.simpleframework.mvc.common.element.LinkButton;
import net.simpleframework.mvc.common.element.LinkElement;
import net.simpleframework.mvc.component.ComponentParameter;
import net.simpleframework.mvc.component.base.ajaxrequest.AjaxRequestBean;
import net.simpleframework.mvc.component.base.validation.EValidatorMethod;
import net.simpleframework.mvc.component.base.validation.Validator;
import net.simpleframework.mvc.component.ui.pager.EPagerBarLayout;
import net.simpleframework.mvc.component.ui.pager.TablePagerBean;
import net.simpleframework.mvc.component.ui.pager.TablePagerColumn;
import net.simpleframework.mvc.component.ui.pager.db.AbstractDbTablePagerHandler;
import net.simpleframework.mvc.component.ui.propeditor.EInputCompType;
import net.simpleframework.mvc.component.ui.propeditor.InputComp;
import net.simpleframework.mvc.component.ui.propeditor.PropEditorBean;
import net.simpleframework.mvc.component.ui.propeditor.PropField;
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
				AddTagPage.class);
		addWindowBean(pp, "ContactsTagPage_add", ajaxRequest).setTitle($m("ContactsTagPage.0"))
				.setHeight(300).setWidth(360);
	}

	protected TablePagerBean addTablePagerBean(final PageParameter pp) {
		final TablePagerBean tablePager = (TablePagerBean) super
				.addTablePagerBean(pp, "ContactsTagPage_tbl", ContactsTagTbl.class).setShowHead(false)
				.setPagerBarLayout(EPagerBarLayout.none).setContainerId("idContactsTagPage_tbl");
		tablePager.addColumn(new TablePagerColumn("text")).addColumn(TablePagerColumn.OPE(55));
		return tablePager;
	}

	@Override
	public ElementList getLeftElements(final PageParameter pp) {
		return ElementList.of(LinkButton.addBtn().setOnclick("$Actions['ContactsTagPage_add']();"));
	}

	@Override
	protected String toContentHTML(final PageParameter pp) {
		final StringBuilder sb = new StringBuilder();
		sb.append("<div id='idContactsTagPage_tbl'>");
		sb.append("</div>");
		return sb.toString();
	}

	public static class ContactsTagTbl extends AbstractDbTablePagerHandler {
		@Override
		public IDataQuery<?> createDataObjectQuery(final ComponentParameter cp) {
			return _contactsTagService.queryTags(ContactsUtils.getDomainId(cp));
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
			if (cp.isLmanager()) {
			}
			return sb.toString();
		}

		protected String toOpeHTML(final ComponentParameter cp, final ContactsTag tag) {
			final StringBuilder sb = new StringBuilder();
			sb.append(ButtonElement.deleteBtn());
			return sb.toString();
		}
	}

	public static class AddTagPage extends FormPropEditorTemplatePage {

		@Override
		protected void onForward(final PageParameter pp) throws Exception {
			super.onForward(pp);

			addFormValidationBean(pp).addValidators(
					new Validator(EValidatorMethod.required, "#tag_name"));
		}

		@Override
		public JavascriptForward onSave(final ComponentParameter cp) throws Exception {
			ContactsTag tag = _contactsTagService.getBean(cp.getParameter("tag_id"));
			if (tag == null) {
				tag = _contactsTagService.createBean();
				tag.setOrgId(ContactsUtils.getDomainId(cp));
			}
			tag.setText(cp.getParameter("tag_name"));
			tag.setDescription(cp.getParameter("tag_description"));
			if (cp.isLmanager()) {
			}
			_contactsTagService.insert(tag);
			return super.onSave(cp);
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

			}

			final PropField f1 = new PropField($m("AddTagPage.0")).addComponents(tagId, tag_name);
			if (pp.isLmanager()) {
			}
			final PropField f2 = new PropField($m("Description")).addComponents(tag_description);
			propEditor.getFormFields().append(f1, f2);
		}
	}
}