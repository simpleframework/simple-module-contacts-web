package net.simpleframework.module.contacts.web.page;

import static net.simpleframework.common.I18n.$m;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import net.simpleframework.ado.query.IDataQuery;
import net.simpleframework.common.BeanUtils;
import net.simpleframework.common.Convert;
import net.simpleframework.common.ID;
import net.simpleframework.common.StringUtils;
import net.simpleframework.ctx.permission.PermissionUser;
import net.simpleframework.ctx.trans.Transaction;
import net.simpleframework.module.contacts.Contacts;
import net.simpleframework.module.contacts.ContactsTag;
import net.simpleframework.module.contacts.ContactsTagR;
import net.simpleframework.module.contacts.IContactsContext;
import net.simpleframework.module.contacts.IContactsContextAware;
import net.simpleframework.mvc.IForward;
import net.simpleframework.mvc.JavascriptForward;
import net.simpleframework.mvc.JsonForward;
import net.simpleframework.mvc.PageParameter;
import net.simpleframework.mvc.common.element.CalendarInput;
import net.simpleframework.mvc.common.element.DictMultiSelectInput;
import net.simpleframework.mvc.common.element.ElementList;
import net.simpleframework.mvc.common.element.InputElement;
import net.simpleframework.mvc.common.element.LinkButton;
import net.simpleframework.mvc.common.element.Option;
import net.simpleframework.mvc.common.element.RowField;
import net.simpleframework.mvc.common.element.TableRow;
import net.simpleframework.mvc.common.element.TableRows;
import net.simpleframework.mvc.common.element.TextButton;
import net.simpleframework.mvc.component.ComponentParameter;
import net.simpleframework.mvc.component.base.validation.EValidatorMethod;
import net.simpleframework.mvc.component.base.validation.Validator;
import net.simpleframework.mvc.component.ext.deptselect.DeptSelectBean;
import net.simpleframework.mvc.component.ext.userselect.UserSelectBean;
import net.simpleframework.mvc.component.ui.dictionary.DictionaryBean;
import net.simpleframework.mvc.component.ui.listbox.AbstractListboxHandler;
import net.simpleframework.mvc.component.ui.listbox.ListItem;
import net.simpleframework.mvc.component.ui.listbox.ListItems;
import net.simpleframework.mvc.component.ui.listbox.ListboxBean;
import net.simpleframework.mvc.template.lets.FormTableRowTemplatePage;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public class ContactsEditPage extends FormTableRowTemplatePage implements IContactsContextAware {
	@Override
	protected void onForward(final PageParameter pp) throws Exception {
		super.onForward(pp);

		// 日历
		addCalendarBean(pp, "cal_Birthday");
		// 部门选择
		addComponentBean(pp, "ContactsEditPage_deptSelect", DeptSelectBean.class).setClearAction(
				"false").setBindingText("ce_dept");

		// 帐号选择
		addComponentBean(pp, "ContactsEditPage_userselect", UserSelectBean.class).setShowGroupOpt(
				false).setJsSelectCallback("return ContactsEditPage.userSelected(selects)");
		// 帐号选择Action
		addAjaxRequest(pp, "ContactsEditPage_us_callback").setHandlerMethod("doUserSelect");

		// tag字典
		addComponentBean(pp, "ContactsEditPage_tagList", ListboxBean.class).setCheckbox(true)
				.setHandlerClass(TagListbox.class);
		addComponentBean(pp, "ContactsEditPage_tag", DictionaryBean.class)
				.addListboxRef(pp, "ContactsEditPage_tagList").setDestroyOnClose(true)
				.setTitle($m("ContactsEditPage.18"));

		// 表单验证
		addFormValidationBean(pp).addValidators(new Validator(EValidatorMethod.required, "#ce_text"))
				.addValidators(new Validator(EValidatorMethod.email, "#ce_email"))
				.addValidators(new Validator(EValidatorMethod.mobile_phone, "#ce_mobile"))
				.addValidators(new Validator(EValidatorMethod.date, "#ce_birthday", "yyyy-MM-dd"));
	}

	@Override
	protected String getPageCSS(final PageParameter pp) {
		return "ContactsEditPage";
	}

	public IForward doUserSelect(final ComponentParameter cp) {
		final PermissionUser user = cp.getUser(cp.getParameter("userId"));
		return new JsonForward().put("text", user.getText()).put("sex", user.getSex())
				.put("birthday", Convert.toDateString(user.getBirthday(), "yyyy-MM-dd"))
				.put("dept", user.getDept().toString()).put("job", user.getNick())
				.put("mobile", user.getMobile()).put("email", user.getEmail())
				.put("workphone", user.getProperty("officePhone"))
				.put("homephone", user.getProperty("homePhone"))
				.put("workaddress", user.getProperty("address"))
				.put("homeaddress", user.getProperty("hometown"))
				.put("postcode", user.getProperty("postcode"))
				.put("description", user.getDescription());
	}

	@Transaction(context = IContactsContext.class)
	@Override
	public JavascriptForward onSave(final ComponentParameter cp) throws Exception {
		Contacts contacts = _contactsService.getBean(cp.getParameter("ce_id"));
		final boolean insert = contacts == null;
		if (insert) {
			contacts = _contactsService.createBean();
		}

		for (final String prop : _PROPs) {
			BeanUtils.setProperty(contacts, prop, cp.getParameter("ce_" + prop));
		}
		contacts.setBirthday(Convert.toDate(cp.getParameter("ce_birthday"), "yyyy-MM-dd"));
		if (insert) {
			_contactsService.insert(contacts);
		} else {
			_contactsService.update(contacts);
		}

		// 同步tag
		syncTags(cp, contacts);

		return new JavascriptForward(
				"$Actions['ContactsTPage_edit'].close(); $Actions['ContactsTPage_tbl']();");
	}

	protected static String[] _PROPs = new String[] { "text", "postcode", "sex", "dept", "job",
			"email", "mobile", "workphone", "workphone2", "fax", "homephone", "qq", "weixin",
			"workaddress", "homeaddress", "description" };

	protected void syncTags(final ComponentParameter cp, final Contacts contacts) {
		final Map<ID, ContactsTag> adds = new LinkedHashMap<ID, ContactsTag>();
		for (final String tagId : StringUtils.split(cp.getParameter("ce_tags"))) {
			final ContactsTag tag = _contactsTagService.getBean(tagId);
			if (tag != null) {
				adds.put(tag.getId(), tag);
			}
		}
		final List<ID> removes = new ArrayList<ID>();
		final IDataQuery<ContactsTagR> dq = _contactsTagRService.queryTagRs(contacts);
		ContactsTagR tagr;
		while ((tagr = dq.next()) != null) {
			final ID tagId = tagr.getTagId();
			if (adds.containsKey(tagId)) {
				adds.remove(tagId);
			} else {
				removes.add(tagr.getId());
			}
		}

		for (final ID rid : removes) {
			_contactsTagRService.delete(rid);
		}
		for (final ContactsTag tag : adds.values()) {
			_contactsTagRService.addSubjectTagR(contacts, tag);
		}
	}

	@Override
	public ElementList getLeftElements(final PageParameter pp) {
		return ElementList.of(LinkButton.corner($m("ContactsEditPage.17")).setOnclick(
				"$Actions['ContactsEditPage_userselect']();"));
	}

	@Override
	protected TableRows getTableRows(final PageParameter pp) {
		final InputElement ce_id = InputElement.hidden("ce_id");
		final InputElement ce_text = new InputElement("ce_text");
		final InputElement ce_postcode = new InputElement("ce_postcode");

		final Option[] opts = Option.from($m("AccountEditPage.16"), $m("AccountEditPage.17"));
		final InputElement ce_sex = InputElement.select("ce_sex");

		final CalendarInput ce_birthday = new CalendarInput("ce_birthday")
				.setCalendarComponent("cal_Birthday");

		final DictMultiSelectInput ce_tags = (DictMultiSelectInput) new DictMultiSelectInput(
				"ce_tags").setDictComponent("ContactsEditPage_tag");

		final TextButton ce_dept = new TextButton("ce_dept")
				.setOnclick("$Actions['ContactsEditPage_deptSelect']();");
		final InputElement ce_job = new InputElement("ce_job");
		final InputElement ce_email = new InputElement("ce_email");
		final InputElement ce_mobile = new InputElement("ce_mobile");
		final InputElement ce_workphone = new InputElement("ce_workphone");
		final InputElement ce_workphone2 = new InputElement("ce_workphone2");
		final InputElement ce_fax = new InputElement("ce_fax");
		final InputElement ce_homephone = new InputElement("ce_homephone");
		final InputElement ce_qq = new InputElement("ce_qq");
		final InputElement ce_weixin = new InputElement("ce_weixin");
		final InputElement ce_workaddress = new InputElement("ce_workaddress");
		final InputElement ce_homeaddress = new InputElement("ce_homeaddress");
		final InputElement ce_description = InputElement.textarea("ce_description").setRows(3);

		final Contacts contacts = _contactsService.getBean(pp.getParameter("contactsId"));
		if (contacts != null) {
			ce_id.setVal(contacts.getId());
			ce_text.setVal(contacts.getText());
			ce_postcode.setVal(contacts.getPostcode());
			Option.setSelected(opts, contacts.getSex());
			ce_birthday.setVal(Convert.toDateString(contacts.getBirthday(), "yyyy-MM-dd"));
			ce_dept.setVal(contacts.getDept());
			ce_job.setVal(contacts.getJob());
			ce_email.setVal(contacts.getEmail());
			ce_mobile.setVal(contacts.getMobile());
			ce_workphone.setVal(contacts.getWorkphone());
			ce_workphone2.setVal(contacts.getWorkphone2());
			ce_fax.setVal(contacts.getFax());
			ce_homephone.setVal(contacts.getHomephone());
			ce_qq.setVal(contacts.getQq());
			ce_weixin.setVal(contacts.getWeixin());
			ce_workaddress.setVal(contacts.getWorkaddress());
			ce_homeaddress.setVal(contacts.getHomeaddress());
			ce_description.setVal(contacts.getDescription());

			final IDataQuery<ContactsTagR> dq = _contactsTagRService.queryTagRs(contacts);
			ContactsTagR tagr;
			while ((tagr = dq.next()) != null) {
				final ContactsTag tag = _contactsTagService.getBean(tagr.getTagId());
				ce_tags.addValue(Convert.toString(tag.getId()), tag.getText());
			}
		}

		final TableRow r1 = new TableRow(
				new RowField($m("ContactsEditPage.0"), ce_id, ce_text).setStarMark(true), new RowField(
						$m("ContactsEditPage.16"), ce_postcode));
		final TableRow r2 = new TableRow(new RowField($m("ContactsEditPage.2"),
				ce_sex.addElements(opts)), new RowField($m("ContactsEditPage.3"), ce_birthday));
		final TableRow r3 = new TableRow(new RowField($m("ContactsEditPage.4"), ce_dept),
				new RowField($m("ContactsEditPage.5"), ce_job));
		final TableRow r4 = new TableRow(new RowField($m("ContactsEditPage.6"), ce_email),
				new RowField($m("ContactsEditPage.7"), ce_mobile));
		final TableRow r5 = new TableRow(new RowField($m("ContactsEditPage.8"), ce_workphone),
				new RowField($m("ContactsEditPage.9"), ce_workphone2));
		final TableRow r6 = new TableRow(new RowField($m("ContactsEditPage.10"), ce_fax),
				new RowField($m("ContactsEditPage.11"), ce_homephone));
		final TableRow r7 = new TableRow(new RowField($m("ContactsEditPage.14"), ce_qq),
				new RowField($m("ContactsEditPage.15"), ce_weixin));

		final TableRow r8 = new TableRow(new RowField($m("ContactsEditPage.1"), ce_tags));
		final TableRow r9 = new TableRow(new RowField($m("ContactsEditPage.12"), ce_workaddress));
		final TableRow r10 = new TableRow(new RowField($m("ContactsEditPage.13"), ce_homeaddress));
		final TableRow r11 = new TableRow(new RowField($m("AccountEditPage.15"), ce_description));

		return TableRows.of(r1, r2, r3, r4, r5, r6, r7, r8, r9, r10, r11);
	}

	@Override
	public boolean isButtonsOnTop(final PageParameter pp) {
		return true;
	}

	@Override
	public String getLabelWidth(final PageParameter pp) {
		return "80px";
	}

	public static class TagListbox extends AbstractListboxHandler {

		@Override
		public ListItems getListItems(final ComponentParameter cp) {
			final ListItems items = ListItems.of();
			final IDataQuery<ContactsTag> dq = _contactsTagService.queryOrgTags(ContactsUtils
					.getDomainId(cp));
			ContactsTag tag;
			while ((tag = dq.next()) != null) {
				items.add(new ListItem((ListboxBean) cp.componentBean, tag.getText()).setId(tag.getId()));
			}
			return items;
		}
	}
}