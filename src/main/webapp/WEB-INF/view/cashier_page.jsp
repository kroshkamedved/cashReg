<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="my" uri="myTaglib" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<html>
<head>
    <script type="text/javascript" src="${app}/js/index.js"></script>
    <title>Commodity expert page</title>
    <%@include file="includes/head.jsp" %>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <jsp:useBean id="loc" scope="session" type="java.lang.String" class="java.lang.String"/>
    <fmt:setLocale value="${loc}"/>
    <fmt:setBundle basename="language"/>

</head>
<body>
<%@include file="includes/cashier_navbar.jsp" %>
<div class="container" style="padding: 50px 100px 100px 100px">
    <h1 align="center">
        <fmt:message key="common.info.cabinet.greetings"/>, ${usr.role} ${usr.login}
        <br>
        <hr>
    </h1>
    <form action="/fp/controller" method="post">
        <input name="command" value="addProductToCart" type="hidden">
        <fieldset style="display: table-column; width: 100%">
            <legend><fmt:message key="cashier.actions.addProductToCart"/></legend>
            <p>
                <label><fmt:message key="common.info.cabinet.productName"/></label>
                <input type="text" name="prod_identifier" id="prod_identifier"
                       placeholder="*product name or id*">
            </p>
            <p>
                <button type="submit" class="btn btn-light" onclick="return empty()">
                    <fmt:message key="cashier.actions.addProductToCartButton"/>
                    </button>
            </p>
        </fieldset>
    </form>
    <div class="card w-80 mx-auto my-8">
        <c:if test="${fn:length(cart) > 0}">
        <div class="card-header text-center"><fmt:message key="cashier.cart.info.header"/></div>
        <div class="card-body" align="center">
            <form action="/fp/controller" method="post">
                <table class="table">
                    <thead class="thead-dark">
                    <tr>
                        <th scope="col"><fmt:message key="commodity.info.cabinet.table.itemId"/></th>
                        <th scope="col"><fmt:message key="commodity.info.cabinet.table.itemName"/></th>
                        <th scope="col"><fmt:message key="commodity.info.cabinet.table.description"/></th>
                        <th scope="col"><fmt:message key="commodity.info.cabinet.table.units"/></th>
                        <th scope="col"><fmt:message key="commodity.info.cabinet.table.pricePerUnit"/></th>
                        <th scope="col"><fmt:message key="common.info.cabinet.quantity"/></th>
                        <th scope="col"><fmt:message key="commodity.actions.deleteProductFromCart"/></th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach var="item" items="${cart.keySet()}">
                        <tr>
                            <td>${item.productID}</td>
                            <td>${item.productName}</td>
                            <td>${item.productDescription}</td>
                            <td><my:unitTag unit="${item.productUnitId}"/></td>
                            <td>${item.productPrice}</td>
                            <td>
                                <form action="/fp/controller" method="post">
                                    <input type="hidden" name="command" value="setItemQuantity">
                                    <select name="unit_quantity" id="select">
                                        <c:forEach begin="${cart.get(item)}" end="${item.productQuantity}"
                                                   var="k">
                                            <option value="${k}">${k}</option>
                                        </c:forEach>
                                    </select>
                                    <button type="submit" name="edit_goods_id" value="${item.productID}">OK
                                    </button>
                                </form>
                            </td>
                            <td>
                                <form action="/fp/cabinet/cashier_page" method="get">
                                    <input type="hidden" name="command" value="deleteItem">
                                    <button name="deleteItemId" value="${item.productID}"
                                            type="submit">X
                                    </button>
                                </form>
                            </td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
                <section>
                    <div class="buttons">

                        <c:if test="${cart.size() gt 0}">
                            <form action="/fp/cabinet/cashier_page" method="get">
                                <button type="submit" class="btn btn-danger" name="clear" value="true"/>
                            </form>
                            <form action="/fp/controller" method="post">
                                <input type="hidden" name="command" value="confirmCheck">
                                "<fmt:message key="cashier.cart.action.clean"/>"</button>
                                <button type="submit" class="btn btn-success" name="checkClosed" value="true">
                                    <fmt:message
                                            key="cashier.actions.confirmOrder"/></button>
                            </form>
                        </c:if>
                    </div>
                </section>
            </form>
            </form>
        </div>
    </div>
    </c:if>
    <%@include file="includes/footer.jsp" %>
</body>
</html>