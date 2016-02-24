package net.simpleframework.module.contacts.web.component.select;

import net.simpleframework.mvc.PageParameter;
import net.simpleframework.mvc.component.ComponentBean;
import net.simpleframework.mvc.component.ComponentName;
import net.simpleframework.mvc.component.ComponentResourceProvider;
import net.simpleframework.mvc.component.ComponentUtils;
import net.simpleframework.mvc.component.base.ajaxrequest.AjaxRequestBean;
import net.simpleframework.mvc.component.ui.dictionary.DictionaryRegistry;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
@ComponentName(ContactsSelectRegistry.CONTACTS_SELECT)
@ComponentBean(ContactsSelectBean.class)
@ComponentResourceProvider(ContactsSelectResourceProvider.class)
public class ContactsSelectRegistry extends DictionaryRegistry {

	public static final String CONTACTS_SELECT = "contacts-select";

	@Override
	public ContactsSelectBean createComponentBean(final PageParameter pp, final Object attriData) {
		final ContactsSelectBean contactsSelect = (ContactsSelectBean) super.createComponentBean(pp,
				attriData);
		final AjaxRequestBean ajaxRequest = (AjaxRequestBean) contactsSelect
				.getAttr(ATTRI_AJAXREQUEST);
		if (ajaxRequest != null) {
			ajaxRequest.setUrlForward(ComponentUtils.getResourceHomePath(ContactsSelectBean.class)
					+ "/jsp/contacts_select.jsp?" + ContactsSelectUtils.BEAN_ID + "="
					+ contactsSelect.hashId());
		}
		return contactsSelect;
	}
}
