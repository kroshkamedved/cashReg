<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<html>
<head>
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/index.js"></script>
    <title>Commodity expert page</title>
    <%@include file="../../includes/head.jsp" %>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <jsp:useBean id="loc" scope="session" type="java.lang.String"/>
    <fmt:setLocale value="${loc}"/>
    <fmt:setBundle basename="language"/>

</head>
<body>
<%@include file="../../includes/cashier_navbar.jsp" %>
<div class="container">

    <h1 align="center">
        Hello, ${usr.role} ${usr.login}
        <br>
    </h1>
    <p>
        <a class="btn btn-primary" data-bs-toggle="collapse" href="#CollapseAdding" role="button"
           aria-expanded="false" aria-controls="CollapseAdding"><fmt:message key="cashier.actions.createCheck"/> </a>
    </p>
    <div class="col">
        <div class="collapse multi-collapse" id="CollapseAdding">
            <div class="card w-50 mx-auto my-5">
                <div class="card-header text-center">add new product</div>
                <div class="card-body">

                    <form action="/fp/controller" method="post">
                        <input name="command" value="addProductToCart" type="hidden">
                        <fieldset style="display: table-column; width: 100%">
                            <legend>Fulfill the form to add the product</legend>
                            <p>
                                <%--<label for="select"><fmt:message key="common.info.cabinet.quantity"/> </label>
                                <select name="unit_id" id="select">
                                    <c:forEach begin="1" end="${maxQuantity}" var="i">
                                        <option value="${i}">${i}</option>
                                    </c:forEach>
                                </select>--%>
                            </p>
                            <p>
                                <label><fmt:message key="common.info.cabinet.productName"/></label>
                                <input type="text" name="prod_identifier" id="prod_identifier"
                                       placeholder="*product name or id*">
                            </p>
                            <p>
                                <input type="submit" value="Add product" onclick="return empty()"/>
                            </p>
                        </fieldset>
                    </form>
                </div>
            </div>
        </div>
    </div>

    <div class="card w-80 mx-auto my-8">
        <div class="card-header text-center"><fmt:message key="cashier.cart.info.header"/></div>
        <div class="card-body" align="center">
            <table border="1" cellpadding="5" cellspacing="5">
                <tr>
                    <th>Item ID</th>
                    <th>Item name</th>
                    <th>Description</th>
                    <th>Units id</th>
                    <th>Price per unit</th>
                    <th><fmt:message key="common.info.cabinet.quantity"/></th>
                    <th>delete item</th>
                </tr>

                <c:forEach var="itemDTO" items="${cart}">
                    <tr>
                        <td>${itemDTO.productID}</td>
                        <td>${itemDTO.productName}</td>
                        <td>${itemDTO.productDescription}</td>
                        <td>${itemDTO.productUnitId}</td>
                        <td>${itemDTO.productPrice}</td>
                        <td>
                            <select>
                                <c:forEach begin="1" end="${itemDTO.productQuantity}" var="k">
                                    <option>${k}</option>
                                </c:forEach>
                            </select>
                        </td>
                        <td>
                            <form action="/fp/cabinet/cashier_page" method="get">
                                <input type="hidden" name="command" value="deleteItem">
                                <button name="deleteItemId" value="${itemDTO.productID}"
                                        type="submit">X
                                </button>
                            </form>
                        </td>
                    </tr>
                </c:forEach>
            </table>
            </form>
            <section>
                <div class="buttons">
                    <form>
                        <form>
                            <c:if test="${cart.size() gt 0}">
                                <button name="clear" value="true"/>
                                "<fmt:message
                                    key="cashier.cart.action.clean"/>"</button>
                                <c:if test="${clear}">
                                    <c:remove var="cart" scope="session"/>"
                                </c:if>
                            </c:if>
                        </form>
                    </form>
                </div>
            </section>
        </div>
    </div>
    <%@include file="../../includes/footer.jsp" %>
</body>
</html>