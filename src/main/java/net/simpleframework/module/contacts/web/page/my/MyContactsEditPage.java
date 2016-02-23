package net.simpleframework.module.contacts.web.page.my;

import net.simpleframework.ctx.trans.Transaction;
import net.simpleframework.module.contacts.IContactsContext;
import net.simpleframework.module.contacts.web.page.ContactsEditPage;
import net.simpleframework.mvc.JavascriptForward;
import net.simpleframework.mvc.component.ComponentParameter;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public class MyContactsEditPage extends ContactsEditPage {

	@Transaction(context = IContactsContext.class)
	@Override
	public JavascriptForward onSave(final ComponentParameter cp) throws Exception {
		// MyContacts contacts =
		// _mycontactsService.getBean(cp.getParameter("ce_id"));
		// final boolean insert = contacts == null;
		// if (insert) {
		// contacts = _contactsService.createBean();
		// }
		//
		// for (final String prop : _PROPs) {
		// BeanUtils.setProperty(contacts, prop, cp.getParameter("ce_" + prop));
		// }
		// contacts.setBirthday(Convert.toDate(cp.getParameter("ce_birthday"),
		// "yyyy-MM-dd"));
		// if (insert) {
		// _contactsService.insert(contacts);
		// } else {
		// _contactsService.update(contacts);
		// }
		//
		// // 同步tag
		// syncTags(cp, contacts);

		return new JavascriptForward(
				"$Actions['ContactsTPage_edit'].close(); $Actions['ContactsTPage_tbl']();");
	}

}
