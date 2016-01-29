<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib tagdir="/WEB-INF/tags/bootstrap/" prefix="bs" %>
<%@taglib tagdir="/WEB-INF/tags/language/" prefix="language" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="f" %>
<div id="ajax" class="container-fluid alert" style="background-color: white;">
    <div class="container-fluid">
        <datalist id="produits">
            <c:forEach items="${produits}" var="item">
                <option itemid="${item.id}" itemprop="${item.quantite}">${item.libele}</option>
            </c:forEach>
        </datalist>
        <form id="form-order" class="form" method="POST">
            <div class="col-md-6 col-xs-12 thumbnail bg-gray">
                <div class="col-md-6 col-xs-6">
                    <label>Client</label>
                    <select  class="input-sm form-control" required name="client">
                        <c:forEach items="${clients}" var="item">
                            <option ${facture.client.id eq item.id ? 'selected':''} value="${item.id}">${item.nom}</option>
                        </c:forEach>
                    </select>   
                    <label>Date</label>
                    <input type="date"  required value="<f:formatDate value="${facture.date}" pattern="yyyy-MM-dd" />" name="date" class="input-sm form-control"/>
                    <br>
                    <input type="hidden" name="SET" value="TRUE">
                    <input type="submit" class="btn btn-primary" >
                </div>
                <div class="col-md-6 col-xs-6">
                    <label>TVA</label>
                    <select  class="input-sm form-control" required name="tax">
                        <c:forEach items="${taxes}" var="item">
                            <option ${facture.tax.id eq item.id ? 'selected':''} value="${item.id}">${item.value}</option>
                        </c:forEach>
                    </select>
                    <label>Type</label>
                    <select  class="input-sm form-control" required name="type">
                        <c:forEach items="${types}" var="item">
                            <option ${facture.type.ordinal() eq item.ordinal() ? 'selected':''} value="${item.ordinal()}">${item.name()}</option>
                        </c:forEach>
                    </select>
                    <label>Reglée</label>
                    <input type="checkbox" ${facture.etat ?'checked':''} name="etat">
                </div>
            </div>
        </form>
    </div>
    <form method="POST" class="form">
        <table class="table">
            <thead>
                <tr>
                    <td><language:language message="product"/></td>
                    <td><language:language message="price"/></td>
                    <td><language:language message="quantity"/></td>
                    <td></td>
                </tr>
            </thead>
            <tbody>
                <tr>
                    <td><input required list="produits" name="product" class="form-control" placeholder=""/></td>
                    <td><input required name="prix" pattern="[0-9]*[.]?[0-9]+"  class="form-control" placeholder=""/></td>
                    <td><input required name="quantite" pattern="[0-9]*" class="form-control" placeholder=""/></td>
                    <td><button  class="btn btn-default btn-block"><span class="fa fa-plus"></span></button></td>
                </tr>
            </tbody>
        </table>
        <input type="hidden" name="ADD"  value="TRUE">
    </form>
    <table class="table table-striped table-bordered table-condensed table-hover">
        <thead>
            <tr>
                <td></td>
                <td>Produit</td>
                <td>Prix</td>
                <td>Quantite</td>
                <td></td>
            </tr>
        </thead>
        <tbody id="table-body">
            <c:forEach items="${facture.items}" var="item">
                <tr>
                    <td><span class="fa fa-bars"></span></td>
                    <td>${item.product.libele}</td>
                    <td>${item.prix}</td>
                    <td>${item.quantite}</td>
                    <td>
                        <form method="POST" action="" class="form">
                            <input type="hidden" name="REMOVE" value="TRUE">
                            <input type="hidden" name="libele" value="${item.product.libele}">
                            <button class="btn btn-warning btn-xs btn-block" ><span class="fa fa-remove"></span> <language:language message="remove"/></button>
                        </form>
                    </td>
                </tr>
            </c:forEach>
        </tbody>
    </table>
    <form method="POST" action="" class="form pull-left">
        <input type="hidden" name="SAVE" value="TRUE">
        <button class="btn btn-success btn-xs btn-block" ><span class="fa fa-outdent"></span> <language:language message="save"/></button>
    </form>
    <form method="POST" action="" class="pull-right">
        <input type="hidden" name="CLEAR" value="TRUE">
        <button class="btn btn-danger btn-xs btn-block" ><span class="fa fa-close"></span> <language:language message="cancel"/></button>
    </form>
</div>