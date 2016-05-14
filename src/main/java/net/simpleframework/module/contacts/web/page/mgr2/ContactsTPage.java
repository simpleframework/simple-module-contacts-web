package net.simpleframework.module.contacts.web.page.mgr2;

import static net.simpleframework.common.I18n.$m;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.simpleframework.ado.FilterItem;
import net.simpleframework.ado.db.DbDataQuery;
import net.simpleframework.ado.db.common.ExpressionValue;
import net.simpleframework.ado.query.IDataQuery;
import net.simpleframework.common.StringUtils;
import net.simpleframework.common.coll.ArrayUtils;
import net.simpleframework.common.coll.KVMap;
import net.simpleframework.ctx.permission.PermissionDept;
import net.simpleframework.ctx.service.ado.db.IDbBeanService;
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
import net.simpleframework.mvc.AbstractMVCPage;
import net.simpleframework.mvc.IForward;
import net.simpleframework.mvc.JavascriptForward;
import net.simpleframework.mvc.PageParameter;
import net.simpleframework.mvc.common.element.ButtonElement;
import net.simpleframework.mvc.common.element.ElementList;
import net.simpleframework.mvc.common.element.LinkButton;
import net.simpleframework.mvc.common.element.LinkElement;
import net.simpleframework.mvc.common.element.SpanElement;
import net.simpleframework.mvc.component.ComponentParameter;
import net.simpleframework.mvc.component.base.ajaxrequest.AjaxRequestBean;
import net.simpleframework.mvc.component.ui.menu.MenuBean;
import net.simpleframework.mvc.component.ui.menu.MenuItem;
import net.simpleframework.mvc.component.ui.menu.MenuItems;
import net.simpleframework.mvc.component.ui.pager.AbstractTablePagerSchema;
import net.simpleframework.mvc.component.ui.pager.EPagerBarLayout;
import net.simpleframework.mvc.component.ui.pager.TablePagerBean;
import net.simpleframework.mvc.component.ui.pager.TablePagerColumn;
import net.simpleframework.mvc.component.ui.pager.TablePagerUtils;
import net.simpleframework.mvc.component.ui.pager.db.AbstractDbTablePagerHandler;
import net.simpleframework.mvc.component.ui.tree.AbstractTreeHandler;
import net.simpleframework.mvc.component.ui.tree.TreeBean;
import net.simpleframework.mvc.component.ui.tree.TreeNode;
import net.simpleframework.mvc.component.ui.tree.TreeNodes;

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

		if (!isReadonly(pp)) {
			pp.addImportJavascript(ContactsUtils.class, "/js/contacts.js");

			// 删除
			addDeleteAjaxRequest(pp, "ContactsTPage_del");
			// 移动
			addAjaxRequest(pp, "ContactsTPage_Move").setHandlerMethod("doMove");

			// 添加
			AjaxRequestBean ajaxRequest = addAjaxRequest(pp, "ContactsTPage_editPage",
					getContactsEditPage());
			addWindowBean(pp, "ContactsTPage_edit", ajaxRequest).setTitle($m("ContactsTPage.1"))
					.setHeight(540).setWidth(620);

			// 标签管理
			ajaxRequest = addAjaxRequest(pp, "ContactsTPage_tagPage", ContactsTagPage.class);
			addWindowBean(pp, "ContactsTPage_tag", ajaxRequest).setTitle($m("ContactsTagPage.0"))
					.setHeight(500).setWidth(400);
		}

		if (isShowNavtree(pp)) {
			addTreeBean(pp, "ContactsTPage_orgTree").setContainerId("idContactsTPage_orgTree")
					.setHandlerClass(OrgNavTree.class);
		}
	}

	protected Class<? extends AbstractMVCPage> getContactsEditPage() {
		return ContactsEditPage.class;
	}

	protected boolean isShowNavtree(final PageParameter pp) {
		return true;
	}

	protected boolean isReadonly(final PageParameter pp) {
		// 是否只读
		return false;
	}

	protected ContactsTag getContactsTag(final PageParameter pp) {
		return null;
	}

	@Transaction(context = IContactsContext.class)
	public IForward doDelete(final ComponentParameter cp) {
		final Object[] ids = StringUtils.split(cp.getParameter("id"));
		_contactsService.delete(ids);
		return new JavascriptForward("$Actions['ContactsTPage_tbl']();");
	}

	@Transaction(context = IContactsContext.class)
	public IForward doMove(final ComponentParameter cp) {
		_contactsService.exchange(TablePagerUtils.getExchangeBeans(cp, _contactsService));
		return new JavascriptForward("$Actions['ContactsTPage_tbl']();");
	}

	protected TablePagerBean addTablePagerBean(final PageParameter pp) {
		final TablePagerBean tablePager = (TablePagerBean) super
				.addTablePagerBean(pp, "ContactsTPage_tbl", ContactsTbl.class)
				.setPagerBarLayout(EPagerBarLayout.bottom).setContainerId("idContactsTPage_tbl");
		if (getContactsTag(pp) == null) {
			tablePager.addColumn(ContactsUtils.TC_TEXT()).addColumn(ContactsUtils.TC_JOB())
					.addColumn(ContactsUtils.TC_DEPT()).addColumn(ContactsUtils.TC_DESC())
					.addColumn(ContactsUtils.TC_TAGs());
		} else {
			tablePager.addColumn(ContactsUtils.TC_TEXT().setWidth(120))
					.addColumn(ContactsUtils.TC_SEX()).addColumn(ContactsUtils.TC_JOB().setWidth(120))
					.addColumn(ContactsUtils.TC_DEPT().setWidth(200)).addColumn(ContactsUtils.TC_DESC());
		}
		tablePager.addColumn(TablePagerColumn.OPE(120));
		return tablePager;
	}

	@Override
	public ElementList getRightElements(final PageParameter pp) {
		if (!isReadonly(pp)) {
			final ContactsTag tag = getContactsTag(pp);
			final LinkButton addBtn = LinkButton.addBtn();
			final ElementList el = ElementList.of();
			if (tag == null) {
				el.append(addBtn.setOnclick("$Actions['ContactsTPage_edit']();"), SpanElement.SPACE,
						new LinkButton($m("ContactsTPage.0"))
								.setOnclick("$Actions['ContactsTPage_tag']();"));
			} else {
				el.add(addBtn.setOnclick("$Actions['ContactsTPage_edit']('tagId=" + tag.getId() + "');"));
			}
			return el;
		}
		return super.getRightElements(pp);
	}

	@Override
	protected String getPageCSS(final PageParameter pp) {
		return getClassName(getClass());
	}

	@Override
	protected String toHtml(final PageParameter pp, final Map<String, Object> variables,
			final String currentVariable) throws IOException {
		final StringBuilder sb = new StringBuilder();
		sb.append(ContactsUtils.toContactFilterHTML(pp, _contactsTagService));
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

	public static class ContactsTbl extends AbstractDbTablePagerHandler {

		protected IDbBeanService<?> getContactsTagService() {
			return _contactsTagService;
		}

		@Override
		protected ExpressionValue createFilterExpressionValue(final DbDataQuery<?> qs,
				final TablePagerColumn oCol, final Collection<FilterItem> coll) {
			final String col = oCol.getColumnName();
			if ("text".equals(col)) {
				final ExpressionValue ev = super.createFilterExpressionValue(qs, oCol, coll);
				final ExpressionValue ev2 = super.createFilterExpressionValue(qs, new TablePagerColumn(
						"py"), coll);
				ev.setExpression("((" + ev.getExpression() + ") or (" + ev2.getExpression() + "))");
				ev.addValues(ev2.getValues());
				return ev;
			}
			return super.createFilterExpressionValue(qs, oCol, coll);
		}

		@Override
		public IDataQuery<?> createDataObjectQuery(final ComponentParameter cp) {
			final String py = cp.getParameter("py");
			cp.addFormParameter("py", py);
			final List<?> list = getTags(cp);
			final String deptId = cp.getParameter("deptId");
			if (StringUtils.hasText(deptId)) {
				cp.addFormParameter("deptId", deptId);
			}
			return _contactsService.queryContacts(ContactsUtils.getDomainId(cp), deptId, py,
					list.toArray(new ContactsTag[list.size()]));
		}

		protected List<?> getTags(final ComponentParameter cp) {
			final ArrayList<ContactsTag> list = new ArrayList<ContactsTag>();
			final AbstractMVCPage page = get(cp);
			ContactsTag tag;
			if (page instanceof ContactsTPage
					&& (tag = ((ContactsTPage) page).getContactsTag(cp)) != null) {
				list.add(tag);
			} else {
				final String tags = cp.getParameter("tags");
				if (StringUtils.hasText(tags)) {
					cp.addFormParameter("tags", tags);

					for (final String tagId : ArrayUtils.asSet(StringUtils.split(tags, ";"))) {
						tag = (ContactsTag) getContactsTagService().getBean(tagId);
						if (tag != null) {
							list.add(tag);
						}
					}
				}
			}
			return list;
		}

		@Override
		protected Map<String, Object> getRowData(final ComponentParameter cp, final Object dataObject) {
			final Contacts contacts = (Contacts) dataObject;
			final KVMap row = new KVMap();
			row.add("text", contacts.getText()).add("sex", contacts.getSex())
					.add("dept", contacts.getDept()).add("job", contacts.getJob())
					.add("desc", toDescHTML(cp, contacts)).add("tags", toTagsHTML(cp, contacts))
					.add(TablePagerColumn.OPE, toOpeHTML(cp, contacts));
			return row;
		}

		protected String toTagsHTML(final ComponentParameter cp, final Contacts contacts) {
			final StringBuilder sb = new StringBuilder();
			final IDataQuery<ContactsTagR> dq = _contactsTagRService.queryTagRs(contacts);
			ContactsTagR tagr;
			final Set<String> tags = ArrayUtils.asSet(StringUtils.split(cp.getParameter("tags"), ";"));
			while ((tagr = dq.next()) != null) {
				final ContactsTag tag = (ContactsTag) getContactsTagService().getBean(tagr.getTagId());
				if (tag != null) {
					final ArrayList<String> _tags = new ArrayList<String>(tags);
					final String _tagId = String.valueOf(tag.getId());
					if (!_tags.contains(_tagId)) {
						_tags.add(_tagId);
					}
					sb.append(new SpanElement(tag).setClassName("contact-tag").setOnclick(
							"$Actions.reloc('tags=" + StringUtils.join(_tags, ";") + "');"));
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
				sb.append(new LinkElement(email).setHref("mailto: " + email));
			}
			return sb.toString();
		}

		protected String toOpeHTML(final ComponentParameter cp, final Contacts contacts) {
			final StringBuilder sb = new StringBuilder();
			String js = "$Actions['ContactsTPage_edit']('contactsId=" + contacts.getId();
			final AbstractMVCPage page = get(cp);
			ContactsTag tag;
			if (page instanceof ContactsTPage
					&& (tag = ((ContactsTPage) page).getContactsTag(cp)) != null) {
				js += "&tagId=" + tag.getId();
			}
			js += "');";
			sb.append(ButtonElement.editBtn().setOnclick(js));
			sb.append(SpanElement.SPACE);
			sb.append(ButtonElement.deleteBtn().setOnclick(
					"$Actions['ContactsTPage_del']('id=" + contacts.getId() + "');"));
			sb.append(AbstractTablePagerSchema.IMG_DOWNMENU);
			return sb.toString();
		}

		@Override
		public MenuItems getContextMenu(final ComponentParameter cp, final MenuBean menuBean,
				final MenuItem menuItem) {
			if (menuItem == null) {
				final MenuItems items = MenuItems.of();
				items.append(MenuItem.TBL_MOVE_UP("ContactsTPage_Move"));
				items.append(MenuItem.TBL_MOVE_UP2("ContactsTPage_Move"));
				items.append(MenuItem.TBL_MOVE_DOWN("ContactsTPage_Move"));
				items.append(MenuItem.TBL_MOVE_DOWN2("ContactsTPage_Move"));
				return items;
			}
			return null;
		}
	}

	public static class OrgNavTree extends AbstractTreeHandler {
		@Override
		public TreeNodes getTreenodes(final ComponentParameter cp, final TreeNode parent) {
			final TreeNodes nodes = TreeNodes.of();
			final TreeBean treeBean = (TreeBean) cp.componentBean;
			if (parent == null) {
				nodes.add(new TreeNode(treeBean, $m("ContactsTPage.8"))
						.setJsClickCallback("$Actions['ContactsTPage_tbl']('deptId=__del')"));
				nodes.add(new TreeNode(treeBean, cp.getDept(cp.getLDomainId())).setOpened(true));
			} else {
				final Object dataObject = parent.getDataObject();
				if (dataObject instanceof PermissionDept) {
					final PermissionDept dept = (PermissionDept) dataObject;
					for (final PermissionDept c : dept.getDeptChildren()) {
						final TreeNode tn = new TreeNode(treeBean, c);
						nodes.add(tn);
					}
					parent.setJsClickCallback("$Actions['ContactsTPage_tbl']('deptId=" + dept.getId()
							+ "')");
				}
			}
			return nodes;
		}
	}
}