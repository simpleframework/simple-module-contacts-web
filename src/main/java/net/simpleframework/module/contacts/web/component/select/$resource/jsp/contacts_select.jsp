<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="net.simpleframework.module.contacts.web.component.select.ContactsSelectUtils"%>
<%@ page import="net.simpleframework.mvc.component.ComponentParameter"%>
<%
	final ComponentParameter cp = ContactsSelectUtils.get(request,
			response);
%>
<div class="contacts_select">
  <%=ContactsSelectUtils.toTblHTML(cp)%>
</div>
<script type="text/javascript">
  $ready(function() {
  });
</script>