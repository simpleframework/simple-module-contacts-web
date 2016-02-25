package net.simpleframework.module.contacts.web.component.select;

import net.simpleframework.ado.query.IDataQuery;
import net.simpleframework.module.contacts.Contacts;
import net.simpleframework.module.contacts.IContactsContextAware;
import net.simpleframework.mvc.component.ComponentParameter;
import net.simpleframework.mvc.component.ui.dictionary.IDictionaryHandle;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public interface IContactsSelectHandler extends IDictionaryHandle, IContactsContextAware {

	/**
	 * 获取联系人列表
	 * 
	 * @param cp
	 * @return
	 */
	IDataQuery<? extends Contacts> getContacts(ComponentParameter cp);
}
