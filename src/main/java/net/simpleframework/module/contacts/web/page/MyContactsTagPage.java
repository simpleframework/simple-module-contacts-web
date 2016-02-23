package net.simpleframework.module.contacts.web.page;

import static net.simpleframework.common.I18n.$m;
import net.simpleframework.ado.query.IDataQuery;
import net.simpleframework.mvc.PageParameter;
import net.simpleframework.mvc.component.ComponentParameter;
import net.simpleframework.mvc.component.base.ajaxrequest.AjaxRequestBean;
import net.simpleframework.mvc.component.ui.pager.TablePagerBean;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public class MyContactsTagPage extends ContactsTagPage {

	@Override
	protected void addTagEditComponent(final PageParameter pp) {
		final AjaxRequestBean ajaxRequest = addAjaxRequest(pp, "ContactsTagPage_addPage",
				_AddTagPage.class);
		addWindowBean(pp, "ContactsTagPage_add", ajaxRequest).setTitle($m("ContactsTagPage.0"))
				.setHeight(300).setWidth(360);
	}

	@Override
	protected TablePagerBean addTablePagerBean(final PageParameter pp) {
		return (TablePagerBean) super.addTablePagerBean(pp).setHandlerClass(_ContactsTagTbl.class);
	}

	public static class _ContactsTagTbl extends ContactsTagTbl {
		@Override
		public IDataQuery<?> createDataObjectQuery(final ComponentParameter cp) {
			// _mycontactsTagService
			return null;
		}
	}

	public static class _AddTagPage extends AddTagPage {
	}
}
