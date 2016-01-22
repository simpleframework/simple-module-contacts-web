package net.simpleframework.module.contacts.web.page.my;

import static net.simpleframework.common.I18n.$m;
import net.simpleframework.module.contacts.IContactsContextAware;
import net.simpleframework.module.contacts.web.page.ContactsEditPage;
import net.simpleframework.mvc.PageParameter;
import net.simpleframework.mvc.common.element.ElementList;
import net.simpleframework.mvc.common.element.LinkButton;
import net.simpleframework.mvc.component.base.ajaxrequest.AjaxRequestBean;
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
		pp.addImportCSS(MyContactsTPage.class, "/my-contacts.css");

		// 添加表格
		addTablePagerBean(pp);

		// 编辑窗口
		final AjaxRequestBean ajaxRequest = addAjaxRequest(pp, "MyContactsTPage_editPage",
				ContactsEditPage.class);
		addWindowBean(pp, "MyContactsTPage_edit", ajaxRequest).setTitle($m("MyContactsTPage.0"))
				.setHeight(500).setWidth(620);
	}

	protected TablePagerBean addTablePagerBean(final PageParameter pp) {
		final TablePagerBean tablePager = (TablePagerBean) super.addTablePagerBean(pp,
				"MyContactsTPage_tbl", MyContactsTbl.class).setContainerId("idMyContactsTPage_tbl");
		tablePager.addColumn(new TablePagerColumn("name")).addColumn(TablePagerColumn.OPE(70));
		return tablePager;
	}

	@Override
	public ElementList getRightElements(final PageParameter pp) {
		return ElementList.of(LinkButton.addBtn().setOnclick("$Actions['MyContactsTPage_edit']();"));
	}

	@Override
	protected String toContentHTML(final PageParameter pp) {
		final StringBuilder sb = new StringBuilder();
		sb.append("<div class='cc'>");
		sb.append(" <div id='idMyContactsTPage_tbl'></div>");
		sb.append("</div>");
		return sb.toString();
	}

	public static class MyContactsTbl extends AbstractDbTablePagerHandler {
	}
}