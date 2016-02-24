package net.simpleframework.module.contacts.web.page.my;

import net.simpleframework.ado.query.IDataQuery;
import net.simpleframework.common.BeanUtils;
import net.simpleframework.common.Convert;
import net.simpleframework.ctx.trans.Transaction;
import net.simpleframework.module.contacts.ContactsTag;
import net.simpleframework.module.contacts.IContactsContext;
import net.simpleframework.module.contacts.MyContacts;
import net.simpleframework.module.contacts.MyContactsTag;
import net.simpleframework.module.contacts.web.page.ContactsEditPage;
import net.simpleframework.mvc.JavascriptForward;
import net.simpleframework.mvc.PageParameter;
import net.simpleframework.mvc.component.ComponentParameter;
import net.simpleframework.mvc.component.ui.listbox.IListboxHandler;
import net.simpleframework.mvc.component.ui.listbox.ListItem;
import net.simpleframework.mvc.component.ui.listbox.ListItems;
import net.simpleframework.mvc.component.ui.listbox.ListboxBean;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public class MyContactsEditPage extends ContactsEditPage {

	@Override
	protected Class<? extends IListboxHandler> getTagSelectClass(final PageParameter pp) {
		return _TagListbox.class;
	}

	@Transaction(context = IContactsContext.class)
	@Override
	public JavascriptForward onSave(final ComponentParameter cp) throws Exception {
		MyContacts contacts = _mycontactsService.getBean(cp.getParameter("ce_id"));

		final boolean insert = contacts == null;
		if (insert) {
			contacts = _mycontactsService.createBean();
			contacts.setOwnerId(cp.getLoginId());
			contacts.setOrgId(cp.getLDomainId());
		}

		for (final String prop : _PROPs) {
			BeanUtils.setProperty(contacts, prop, cp.getParameter("ce_" + prop));
		}
		contacts.setBirthday(Convert.toDate(cp.getParameter("ce_birthday"), "yyyy-MM-dd"));
		if (insert) {
			_mycontactsService.insert(contacts);
		} else {
			_mycontactsService.update(contacts);
		}

		// 同步tag
		syncTags(cp, contacts);

		return new JavascriptForward(
				"$Actions['ContactsTPage_edit'].close(); $Actions['ContactsTPage_tbl']();");
	}

	public static class _TagListbox extends TagListbox {

		@Override
		public ListItems getListItems(final ComponentParameter cp) {
			final ListItems items = ListItems.of();
			final IDataQuery<MyContactsTag> dq = _mycontactsTagService.queryMyTags(cp.getLoginId());
			ContactsTag tag;
			while ((tag = dq.next()) != null) {
				items.add(new ListItem((ListboxBean) cp.componentBean, tag.getText()).setId(tag.getId()));
			}
			return items;
		}
	}
}
