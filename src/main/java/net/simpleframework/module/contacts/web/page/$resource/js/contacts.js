var ContactsEditPage = {

  userSelected : function(selects) {
    var act = $Actions['ContactsEditPage_us_callback'];
    act.jsCompleteCallback = function(rep) {
      var rt = rep.responseText.evalJSON().rt.evalJSON();
      $A([ "text", "birthday", "dept" ]).each(function(name) {
        var val = rt[name];
        if (val)
          $("ce_" + name).value = val;
      });
    };
    act('userId=' + selects[0].id);
    return true;
  }
}