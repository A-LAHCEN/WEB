<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib tagdir="/WEB-INF/tags/bootstrap/" prefix="bs" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib tagdir="/WEB-INF/tags/language/" prefix="language" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="f" %>
<div id="ajax" class="container-fluid well well-lg">
    <!-- Main content -->
    <section class="content invoice">
        <div class="row center-block">
            <h1 class="text-center">
                ${entity.nom}  
            </h1>

        </div>
        <div class="row">
            <div class="col-xs-12 table-responsive">
                <table class="table table-striped table-bordered table-hover">
                    <thead>
                        <tr>
                            <td></td>
                            <td></td>
                            <td></td>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach items="${entity.factures}" var="item">
                            <tr class="${not item.etat ?'danger':''}">
                                <td><f:formatDate value="${item.date}" pattern="yyyy-MM-dd"/></td>
                                <td>${item.total*(1+(item.tax.value/100))}</td>
                                <td>${item.payed}</td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div><!-- /.col -->
        </div><!-- /.row -->
    </section><!-- /.content -->
</div>