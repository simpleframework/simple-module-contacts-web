package net.simpleframework.module.contacts.web.page.mgr2;

import static net.simpleframework.common.I18n.$m;

import java.io.IOException;
import java.util.Map;

import net.simpleframework.ado.query.IDataQuery;
import net.simpleframework.common.StringUtils;
import net.simpleframework.common.coll.KVMap;
import net.simpleframework.ctx.trans.Transaction;
import net.simpleframework.module.common.web.page.AbstractMgrTPage;
import net.simpleframework.module.contacts.Contacts;
import net.simpleframework.module.contacts.ContactsTag;
import net.simpleframework.module.contacts.ContactsTagR;
import net.simpleframework.module.contacts.IContactsContext;
import net.simpleframework.module.contacts.IContactsContextAware;
import net.simpleframework.module.contacts.web.page.ContactsEditPage;
import net.simpleframework.module.contacts.web.page.ContactsTagPage;
import net.simpleframework.module.contacts.web.page.ContactsUtils;
import net.simpleframework.mvc.IForward;
import net.simpleframework.mvc.JavascriptForward;
import net.simpleframework.mvc.PageParameter;
import net.simpleframework.mvc.common.element.ButtonElement;
import net.simpleframework.mvc.common.element.ElementList;
import net.simpleframework.mvc.common.element.LinkButton;
import net.simpleframework.mvc.common.element.SpanElement;
import net.simpleframework.mvc.component.ComponentParameter;
import net.simpleframework.mvc.component.base.ajaxrequest.AjaxRequestBean;
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
public class ContactsTPage extends AbstractMgrTPage implements IContactsContextAware {
	@Override
	protected void onForward(final PageParameter pp) throws Exception {
		super.onForward(pp);

		pp.addImportCSS(ContactsUtils.class, "/contacts.css");

		addTablePagerBean(pp);

		// 添加
		AjaxRequestBean ajaxRequest = addAjaxRequest(pp, "ContactsTPage_editPage",
				ContactsEditPage.class);
		addWindowBean(pp, "ContactsTPage_edit", ajaxRequest).setTitle($m("ContactsTPage.1"))
				.setHeight(500).setWidth(620);

		// 删除
		addDeleteAjaxRequest(pp, "ContactsTPage_del");

		// 标签管理
		ajaxRequest = addAjaxRequest(pp, "ContactsTPage_tagPage", ContactsTagPage.class);
		addWindowBean(pp, "ContactsTPage_tag", ajaxRequest).setTitle($m("ContactsTagPage.0"))
				.setHeight(500).setWidth(400);
	}

	@Override
	public ElementList getRightElements(final PageParameter pp) {
		return ElementList.of(LinkButton.addBtn().setOnclick("$Actions['ContactsTPage_edit']();"),
				SpanElement.SPACE,
				new LinkButton($m("ContactsTPage.0")).setOnclick("$Actions['ContactsTPage_tag']();"));
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

	@Transaction(context = IContactsContext.class)
	public IForward doDelete(final ComponentParameter cp) {
		final Object[] ids = StringUtils.split(cp.getParameter("id"));
		_contactsService.delete(ids);
		return new JavascriptForward("$Actions['ContactsTPage_tbl']();");
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
			return _contactsService.queryContacts(ContactsUtils.getDomainId(cp));
		}

		@Override
		protected Map<String, Object> getRowData(final ComponentParameter cp, final Object dataObject) {
			final Contacts contacts = (Contacts) dataObject;
			final KVMap row = new KVMap();
			row.add("text", contacts.getText()).add("desc", toDescHTML(cp, contacts))
					.add("tags", toTagsHTML(cp, contacts))
					.add(TablePagerColumn.OPE, toOpeHTML(cp, contacts));
			return row;
		}

		protected String toTagsHTML(final ComponentParameter cp, final Contacts contacts) {
			final StringBuilder sb = new StringBuilder();
			final IDataQuery<ContactsTagR> dq = _contactsTagRService.queryTagRs(contacts);
			ContactsTagR tagr;
			while ((tagr = dq.next()) != null) {
				final ContactsTag tag = _contactsTagService.getBean(tagr.getTagId());
				if (tag != null) {
					sb.append(new SpanElement(tag).setClassName("contact-tag"));
				}
			}
			return sb.toString();
		}

		protected String toDescHTML(final ComponentParameter cp, final Contacts contacts) {
			final StringBuilder sb = new StringBuilder();
			final String address = contacts.getWorkaddress();
			if (StringUtils.hasText(address)) {
				sb.append(address).append("<br>");
			}
			final String mobile = contacts.getMobile();
			if (StringUtils.hasText(mobile)) {
				sb.append(mobile).append(", ");
			}
			final String email = contacts.getEmail();
			if (StringUtils.hasText(email)) {
				sb.append(email);
			}
			return sb.toString();
		}

		protected String toOpeHTML(final ComponentParameter cp, final Contacts contacts) {
			final StringBuilder sb = new StringBuilder();
			sb.append(ButtonElement.editBtn().setOnclick(
					"$Actions['ContactsTPage_edit']('contactsId=" + contacts.getId() + "');"));
			sb.append(SpanElement.SPACE);
			sb.append(ButtonElement.deleteBtn().setOnclick("$Actions['ContactsTPage_del']();"));
			return sb.toString();
		}
	}
}