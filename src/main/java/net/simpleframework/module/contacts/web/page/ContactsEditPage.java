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
	protected TableRows getTableRows(final PageParameter pp) {
		final TableRow r1 = new TableRow(new RowField($m("ContactsEditPage.0"),
				InputElement.hidden("ce_id"), new InputElement("ce_text")).setStarMark(true),
				new RowField($m("ContactsEditPage.1"), new InputElement("ce_tags")));
		return TableRows.of(r1);
	}
}