package net.simpleframework.module.contacts.web.page.mgr2;

import static net.simpleframework.common.I18n.$m;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

import net.simpleframework.common.StringUtils;
import net.simpleframework.common.coll.ArrayUtils;
import net.simpleframework.module.contacts.Contacts;
import net.simpleframework.module.contacts.ContactsTag;
import net.simpleframework.module.contacts.web.page.ContactsEditPage;
import net.simpleframework.module.contacts.web.page.ContactsUtils;
import net.simpleframework.mvc.PageParameter;
import net.simpleframework.mvc.common.element.ButtonElement;
import net.simpleframework.mvc.common.element.ElementList;
import net.simpleframework.mvc.common.element.LinkButton;
import net.simpleframework.mvc.common.element.TableRows;
import net.simpleframework.mvc.component.ComponentParameter;
import net.simpleframework.mvc.component.base.ajaxrequest.AjaxRequestBean;
import net.simpleframework.mvc.component.ui.pager.EPagerBarLayout;
import net.simpleframework.mvc.component.ui.pager.TablePagerBean;
import net.simpleframework.mvc.component.ui.pager.TablePagerColumn;
import net.simpleframework.mvc.template.struct.FilterButton;
import net.simpleframework.mvc.template.struct.FilterButtons;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public class TongxunluTPage extends ContactsTPage {
	private static ContactsTag tag;

	@Override
	protected void onForward(final PageParameter pp) throws Exception {
		super.onForward(pp);
		getContactsTag(pp);

		// 查看
		final AjaxRequestBean ajaxRequest = addAjaxRequest(pp, "TongxunluTPage_viewPage",
				_ContactsEditPage.class);
		addWindowBean(pp, "TongxunluTPage_view", ajaxRequest).setTitle($m("ContactsTPage.1"))
				.setHeight(500).setWidth(620);
	}

	@Override
	protected ContactsTag getContactsTag(final PageParameter pp) {
		if (tag == null) {
			tag = _contactsTagService.getContactsTag(pp.getLDomainId(), $m("TongxunluTPage.0"), true);
		}
		return tag;
	}

	@Override
	protected boolean isReadonly(final PageParameter pp) {
		return true;
	}

	@Override
	public ElementList getLeftElements(final PageParameter pp) {
		return null;
	}

	@Override
	protected TablePagerBean addTablePagerBean(final PageParameter pp) {
		final TablePagerBean tablePager = (TablePagerBean) super.addTablePagerBean(pp,
				"ContactsTPage_tbl", TongxunluTbl.class).setPagerBarLayout(EPagerBarLayout.bottom)
						.setContainerId("idContactsTPage_tbl");
		tablePager.addColumn(ContactsUtils.TC_TEXT().setWidth(120)).addColumn(ContactsUtils.TC_SEX())
				.addColumn(ContactsUtils.TC_JOB().setWidth(120))
				.addColumn(ContactsUtils.TC_DEPT().setWidth(200)).addColumn(ContactsUtils.TC_DESC())
				.addColumn(TablePagerColumn.OPE(70));
		return tablePager;
	}

	@Override
	protected String toHtml(final PageParameter pp, final Map<String, Object> variables,
			final String currentVariable) throws IOException {
		final StringBuilder sb = new StringBuilder();
		final FilterButtons btns = FilterButtons.of();
		final Set<String> set = ArrayUtils.asSet(StringUtils.split(pp.getParameter("py"), ";"));
		for (final String py : set) {
			if (py.length() == 1 && Character.isLetter(py.charAt(0))) {
				final ArrayList<String> _set = new ArrayList<String>(set);
				_set.remove(py);
				btns.add(new FilterButton(py.toUpperCase())
						.setOnclick("$Actions.reloc('py=" + StringUtils.join(_set, ";") + "')"));
			}
		}
		if (btns.size() > 0) {
			sb.append("<div class='txl-filter'>");
			sb.append(btns);
			sb.append("</div>");
		}

		sb.append("<div class='pynav'>");
		for (int i = 'A'; i <= 'Z'; i++) {
			final String py = String.valueOf((char) i);
			final ArrayList<String> _set = new ArrayList<String>(set);
			if (!_set.contains(py)) {
				_set.add(py);
			}

			sb.append(LinkButton.corner(py)
					.setOnclick("$Actions.reloc('py=" + StringUtils.join(_set, ";") + "')"));
		}
		sb.append("</div>");
		if (isShowNavtree(pp)) {
			sb.append("<div class='contact-nav clearfix'>");
			sb.append(" <div class='left' id='idContactsTPage_orgTree'></div>");
			sb.append(" <div class='right' id='idContactsTPage_tbl'></div>");
			sb.append("</div>");
		} else {
			sb.append("<div id='idContactsTPage_tbl'></div>");
		}
		return sb.toString();
	}

	public static class TongxunluTbl extends ContactsTbl {
		@Override
		protected String toOpeHTML(final ComponentParameter cp, final Contacts contacts) {
			final StringBuilder sb = new StringBuilder();
			sb.append(new ButtonElement($m("Button.View")).setOnclick(
					"$Actions['TongxunluTPage_view']('contactsId=" + contacts.getId() + "');"));
			return sb.toString();
		}
	}

	public static class _ContactsEditPage extends ContactsEditPage {
		@Override
		public ElementList getLeftElements(final PageParameter pp) {
			return null;
		}

		@Override
		public ElementList getRightElements(final PageParameter pp) {
			final ElementList el = super.getRightElements(pp);
			el.remove(0);
			return el;
		}

		@Override
		public boolean isButtonsOnTop(final PageParameter pp) {
			return false;
		}

		@Override
		protected TableRows getTableRows(final PageParameter pp) {
			return super.getTableRows(pp).setReadonly(true);
		}

		@Override
		protected ContactsTag getContactsTag(final PageParameter pp) {
			return tag;
		}
	}
}
