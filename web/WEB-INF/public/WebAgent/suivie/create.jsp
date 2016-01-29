<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib tagdir="/WEB-INF/tags/bootstrap/" prefix="bs" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib  tagdir="/WEB-INF/tags/language/" prefix="language" %>
<bs:modal id="client" icon="money" title="${facture.id}">
    <form method="POST" action="" class="row">
        <label><language:language  message="costumer"/></label>
        <input type="text" name="nom" readonly="readonly" required value="${facture.client.nom}" class="form-control">
        <label><language:language  message="mode"/></label>
        <select  class="input-sm form-control" required name="mode">
            <c:forEach items="${modes}" var="item">
                <option value="${item.id}">${item.name}</option>
            </c:forEach>
        </select>
        <label><language:language  message="amount"/></label>
        <input type="text" name="montant" required  class="form-control">
        <label><language:language  message="ref"/></label>
        <input type="text" name="ref"  class="form-control">
        <br>
        <input type="hidden" name="SAVE" value="TRUE">
        <input type="hidden" name="SUIVIE" value="TRUE">
        <input type="hidden" name="facture" value="${facture.id}">
        <input type="submit" class="btn btn-primary">
    </form>
</bs:modal>