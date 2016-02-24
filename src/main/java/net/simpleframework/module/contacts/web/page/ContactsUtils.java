package net.simpleframework.module.contacts.web.page;

import java.util.ArrayList;
import java.util.Set;

import net.simpleframework.common.ID;
import net.simpleframework.common.StringUtils;
import net.simpleframework.common.coll.ArrayUtils;
import net.simpleframework.ctx.service.ado.db.IDbBeanService;
import net.simpleframework.module.contacts.ContactsTag;
import net.simpleframework.mvc.AbstractMVCPage;
import net.simpleframework.mvc.PageParameter;
import net.simpleframework.mvc.template.struct.FilterButton;
import net.simpleframework.mvc.template.struct.FilterButtons;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public abstract class ContactsUtils {

	public static ID getDomainId(final PageParameter pp) {
		return AbstractMVCPage.getPermissionOrg(pp).getId();
	}

	public static String toContactFilterHTML(final PageParameter pp,
			final IDbBeanService<?> tagService) {
		final StringBuilder sb = new StringBuilder();
		final FilterButtons btns = FilterButtons.of();
		final Set<String> tags = ArrayUtils.asSet(StringUtils.split(pp.getParameter("tags"), ";"));
		for (final String tagId : tags) {
			final ContactsTag tag = (ContactsTag) tagService.getBean(tagId);
			if (tag != null) {
				final ArrayList<String> _tags = new ArrayList<String>(tags);
				_tags.remove(tagId);
				btns.append(new FilterButton(tag).setOndelete("$Actions.reloc('tags="
						+ StringUtils.join(_tags, ";") + "')"));
			}
		}
		if (btns.size() > 0) {
			sb.append("<div class='contact-filter'>");
			sb.append(btns);
			sb.append("</div>");
		}
		return sb.toString();
	}
}
