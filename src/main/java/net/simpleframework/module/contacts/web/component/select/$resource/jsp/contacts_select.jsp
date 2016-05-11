<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="net.simpleframework.module.contacts.web.component.select.ContactsSelectUtils"%>
<%@ page import="net.simpleframework.mvc.component.ComponentParameter"%>
<%@ page import="net.simpleframework.mvc.component.ui.dictionary.DictionaryRender"%>
<%
	final ComponentParameter cp = ContactsSelectUtils.get(request,
			response);
	String componentName = cp.getComponentName();
%>
<div class="contacts_select">
  <%=ContactsSelectUtils.toTblHTML(cp)%>
</div>
<script type="text/javascript">
  $ready(function() {
    var us = $Actions['<%=componentName%>'];
    var w = us.window;
    w.content.setStyle("overflow:hidden;");
    
    var tp = $Actions["<%=componentName + "_tablePager"%>"];
    var s = function() {
      tp.setHeight(w.content.getHeight() - 93);
    };
    s();
    w.observe("resize:ended", s);
    
    us.doDblclick = function(d) {
      var selects = new Array();
      var arr = tp.checkArr();
      if (arr && arr.length > 0) {
        arr.each(function(d2) {
          selects.push({
            id : tp.rowId(d2),
            text : d2.readAttribute("contactsText"),
            row: d2
          });
        });
      } else {
        if (d) {
          selects.push({
            id : tp.rowId(d),
            text : d.readAttribute("contactsText"),
            row: d
          });
        }
      }
      if (selects.length == 0) {
        alert("#(contacts_select.0)");
      } else {
        if (us.jsSelectCallback) {
          if (us.jsSelectCallback(selects))
            us.close();
        } else {
        	<%=DictionaryRender.genSelectCallback(cp, "selects")%>
        }
      }
    };
    
    tp.jsLoadedCallback = function() {
    	var allCheck = $("idAllCheck_<%=componentName%>");
    	if (allCheck) {
    	  allCheck.observe("click", function(ev) {
    	    tp.checkAll(this);
    	  });
    	}
    };
    $call(tp.jsLoadedCallback);
  });
</script>