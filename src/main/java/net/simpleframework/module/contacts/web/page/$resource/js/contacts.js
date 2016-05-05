var ContactsEditPage = {

  userSelected : function(selects) {
    var act = $Actions['ContactsEditPage_us_callback'];
    act.jsCompleteCallback = function(rep) {
      var rt = rep.responseText.evalJSON().rt.evalJSON();
      $A(
          [ "text", "sex", "birthday", "dept", "job", "mobile", "email",
              "workphone", "homephone", "workaddress", "homeaddress",
              "postcode", "description", "userId", "deptId" ]).each(function(name) {
        var val = rt[name];
        if (val) {
          $Actions.setValue("ce_" + name, val);
        }
      });
    };
    act('userId=' + selects[0].id);
    return true;
  }
}