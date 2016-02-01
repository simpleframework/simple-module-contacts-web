package net.simpleframework.module.contacts.web.page;

import static net.simpleframework.common.I18n.$m;

import java.util.Map;

import net.simpleframework.ado.query.IDataQuery;
import net.simpleframework.module.contacts.ContactsTag;
import net.simpleframework.module.contacts.IContactsContextAware;
import net.simpleframework.mvc.JavascriptForward;
import net.simpleframework.mvc.PageParameter;
import net.simpleframework.mvc.common.element.ElementList;
import net.simpleframework.mvc.common.element.LinkButton;
import net.simpleframework.mvc.component.ComponentParameter;
import net.simpleframework.mvc.component.base.ajaxrequest.AjaxRequestBean;
import net.simpleframework.mvc.component.base.validation.EValidatorMethod;
import net.simpleframework.mvc.component.base.validation.Validator;
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
				.setContainerId("idContactsTagPage_tbl");
		tablePager.addColumn(new TablePagerColumn("cc"));
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
		protected IDataQuery<?> getDataObjectQuery(final ComponentParameter cp) {
			return super.getDataObjectQuery(cp);
		}

		@Override
		protected Map<String, Object> getRowData(final ComponentParameter cp, final Object dataObject) {
			return super.getRowData(cp, dataObject);
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
			final ContactsTag tag = _contactsTagService.getBean(pp.getParameter("tag_id"));
			final InputComp tagId = InputComp.hidden("tag_id");
			if (tag != null) {
				tagId.setDefaultValue(tag.getId());
			}

			final PropField f1 = new PropField($m("AddTagPage.0")).addComponents(tagId, new InputComp(
					"tag_name"));
			if (pp.isLmanager()) {
			}
			final PropField f2 = new PropField($m("Description")).addComponents(new InputComp(
					"tag_description").setType(EInputCompType.textarea).setAttributes("rows:5"));
			propEditor.getFormFields().append(f1, f2);
		}
	}
}