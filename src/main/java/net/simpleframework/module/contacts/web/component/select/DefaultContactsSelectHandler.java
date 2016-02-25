package net.simpleframework.module.contacts.web.component.select;

import net.simpleframework.ado.query.IDataQuery;
import net.simpleframework.module.contacts.Contacts;
import net.simpleframework.mvc.component.ComponentParameter;
import net.simpleframework.mvc.component.ui.dictionary.AbstractDictionaryHandler;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public class DefaultContactsSelectHandler extends AbstractDictionaryHandler implements
		IContactsSelectHandler {

	@Override
	public IDataQuery<? extends Contacts> getContacts(final ComponentParameter cp) {
		return _mycontactsService.queryMyContacts(cp.getLoginId());
	}
}
