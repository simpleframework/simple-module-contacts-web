package net.simpleframework.module.contacts.web.component.select;

import java.util.Map;

import net.simpleframework.ado.query.IDataQuery;
import net.simpleframework.common.ID;
import net.simpleframework.common.coll.KVMap;
import net.simpleframework.module.contacts.Contacts;
import net.simpleframework.module.contacts.ContactsTag;
import net.simpleframework.module.contacts.MyContactsTag;
import net.simpleframework.mvc.component.ComponentParameter;
import net.simpleframework.mvc.component.ui.dictionary.AbstractDictionaryHandler;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public class DefaultContactsSelectHandler extends AbstractDictionaryHandler
		implements IContactsSelectHandler {

	@Override
	public IDataQuery<? extends Contacts> getContacts(final ComponentParameter cp) {
		final String tagText = (String) cp.getBeanProperty("tagText");
		if ((Boolean) cp.getBeanProperty("mycontacts")) {
			final ID loginId = cp.getLoginId();
			final MyContactsTag tag = _mycontactsTagService.getMyContactsTag(loginId, tagText, false);
			if (tag != null) {
				return _mycontactsService.queryMyContacts(loginId, tag);
			} else {
				return _mycontactsService.queryMyContacts(loginId);
			}
		} else {
			final ID orgId = cp.getLDomainId();
			final ContactsTag tag = _contactsTagService.getContactsTag(orgId, tagText, false);
			if (tag != null) {
				return _contactsService.queryContacts(orgId, tag);
			} else {
				return _contactsService.queryContacts(orgId);
			}
		}
	}

	@Override
	public Map<String, Object> getContactsAttributes(final ComponentParameter cp,
			final Contacts contacts) {
		return new KVMap().add("contactsText", contacts.getText());
	}
}
