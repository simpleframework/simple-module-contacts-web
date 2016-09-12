package net.simpleframework.module.contacts.web.component.select;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.simpleframework.mvc.PageRequestResponse;
import net.simpleframework.mvc.common.element.ButtonElement;
import net.simpleframework.mvc.common.element.SpanElement;
import net.simpleframework.mvc.component.ComponentParameter;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public abstract class ContactsSelectUtils {

	public static final String BEAN_ID = "contacts_select_@bid";

	public static ComponentParameter get(final PageRequestResponse rRequest) {
		return ComponentParameter.get(rRequest, BEAN_ID);
	}

	public static ComponentParameter get(final HttpServletRequest request,
			final HttpServletResponse response) {
		return ComponentParameter.get(request, response, BEAN_ID);
	}

	public static String toTblHTML(final ComponentParameter cp) {
		final StringBuilder sb = new StringBuilder();
		sb.append("<div id='contacts_").append(cp.hashId()).append("'>");
		sb.append("</div>");
		sb.append("<div class='b'>");
		final String componentName = cp.getComponentName();
		sb.append(
				ButtonElement.okBtn().setOnclick("$Actions['" + componentName + "'].doDblclick();"));
		sb.append(SpanElement.SPACE);
		sb.append(ButtonElement.cancelBtn().setOnclick("$Actions['" + componentName + "'].close();"));
		sb.append("</div>");
		return sb.toString();
	}
}