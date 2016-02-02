package net.simpleframework.module.contacts.web.page.mgr2;

import static net.simpleframework.common.I18n.$m;

import java.io.IOException;
import java.util.Map;

import net.simpleframework.ado.query.IDataQuery;
import net.simpleframework.common.coll.KVMap;
import net.simpleframework.module.common.web.page.AbstractMgrTPage;
import net.simpleframework.module.contacts.web.page.ContactsTagPage;
import net.simpleframework.module.contacts.web.page.ContactsUtils;
import net.simpleframework.mvc.PageParameter;
import net.simpleframework.mvc.common.element.ElementList;
import net.simpleframework.mvc.common.element.LinkButton;
import net.simpleframework.mvc.common.element.SpanElement;
import net.simpleframework.mvc.component.ComponentParameter;
import net.simpleframework.mvc.component.base.ajaxrequest.AjaxRequestBean;
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
public class ContactsTPage extends AbstractMgrTPage {
	@Override
	protected void onForward(final PageParameter pp) throws Exception {
		super.onForward(pp);

		pp.addImportCSS(ContactsUtils.class, "/contacts.css");

		addTablePagerBean(pp);

		// 标签管理
		final AjaxRequestBean ajaxRequest = addAjaxRequest(pp, "ContactsTPage_tagPage",
				ContactsTagPage.class);
		addWindowBean(pp, "ContactsTPage_tag", ajaxRequest).setTitle($m("ContactsTagPage.0"))
				.setHeight(500).setWidth(400);
	}

	@Override
	public ElementList getRightElements(final PageParameter pp) {
		return ElementList.of(LinkButton.addBtn(), SpanElement.SPACE, new LinkButton(
				$m("ContactsTPage.0")).setOnclick("$Actions['ContactsTPage_tag']();"));
	}

	protected TablePagerBean addTablePagerBean(final PageParameter pp) {
		final TablePagerBean tablePager = (TablePagerBean) super.addTablePagerBean(pp,
				"ContactsTPage_tbl", ContactsTbl.class).setContainerId("idContactsTPage_tbl");
		tablePager.addColumn(new TablePagerColumn("name")).addColumn(TablePagerColumn.OPE(70));
		return tablePager;
	}

	@Override
	protected String toHtml(final PageParameter pp, final Map<String, Object> variables,
			final String currentVariable) throws IOException {
		final StringBuilder sb = new StringBuilder();
		sb.append("<div id='idContactsTPage_tbl'></div>");
		return sb.toString();
	}

	public static class ContactsTbl extends AbstractDbTablePagerHandler {
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