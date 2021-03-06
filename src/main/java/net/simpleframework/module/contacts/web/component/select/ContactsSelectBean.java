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
	private static final long serialVersionUID = 8528168339364653590L;

	/* 每页的数据数量 */
	private int pageItems;

	/* 是否我的联系人 */
	private boolean mycontacts = true;
	/* 标签名 */
	private String tagText;

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

	public boolean isMycontacts() {
		return mycontacts;
	}

	public void setMycontacts(final boolean mycontacts) {
		this.mycontacts = mycontacts;
	}

	public String getTagText() {
		return tagText;
	}

	public void setTagText(final String tagText) {
		this.tagText = tagText;
	}

	@Override
	public String getHandlerClass() {
		final String sClass = super.getHandlerClass();
		return StringUtils.hasText(sClass) ? sClass : DefaultContactsSelectHandler.class.getName();
	}
}
