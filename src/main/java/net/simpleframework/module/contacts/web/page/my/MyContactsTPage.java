package net.simpleframework.module.contacts.web.page.my;

import static net.simpleframework.common.I18n.$m;
import net.simpleframework.module.contacts.IContactsContextAware;
import net.simpleframework.module.contacts.web.page.ContactsEditPage;
import net.simpleframework.module.contacts.web.page.ContactsUtils;
import net.simpleframework.module.contacts.web.page.MyContactsTagPage;
import net.simpleframework.module.contacts.web.page.mgr2.ContactsTPage.ContactsTbl;
import net.simpleframework.mvc.PageParameter;
import net.simpleframework.mvc.common.element.ElementList;
import net.simpleframework.mvc.common.element.LinkButton;
import net.simpleframework.mvc.common.element.SpanElement;
import net.simpleframework.mvc.component.base.ajaxrequest.AjaxRequestBean;
import net.simpleframework.mvc.component.ui.pager.EPagerBarLayout;
import net.simpleframework.mvc.component.ui.pager.TablePagerBean;
import net.simpleframework.mvc.component.ui.pager.TablePagerColumn;
import net.simpleframework.mvc.component.ui.pager.db.AbstractDbTablePagerHandler;
import net.simpleframework.mvc.template.AbstractTBTemplatePage;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public class MyContactsTPage extends AbstractTBTemplatePage implements IContactsContextAware {

	@Override
	protected void onForward(final PageParameter pp) throws Exception {
		super.onForward(pp);

		pp.addImportCSS(ContactsUtils.class, "/contacts.css");
		pp.addImportJavascript(ContactsUtils.class, "/js/contacts.js");

		// 添加表格
		addTablePagerBean(pp);

		// 编辑窗口
		AjaxRequestBean ajaxRequest = addAjaxRequest(pp, "MyContactsTPage_editPage",
				MyContactsEditPage.class);
		addWindowBean(pp, "MyContactsTPage_edit", ajaxRequest).setTitle($m("ContactsTPage.1"))
				.setHeight(500).setWidth(620);

		// 标签管理
		ajaxRequest = addAjaxRequest(pp, "MyContactsTPage_tagPage", MyContactsTagPage.class);
		addWindowBean(pp, "MyContactsTPage_tag", ajaxRequest).setTitle($m("ContactsTagPage.0"))
				.setHeight(500).setWidth(400);
	}

	protected TablePagerBean addTablePagerBean(final PageParameter pp) {
		final TablePagerBean tablePager = (TablePagerBean) super
				.addTablePagerBean(pp, "ContactsTPage_tbl", ContactsTbl.class)
				.setPagerBarLayout(EPagerBarLayout.bottom).setContainerId("idContactsTPage_tbl");
		tablePager
				.addColumn(new TablePagerColumn("text", $m("ContactsTPage.2"), 120).setSort(false))
				.addColumn(
						new TablePagerColumn("desc", $m("ContactsTPage.3")).setLblStyle(
								"line-height:1.6;color:#666;").setFilterSort(false))
				.addColumn(
						new TablePagerColumn("tags", $m("ContactsTPage.4"), 280).setFilterSort(false))
				.addColumn(TablePagerColumn.OPE(110));
		return tablePager;
	}

	@Override
	public ElementList getRightElements(final PageParameter pp) {
		return ElementList.of(LinkButton.addBtn().setOnclick("$Actions['MyContactsTPage_edit']();"),
				SpanElement.SPACE,
				new LinkButton($m("ContactsTPage.0")).setOnclick("$Actions['MyContactsTPage_tag']();"));
	}

	@Override
	protected String toContentHTML(final PageParameter pp) {
		final StringBuilder sb = new StringBuilder();
		sb.append("<div class='cc'>");
		sb.append(" <div id='idContactsTPage_tbl'></div>");
		sb.append("</div>");
		return sb.toString();
	}

	public static class MyContactsTbl extends AbstractDbTablePagerHandler {
	}

	public static class MyContactsEditPage extends ContactsEditPage {
	}
}