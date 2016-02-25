package net.simpleframework.module.contacts.web.component.select;

import java.util.Map;

import net.simpleframework.ado.query.IDataQuery;
import net.simpleframework.common.coll.KVMap;
import net.simpleframework.mvc.DefaultPageHandler;
import net.simpleframework.mvc.PageParameter;
import net.simpleframework.mvc.component.ComponentParameter;
import net.simpleframework.mvc.component.ui.pager.EPagerBarLayout;
import net.simpleframework.mvc.component.ui.pager.TablePagerBean;
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
	}

	public static class ContactsList extends AbstractDbTablePagerHandler {
		@Override
		public IDataQuery<?> createDataObjectQuery(final ComponentParameter cp) {
			return super.createDataObjectQuery(cp);
		}

		@Override
		protected Map<String, Object> getRowData(final ComponentParameter cp, final Object dataObject) {
			final KVMap row = new KVMap();
			return row;
		}
	}
}
