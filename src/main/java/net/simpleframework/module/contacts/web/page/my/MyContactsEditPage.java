package net.simpleframework.module.contacts.web.page.my;

import net.simpleframework.ado.query.IDataQuery;
import net.simpleframework.ctx.service.ado.db.IDbBeanService;
import net.simpleframework.module.contacts.Contacts;
import net.simpleframework.module.contacts.ContactsTag;
import net.simpleframework.module.contacts.IMyContactsTagService;
import net.simpleframework.module.contacts.MyContacts;
import net.simpleframework.module.contacts.web.page.ContactsEditPage;
import net.simpleframework.mvc.PageParameter;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public class MyContactsEditPage extends ContactsEditPage {
	@SuppressWarnings("unchecked")
	@Override
	protected IDbBeanService<MyContacts> getContactsService() {
		return _mycontactsService;
	}

	@Override
	protected IDbBeanService<?> getContactsTagService() {
		return _mycontactsTagService;
	}

	@Override
	protected ContactsTag getContactsTag(final PageParameter pp) {
		return null;
	}

	@Override
	protected Contacts createContacts(final PageParameter pp) {
		final MyContacts contacts = getContactsService().createBean();
		contacts.setOwnerId(pp.getLoginId());
		contacts.setOrgId(pp.getLDomainId());
		return contacts;
	}

	@Override
	protected IDataQuery<? extends ContactsTag> queryTags(final PageParameter pp) {
		return ((IMyContactsTagService) getContactsTagService()).queryMyTags(pp.getLoginId());
	}
}
