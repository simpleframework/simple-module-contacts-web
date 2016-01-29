package net.simpleframework.module.contacts.web.page;

import static net.simpleframework.common.I18n.$m;
import net.simpleframework.common.Convert;
import net.simpleframework.ctx.permission.PermissionUser;
import net.simpleframework.module.contacts.IContactsContextAware;
import net.simpleframework.mvc.IForward;
import net.simpleframework.mvc.JavascriptForward;
import net.simpleframework.mvc.JsonForward;
import net.simpleframework.mvc.PageParameter;
import net.simpleframework.mvc.common.element.CalendarInput;
import net.simpleframework.mvc.common.element.DictMultiSelectInput;
import net.simpleframework.mvc.common.element.ElementList;
import net.simpleframework.mvc.common.element.InputElement;
import net.simpleframework.mvc.common.element.LinkButton;
import net.simpleframework.mvc.common.element.RowField;
import net.simpleframework.mvc.common.element.TableRow;
import net.simpleframework.mvc.common.element.TableRows;
import net.simpleframework.mvc.common.element.TextButton;
import net.simpleframework.mvc.component.ComponentParameter;
import net.simpleframework.mvc.component.base.validation.EValidatorMethod;
import net.simpleframework.mvc.component.base.validation.Validator;
import net.simpleframework.mvc.component.ext.deptselect.DeptSelectBean;
import net.simpleframework.mvc.component.ext.userselect.UserSelectBean;
import net.simpleframework.mvc.template.lets.FormTableRowTemplatePage;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public class ContactsEditPage extends FormTableRowTemplatePage implements IContactsContextAware {
	@Override
	protected void onForward(final PageParameter pp) throws Exception {
		super.onForward(pp);

		// 日历
		addCalendarBean(pp, "cal_Birthday");
		// 部门选择
		addComponentBean(pp, "ContactsEditPage_deptSelect", DeptSelectBean.class).setClearAction(
				"false").setBindingText("ce_dept");

		// 帐号选择
		addComponentBean(pp, "ContactsEditPage_userselect", UserSelectBean.class).setShowGroupOpt(
				false).setJsSelectCallback("return ContactsEditPage.userSelected(selects)");
		// 帐号选择Action
		addAjaxRequest(pp, "ContactsEditPage_us_callback").setHandlerMethod("doUserSelect");

		// 表单验证
		addFormValidationBean(pp).addValidators(new Validator(EValidatorMethod.required, "#ce_text"));
	}

	public IForward doUserSelect(final ComponentParameter cp) {
		final PermissionUser user = cp.getUser(cp.getParameter("userId"));
		return new JsonForward().put("text", user.getText())
				.put("birthday", Convert.toDateString(user.getBirthday(), "yyyy-MM-dd"))
				.put("dept", user.getDept().toString());
	}

	@Override
	public JavascriptForward onSave(final ComponentParameter cp) throws Exception {
		// _accountService.getBean(pp.getParameter("contactsId"))
		return super.onSave(cp);
	}

	@Override
	public ElementList getLeftElements(final PageParameter pp) {
		return ElementList.of(LinkButton.corner($m("ContactsEditPage.17")).setOnclick(
				"$Actions['ContactsEditPage_userselect']();"));
	}

	@Override
	protected TableRows getTableRows(final PageParameter pp) {
		final InputElement ce_id = InputElement.hidden("ce_id");
		final InputElement ce_text = new InputElement("ce_text");
		final InputElement ce_sex = new InputElement("ce_sex");

		final CalendarInput ce_birthday = new CalendarInput("ce_birthday")
				.setCalendarComponent("cal_Birthday");

		final DictMultiSelectInput ce_tags = new DictMultiSelectInput("ce_tags");

		final TextButton ce_dept = new TextButton("ce_dept")
				.setOnclick("$Actions['ContactsEditPage_deptSelect']();");
		final InputElement ce_job = new InputElement("ce_job");
		final InputElement ce_email = new InputElement("ce_email");
		final InputElement ce_mobile = new InputElement("ce_mobile");
		final InputElement ce_workphone = new InputElement("ce_workphone");
		final InputElement ce_workphone2 = new InputElement("ce_workphone2");
		final InputElement ce_fax = new InputElement("ce_fax");
		final InputElement ce_homephone = new InputElement("ce_homephone");
		final InputElement ce_qq = new InputElement("ce_qq");
		final InputElement ce_weixin = new InputElement("ce_weixin");
		final InputElement ce_postcode = new InputElement("ce_postcode");
		final InputElement ce_workaddress = new InputElement("ce_workaddress");
		final InputElement ce_homeaddress = new InputElement("ce_homeaddress");
		final InputElement ce_description = InputElement.textarea("ce_description").setRows(3);

		final TableRow r1 = new TableRow(
				new RowField($m("ContactsEditPage.0"), ce_id, ce_text).setStarMark(true), new RowField(
						$m("ContactsEditPage.16"), ce_postcode));
		final TableRow r2 = new TableRow(new RowField($m("ContactsEditPage.2"), ce_sex),
				new RowField($m("ContactsEditPage.3"), ce_birthday));
		final TableRow r3 = new TableRow(new RowField($m("ContactsEditPage.4"), ce_dept),
				new RowField($m("ContactsEditPage.5"), ce_job));
		final TableRow r4 = new TableRow(new RowField($m("ContactsEditPage.6"), ce_email),
				new RowField($m("ContactsEditPage.7"), ce_mobile));
		final TableRow r5 = new TableRow(new RowField($m("ContactsEditPage.8"), ce_workphone),
				new RowField($m("ContactsEditPage.9"), ce_workphone2));
		final TableRow r6 = new TableRow(new RowField($m("ContactsEditPage.10"), ce_fax),
				new RowField($m("ContactsEditPage.11"), ce_homephone));
		final TableRow r7 = new TableRow(new RowField($m("ContactsEditPage.14"), ce_qq),
				new RowField($m("ContactsEditPage.15"), ce_weixin));

		final TableRow r8 = new TableRow(new RowField($m("ContactsEditPage.1"), ce_tags));
		final TableRow r9 = new TableRow(new RowField($m("ContactsEditPage.12"), ce_workaddress));
		final TableRow r10 = new TableRow(new RowField($m("ContactsEditPage.13"), ce_homeaddress));
		final TableRow r11 = new TableRow(new RowField($m("AccountEditPage.15"), ce_description));

		return TableRows.of(r1, r2, r3, r4, r5, r6, r7, r8, r9, r10, r11);
	}

	@Override
	public boolean isButtonsOnTop(final PageParameter pp) {
		return true;
	}

	@Override
	public String getLabelWidth(final PageParameter pp) {
		return "80px";
	}
}