package net.simpleframework.module.contacts.web.component.select;

import static net.simpleframework.common.I18n.$m;
import net.simpleframework.common.StringUtils;
import net.simpleframework.mvc.component.ui.dictionary.DictionaryBean;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public class ContactsSelectBean extends DictionaryBean {
	/* 每页的数据数量 */
	private int pageItems;

	public ContactsSelectBean() {
		setShowHelpTooltip(false);
		setTitle($m("ContactsSelectBean.0"));
		setMinWidth(360);
		setWidth(360);
		setHeight(445);
		setPageItems(100);
	}

	public int getPageItems() {
		return pageItems;
	}

	public ContactsSelectBean setPageItems(final int pageItems) {
		this.pageItems = pageItems;
		return this;
	}

	@Override
	public String getHandlerClass() {
		final String sClass = super.getHandlerClass();
		return StringUtils.hasText(sClass) ? sClass : DefaultContactsSelectHandler.class.getName();
	}
}
