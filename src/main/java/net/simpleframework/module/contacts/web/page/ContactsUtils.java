package net.simpleframework.module.contacts.web.page;

import net.simpleframework.common.ID;
import net.simpleframework.mvc.AbstractMVCPage;
import net.simpleframework.mvc.PageParameter;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public abstract class ContactsUtils {

	public static ID getDomainId(final PageParameter pp) {
		return AbstractMVCPage.getPermissionOrg(pp).getId();
	}
}
