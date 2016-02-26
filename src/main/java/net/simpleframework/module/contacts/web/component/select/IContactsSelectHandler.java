package net.simpleframework.module.contacts.web.component.select;

import java.util.Map;

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

	/**
	 * 获取联系人的属性, 该属性被放在DIV中, 可通过 selects[0].row.getAttribute("XXX")获取
	 * selects是字典选择后回调返回的参数
	 * 
	 * @param cp
	 * @param contacts
	 * @return
	 */
	Map<String, Object> getContactsAttributes(ComponentParameter cp, Contacts contacts);
}
