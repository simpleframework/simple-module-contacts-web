package net.simpleframework.module.contacts.web.page;

import static net.simpleframework.common.I18n.$m;
import net.simpleframework.module.contacts.IContactsContextAware;
import net.simpleframework.mvc.PageParameter;
import net.simpleframework.mvc.common.element.InputElement;
import net.simpleframework.mvc.common.element.RowField;
import net.simpleframework.mvc.common.element.TableRow;
import net.simpleframework.mvc.common.element.TableRows;
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
	public boolean isButtonsOnTop(final PageParameter pp) {
		return true;
	}

	@Override
	public String getLabelWidth(final PageParameter pp) {
		return "75px";
	}

	@Override
	protected TableRows getTableRows(final PageParameter pp) {
		final TableRow r1 = new TableRow(new RowField($m("ContactsEditPage.0"),
				InputElement.hidden("ce_id"), new InputElement("ce_text")).setStarMark(true),
				new RowField($m("ContactsEditPage.1"), new InputElement("ce_tags")));
		final TableRow r2 = new TableRow(new RowField($m("ContactsEditPage.2"), new InputElement(
				"ce_sex")), new RowField($m("ContactsEditPage.3"), new InputElement("ce_birthday")));
		final TableRow r3 = new TableRow(new RowField($m("ContactsEditPage.4"), new InputElement(
				"ce_dept")), new RowField($m("ContactsEditPage.5"), new InputElement("ce_job")));
		final TableRow r4 = new TableRow(new RowField($m("ContactsEditPage.6"), new InputElement(
				"ce_email")), new RowField($m("ContactsEditPage.7"), new InputElement("ce_mobile")));
		final TableRow r5 = new TableRow(new RowField($m("ContactsEditPage.8"), new InputElement(
				"ce_workphone")), new RowField($m("ContactsEditPage.9"), new InputElement(
				"ce_workphone2")));
		final TableRow r6 = new TableRow(new RowField($m("ContactsEditPage.10"), new InputElement(
				"ce_fax")), new RowField($m("ContactsEditPage.11"), new InputElement("ce_homephone")));
		final TableRow r7 = new TableRow(new RowField($m("ContactsEditPage.14"), new InputElement(
				"ce_qq")), new RowField($m("ContactsEditPage.15"), new InputElement("ce_weixin")));
		final TableRow r8 = new TableRow(new RowField($m("ContactsEditPage.16"), new InputElement(
				"ce_homeaddress")), new RowField("&nbsp"));
		final TableRow r9 = new TableRow(new RowField($m("ContactsEditPage.12"), new InputElement(
				"ce_workaddress")));
		final TableRow r10 = new TableRow(new RowField($m("ContactsEditPage.13"), new InputElement(
				"ce_homeaddress")));
		final TableRow r11 = new TableRow(new RowField($m("AccountEditPage.15"), InputElement
				.textarea("ce_description").setRows(3)));

		return TableRows.of(r1, r2, r3, r4, r5, r6, r7, r8, r9, r10, r11);
	}
}