<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib tagdir="/WEB-INF/tags/bootstrap/" prefix="bs" %>
<%@taglib tagdir="/WEB-INF/tags/language/" prefix="language" %>
<tr>
    <td><span class="fa fa-bars"></span></td>
    <td>${entity.product.libele}</td>
    <td>${entity.quantite}</td>
    <td>${entity.prix}</td>
    <td>${entity.fournisseur.nom}</td>
    <td>
        <form method="POST" class="form-remove">
            <button type="submit" class="btn btn-danger btn-xs btn-block">
                <span class="fa fa-remove"></span> <language:language message="remove"/>
            </button>
            <input type="hidden" name="STOCK"  value="TRUE">
            <input type="hidden" name="REMOVE" value="TRUE">
            <input type="hidden" name="product" value="F${entity.fournisseur.id}P${entity.product.id}D${entity.date}">
        </form>
    </td>
</tr>