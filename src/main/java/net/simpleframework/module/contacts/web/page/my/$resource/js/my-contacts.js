
var ContactsEditPage = {
    
    userSelected : function(selects) {
      var act = $Actions['ContactsEditPage_us_callback'];
      act.jsCompleteCallback = function(rep) {
        var rt = rep.responseText.evalJSON().rt.evalJSON();
        $("ce_text").value = rt.text;
      };
      act('userId=' + selects[0].id);
      return true;
    }
}