package net.simpleframework.module.contacts.web.component.select;

import net.simpleframework.mvc.component.ComponentBean;
import net.simpleframework.mvc.component.ComponentName;
import net.simpleframework.mvc.component.ComponentResourceProvider;
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
}
