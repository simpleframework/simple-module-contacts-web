package net.simpleframework.module.contacts.web.page.my;

import static net.simpleframework.common.I18n.$m;

import java.util.List;

import net.simpleframework.ado.query.IDataQuery;
import net.simpleframework.common.StringUtils;
import net.simpleframework.ctx.service.ado.db.IDbBeanService;
import net.simpleframework.ctx.trans.Transaction;
import net.simpleframework.module.contacts.IContactsContext;
import net.simpleframework.module.contacts.IContactsContextAware;
import net.simpleframework.module.contacts.MyContactsTag;
import net.simpleframework.module.contacts.web.page.ContactsUtils;
import net.simpleframework.module.contacts.web.page.mgr2.ContactsTPage.ContactsTbl;
import net.simpleframework.mvc.IForward;
import net.simpleframework.mvc.JavascriptForward;
import net.simpleframework.mvc.PageParameter;
import net.simpleframework.mvc.common.element.ElementList;
import net.simpleframework.mvc.common.element.LinkButton;
import net.simpleframework.mvc.common.element.SpanElement;
import net.simpleframework.mvc.component.ComponentParameter;
import net.simpleframework.mvc.component.base.ajaxrequest.AjaxRequestBean;
import net.simpleframework.mvc.component.ui.pager.EPagerBarLayout;
import net.simpleframework.mvc.component.ui.pager.TablePagerBean;
import net.simpleframework.mvc.component.ui.pager.TablePagerColumn;
import net.simpleframework.mvc.component.ui.pager.TablePagerUtils;
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

		// 删除
		addDeleteAjaxRequest(pp, "ContactsTPage_del");
		// 移动
		addAjaxRequest(pp, "ContactsTPage_Move").setHandlerMethod("doMove");

		// 编辑窗口
		AjaxRequestBean ajaxRequest = addAjaxRequest(pp, "ContactsTPage_editPage",
				MyContactsEditPage.class);
		addWindowBean(pp, "ContactsTPage_edit", ajaxRequest).setTitle($m("ContactsTPage.1"))
				.setHeight(540).setWidth(620);

		// 标签管理
		ajaxRequest = addAjaxRequest(pp, "ContactsTPage_tagPage", MyContactsTagPage.class);
		addWindowBean(pp, "ContactsTPage_tag", ajaxRequest).setTitle($m("ContactsTagPage.0"))
				.setHeight(500).setWidth(400);
	}

	protected TablePagerBean addTablePagerBean(final PageParameter pp) {
		final TablePagerBean tablePager = (TablePagerBean) super
				.addTablePagerBean(pp, "ContactsTPage_tbl", _ContactsTbl.class)
				.setPagerBarLayout(EPagerBarLayout.bottom).setContainerId("idContactsTPage_tbl");
		tablePager.addColumn(ContactsUtils.TC_TEXT()).addColumn(ContactsUtils.TC_JOB())
				.addColumn(ContactsUtils.TC_DEPT()).addColumn(ContactsUtils.TC_DESC())
				.addColumn(ContactsUtils.TC_TAGs()).addColumn(TablePagerColumn.OPE(120));
		return tablePager;
	}

	@Override
	public ElementList getRightElements(final PageParameter pp) {
		return ElementList.of(LinkButton.addBtn().setOnclick("$Actions['ContactsTPage_edit']();"),
				SpanElement.SPACE,
				new LinkButton($m("ContactsTPage.0")).setOnclick("$Actions['ContactsTPage_tag']();"));
	}

	@Transaction(context = IContactsContext.class)
	public IForward doDelete(final ComponentParameter cp) {
		final Object[] ids = StringUtils.split(cp.getParameter("id"));
		_mycontactsService.delete(ids);
		return new JavascriptForward("$Actions['ContactsTPage_tbl']();");
	}

	@Transaction(context = IContactsContext.class)
	public IForward doMove(final ComponentParameter cp) {
		_mycontactsService.exchange(TablePagerUtils.getExchangeBeans(cp, _mycontactsService));
		return new JavascriptForward("$Actions['ContactsTPage_tbl']();");
	}

	@Override
	protected String toContentHTML(final PageParameter pp) {
		final StringBuilder sb = new StringBuilder();
		sb.append(ContactsUtils.toContactFilterHTML(pp, _mycontactsTagService));
		sb.append("<div id='idContactsTPage_tbl'></div>");
		return sb.toString();
	}

	public static class _ContactsTbl extends ContactsTbl {
		@Override
		protected IDbBeanService<?> getContactsTagService() {
			return _mycontactsTagService;
		}

		@Override
		public IDataQuery<?> createDataObjectQuery(final ComponentParameter cp) {
			final List<?> list = getTags(cp);
			return _mycontactsService.queryMyContacts(cp.getLoginId(),
					list.toArray(new MyContactsTag[list.size()]));
		}
	}
}