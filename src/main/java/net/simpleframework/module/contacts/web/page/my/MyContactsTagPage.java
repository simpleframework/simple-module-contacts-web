package net.simpleframework.module.contacts.web.page.my;

import net.simpleframework.ado.query.IDataQuery;
import net.simpleframework.common.StringUtils;
import net.simpleframework.ctx.trans.Transaction;
import net.simpleframework.module.contacts.IContactsContext;
import net.simpleframework.module.contacts.MyContactsTag;
import net.simpleframework.module.contacts.web.page.ContactsTagPage;
import net.simpleframework.mvc.AbstractMVCPage;
import net.simpleframework.mvc.IForward;
import net.simpleframework.mvc.JavascriptForward;
import net.simpleframework.mvc.PageParameter;
import net.simpleframework.mvc.component.ComponentParameter;
import net.simpleframework.mvc.component.ui.pager.TablePagerBean;
import net.simpleframework.mvc.component.ui.pager.TablePagerUtils;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public class MyContactsTagPage extends ContactsTagPage {

	@Override
	protected TablePagerBean addTablePagerBean(final PageParameter pp) {
		return (TablePagerBean) super.addTablePagerBean(pp).setHandlerClass(_ContactsTagTbl.class);
	}

	@Override
	protected Class<? extends AbstractMVCPage> getTagEditClass(final PageParameter pp) {
		return _AddTagPage.class;
	}

	@Transaction(context = IContactsContext.class)
	@Override
	public IForward doDelete(final ComponentParameter cp) {
		final Object[] ids = StringUtils.split(cp.getParameter("id"));
		_mycontactsTagService.delete(ids);
		return new JavascriptForward("$Actions['ContactsTagPage_tbl']();");
	}

	@Transaction(context = IContactsContext.class)
	@Override
	public IForward doMove(final ComponentParameter cp) {
		_mycontactsTagService.exchange(TablePagerUtils.getExchangeBeans(cp, _mycontactsTagService));
		return new JavascriptForward("$Actions['ContactsTagPage_tbl']();");
	}

	public static class _ContactsTagTbl extends ContactsTagTbl {
		@Override
		public IDataQuery<?> createDataObjectQuery(final ComponentParameter cp) {
			return _mycontactsTagService.queryMyTags(cp.getLoginId());
		}
	}

	public static class _AddTagPage extends AddTagPage {

		@Transaction(context = IContactsContext.class)
		@Override
		public JavascriptForward onSave(final ComponentParameter cp) throws Exception {
			MyContactsTag tag = _mycontactsTagService.getBean(cp.getParameter("tag_id"));
			final boolean insert = tag == null;
			if (insert) {
				tag = _mycontactsTagService.createBean();
				tag.setOwnerId(cp.getLoginId());
				tag.setOrgId(cp.getLDomainId());
			}
			tag.setText(cp.getParameter("tag_name"));
			tag.setDescription(cp.getParameter("tag_description"));
			if (insert) {
				_mycontactsTagService.insert(tag);
			} else {
				_mycontactsTagService.update(tag);
			}
			return new JavascriptForward(
					"$Actions['ContactsTagPage_add'].close(); $Actions['ContactsTagPage_tbl']();");
		}

		@Override
		protected boolean isShowOrgEdit(final PageParameter pp) {
			return false;
		}
	}
}
