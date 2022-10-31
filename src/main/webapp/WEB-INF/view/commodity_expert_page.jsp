<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="my" uri="myTaglib" %>
<%@ page contentType="text/html; charset=UTF-8" %>

<html>
<head>
    <title>Commodity expert page</title>
    <%@include file="includes/head.jsp" %>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <%--    <jsp:useBean id="loc" scope="session" type="java.lang.String"/>--%>
    <fmt:setLocale value="${loc}"/>
    <fmt:setBundle basename="language"/>
    <%--    <jsp:useBean id="edit" scope="request" type="java.lang.Boolean"/>--%>
</head>
<body>
<%@include file="includes/commodity_navbar.jsp" %>
<div class="container">

    <h1 align="center">
        Hello, ${usr.role} ${usr.login}
        <br>
    </h1>
    <p>
        <a class="btn btn-primary" data-bs-toggle="collapse" href="#CollapseAdding" role="button"
           aria-expanded="false" aria-controls="CollapseAdding">Add new product</a>
    </p>
    <div class="col">
        <div class="collapse multi-collapse" id="CollapseAdding">
            <div class="card w-50 mx-auto my-5">
                <div class="card-header text-center">add new product</div>
                <div class="card-body">

                    <form action="/fp/controller" method="post">
                        <input name="command" value="addProduct" type="hidden">
                        <fieldset style="display: table-column; width: 100%">
                            <legend>Fulfill the form to add the product</legend>
                            <p>
                                <label for="select"><fmt:message key="common.info.cabinet.units"/></label>
                                <select name="unit_id" id="select">
                                    <c:forEach items="${units}" var="unit">
                                        <option value="${unit.id}">${unit.name}</option>
                                    </c:forEach>
                                </select>
                            </p>
                            <p>
                                <label><label><fmt:message key="common.info.cabinet.productName"/></label></label>
                                <input type="text" name="prod_name" placeholder="*product name*">
                            </p>
                            <p>
                                <label>Product description</label>
                                <textarea name="description" rows="2" style="height: 50px;"></textarea>
                            </p>
                            <p>
                                <label>Product quantity</label>
                                <input type="number" min="1" max="99999" name="prod_quantity"
                                       placeholder="*product quantity*">
                            </p>
                            <p>
                                <label>Product price per unit</label>
                                <input type="number" step="0.01" min="0.1" name="product_price"
                                       placeholder="*product price*">
                            </p>
                            <p>
                                <button type="submit"><fmt:message key="commodity.actions.createProduct"/></button>
                            </p>

                        </fieldset>
                    </form>
                </div>
            </div>
        </div>
    </div>

    <div class="card w-80 mx-auto my-8">
        <div class="card-header text-center"><fmt:message key="goods.warehouse.info.header"/></div>
        <div class="card-body" align="center">
            <table border="1" cellpadding="5" cellspacing="5">
                <tr>
                    <th>Item ID</th>
                    <th>Item name</th>
                    <th>Description</th>
                    <th>Units</th>
                    <th>Price per unit</th>
                    <th>Remaining quantity</th>
                    <th><c:if test="${edit}">Change remaining quantity</c:if></th>
                    <th><c:if test="${edit}">delete item</c:if></th>
                </tr>

                <c:forEach var="itemDTO" items="${itemDTOList}">
                    <tr>
                        <td>${itemDTO.productID}</td>
                        <td>${itemDTO.productName}</td>
                        <td>${itemDTO.productDescription}</td>
                        <td><my:unitTag unit="${itemDTO.productUnitId}"/></td>
                        <td>${itemDTO.productPrice}</td>
                        <td>${itemDTO.productQuantity}</td>
                        <c:if test="${edit}">
                            <td>
                                <form action="/fp/controller" method="post">
                                    <input type="hidden" name="command" value="changeStock">
                                    <input type="hidden" name="productId" value=${itemDTO.productID}>
                                    <input type="hidden" name="page" value="${page}">
                                    <input type="number" name="newStock"
                                           placeholder=${itemDTO.productQuantity}
                                                   min="1" max="9999">
                                    <button type="submit" class="btn btn-success" type="submit">SUBMIT</button>
                                </form>
                            </td>
                            <td>
                                <form action="/fp/controller" method="post">
                                    <input type="hidden" name="command" value="deleteItem">
                                    <button class="delete_btn" name="deleteItemId" value="${itemDTO.productID}"
                                            type="submit">X
                                    </button>
                                </form>
                            </td>
                        </c:if>
                    </tr>
                </c:forEach>
            </table>
            </form>
            <section>
                <div class="counter">${currentPage}</div>
                <div class="buttons">
                    <form action="/fp/controller" method="get">
                        <c:if test="${currentPage != 1}">
                            <td><a href="/fp/cabinet/commodity_expert_page?page=${currentPage - 1}">Previous</a></td>
                        </c:if>
                        <table border="1" cellpadding="5" cellspacing="5">
                            <tr>
                                <c:forEach begin="1" end="${noOfPages}" var="i">
                                    <c:choose>
                                        <c:when test="${currentPage eq i}">
                                            <td>${i}</td>
                                        </c:when>
                                        <c:otherwise>
                                            <td><a href="/fp/cabinet/commodity_expert_page?page=${i}">${i}</a></td>
                                        </c:otherwise>
                                    </c:choose>
                                </c:forEach>
                            </tr>
                        </table>
                        <c:if test="${currentPage lt noOfPages}">
                            <td><a href="/fp/cabinet/commodity_expert_page?page=${currentPage + 1}">Next</a></td>
                        </c:if>
                    </form>
                </div>
                <div class="edit_btns">
                    <form>
                        <button name="edit" value="true" dat><fmt:message
                                key="goods.edit.btn"/></button>
                        <c:if test="${edit}">
                            <button name="edit" value="false"><fmt:message
                                    key="goods.edit.exit.btn"/></button>
                        </c:if>
                    </form>
                </div>
            </section>
        </div>
    </div>
    <%@include file="includes/footer.jsp" %>
</body>
</html>