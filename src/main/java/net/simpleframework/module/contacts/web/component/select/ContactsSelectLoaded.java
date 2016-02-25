package net.simpleframework.module.contacts.web.component.select;

import java.util.Map;

import net.simpleframework.ado.query.IDataQuery;
import net.simpleframework.common.coll.KVMap;
import net.simpleframework.module.contacts.Contacts;
import net.simpleframework.mvc.DefaultPageHandler;
import net.simpleframework.mvc.PageParameter;
import net.simpleframework.mvc.component.ComponentParameter;
import net.simpleframework.mvc.component.ComponentUtils;
import net.simpleframework.mvc.component.ui.pager.EPagerBarLayout;
import net.simpleframework.mvc.component.ui.pager.TablePagerBean;
import net.simpleframework.mvc.component.ui.pager.TablePagerColumn;
import net.simpleframework.mvc.component.ui.pager.db.AbstractDbTablePagerHandler;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public class ContactsSelectLoaded extends DefaultPageHandler {

	@Override
	public void onBeforeComponentRender(final PageParameter pp) {
		final ComponentParameter cp = ContactsSelectUtils.get(pp);
		final ContactsSelectBean contactsSelect = (ContactsSelectBean) cp.componentBean;

		final String componentName = cp.getComponentName();

		final TablePagerBean tablePager = (TablePagerBean) pp
				.addComponentBean(componentName + "_tablePager", TablePagerBean.class)
				// .setJsRowDblclick("$Actions['" + userSelectName +
				// "'].doDblclick(item);")
				.setShowHead(false).setPagerBarLayout(EPagerBarLayout.top).setShowEditPageItems(false)
				.setExportAction("false").setIndexPages(4)
				.setContainerId("contacts_" + contactsSelect.hashId())
				.setHandlerClass(ContactsList.class).setAttr("contactsSelect", contactsSelect);
		tablePager.addColumn(new TablePagerColumn("text").setWidth(120)).addColumn(
				new TablePagerColumn("dept"));
	}

	public static class ContactsList extends AbstractDbTablePagerHandler {
		@Override
		public IDataQuery<?> createDataObjectQuery(final ComponentParameter cp) {
			final ComponentParameter nCP = ContactsSelectUtils.get(cp);
			return ((IContactsSelectHandler) nCP.getComponentHandler()).getContacts(nCP);
		}

		@Override
		public Object getBeanProperty(final ComponentParameter cp, final String beanProperty) {
			if ("pageItems".equals(beanProperty)) {
				return ContactsSelectUtils.get(cp).getBeanProperty(beanProperty);
			} else if ("showCheckbox".equals(beanProperty)) {
				return ContactsSelectUtils.get(cp).getBeanProperty("multiple");
			}
			return super.getBeanProperty(cp, beanProperty);
		}

		@Override
		public Map<String, Object> getFormParameters(final ComponentParameter cp) {
			final KVMap kv = (KVMap) super.getFormParameters(cp);
			kv.putAll(ComponentUtils.toFormParameters(ContactsSelectUtils.get(cp)));
			return kv.add(ContactsSelectUtils.BEAN_ID, cp.getParameter(ContactsSelectUtils.BEAN_ID));
		}

		@Override
		protected Map<String, Object> getRowData(final ComponentParameter cp, final Object dataObject) {
			final Contacts contacts = (Contacts) dataObject;
			return new KVMap().add("text", contacts.getText()).add("dept", contacts.getDept());
		}
	}
}
