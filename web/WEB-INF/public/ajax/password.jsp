<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib tagdir="/WEB-INF/tags/bootstrap/" prefix="bs" %>
<%@taglib tagdir="/WEB-INF/tags/language/" prefix="language" %>
<bs:modal icon="warning">
    <form method="POST" name="form" action=""  class="form-mail row">
        <label>Ancien mot de pass</label>
        <input type="password" required name="password" class="form-control">
        <label>Nouveau Mo de pass</label>
        <input type="password" required onchange="form.confirmpassword.pattern=this.value" name="newPassword" class="form-control">
        <label>confirmation</label>
        <input title="mot de pass et confirmation non identiques" required type="password" name="passwordConfirmation" class="form-control">
        <label></label>
        <input type="hidden" name="UPDATE" value="TRUE">
        <button class="btn btn-success"> <span class="fa fa-save"></span></button>
        <button type="button" data-dismiss="modal" class="btn btn-warning"> <span class="fa fa-remove"></span></button>
    </form>
</bs:modal>
