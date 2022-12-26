<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="my" uri="myTaglib" %>
<%@page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="hi" tagdir="/WEB-INF/tags" %>

<html>
<head>
    <script type="text/javascript" src="${app}/js/index.js"></script>
    <title>Commodity expert page</title>
    <%@include file="includes/head.jsp" %>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <fmt:setLocale value="${loc}"/>
    <fmt:setBundle basename="language"/>
</head>
<body>
<%@include file="includes/commodity_navbar.jsp" %>
<div class="container">
    <hi:greetings/>
    <p>
        <a class="btn btn-primary" data-bs-toggle="collapse" href="#CollapseAdding" role="button"
           aria-expanded="false" aria-controls="CollapseAdding"><fmt:message key="commodity.actions.addProduct"/></a>
    </p>
    <div class="col">
        <div class="collapse multi-collapse" id="CollapseAdding">
            <div class="card w-50 mx-auto my-5">
                <div class="card-header text-center"><fmt:message key="commodity.actions.createProduct"/></div>
                <div class="card-body">
                    <form action="/fp/controller" method="post">
                        <input name="command" value="addProduct" type="hidden">
                        <fieldset>
                            <legend><fmt:message key="commodity.info.cabinet.addProductDescription"/></legend>
                            <div class="form-group" style="margin: 10px">
                                <label for="select"><fmt:message key="common.info.cabinet.units"/></label>
                                <select name="unit_id" id="select" class="form-control">
                                    <c:forEach items="${units}" var="unit">
                                        <option value="${unit.id}">${unit.name}</option>
                                    </c:forEach>
                                </select>
                            </div>
                            <div class="form-group" style="margin: 10px">
                                <label><fmt:message key="common.info.cabinet.productName"/></label>
                                <input type="text" class="form-control" id="prod_identifier" name="prod_name"
                                       placeholder="*0-9a-zA-Zа-яА-Я*" required>
                            </div>
                            <div class="form-group" style="margin: 10px">
                                <label>Product description</label>
                                <textarea class="form-control" name="description" rows="2" required></textarea>
                            </div>
                            <div class="form-group" style="margin: 10px">
                                <label>Product quantity</label>
                                <input type="number" class="form-control" min="1" max="9999999" name="prod_quantity"
                                       placeholder="*1-9999999*" required>
                            </div>
                            <div class="form-group" style="margin: 10px">
                                <label>Product price per unit</label>
                                <input type="number" class="form-control" step="0.01" min="0.1" name="product_price"
                                       placeholder="*product price*" required>
                            </div>
                            <div class="form-group" style="margin: 10px;text-align: center">
                                <button type="submit" class="btn btn-success" onclick="return empty()">
                                    <fmt:message key="commodity.actions.createProduct"/>
                                </button>
                            </div>
                        </fieldset>
                    </form>
                </div>
            </div>
        </div>
    </div>

    <div class="card w-80 mx-auto my-8">
        <div class="card-header text-center"
        "><fmt:message key="goods.warehouse.info.header"/></div>
    <table class="table">
        <thead class="thead-dark">
        <tr>
            <th scope="col"><fmt:message key="commodity.info.cabinet.table.itemId"/></th>
            <th scope="col"><fmt:message key="commodity.info.cabinet.table.itemName"/></th>
            <th scope="col"><fmt:message key="commodity.info.cabinet.table.description"/></th>
            <th scope="col"><fmt:message key="commodity.info.cabinet.table.units"/></th>
            <th scope="col"><fmt:message key="commodity.info.cabinet.table.pricePerUnit"/></th>
            <th scope="col"><fmt:message key="commodity.info.cabinet.table.quantityLeft"/></th>
            <th scope="col"><c:if test="${edit}"><fmt:message key="commodity.actions.editProduct"/></c:if></th>
            <th scope="col"><c:if test="${edit}"><fmt:message key="commodity.actions.deleteProduct"/></c:if></th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="item" items="${itemList}">
            <tr>
                <td>${item.productID}</td>
                <td>${item.productName}</td>
                <td>${item.productDescription}</td>
                <td><my:unitTag unit="${item.productUnitId}"/></td>
                <td>${item.productPrice}</td>
                <td>${item.productQuantity}</td>
                <c:if test="${edit}">
                    <td>
                        <form action="/fp/controller" method="post">
                            <input type="hidden" name="command" value="changeStock">
                            <input type="hidden" name="productId" value=${item.productID}>
                            <input type="hidden" name="page" value="${page}">
                            <input type="number" name="newStock"
                                   placeholder=${item.productQuantity}
                                           min="1" max="9999">
                            <button type="submit" class="btn btn-success">SUBMIT</button>
                        </form>
                    </td>
                    <td>
                        <form action="/fp/controller" method="post">
                            <input type="hidden" name="command" value="deleteItem">
                            <button class="delete_btn" name="deleteItemId" value="${item.productID}"
                                    type="submit">X
                            </button>
                        </form>
                    </td>
                </c:if>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</div>
<div style="padding: 25px 25px 25px 0px;">
    <form name="editForm" action="/fp/cabinet/commodity_expert_page?page=${currentPage}" method="get">
        <c:if test="${!edit}">
            <button type="submit" class="btn btn-secondary" name="edit" value="true" onclick="change()"><fmt:message
                    key="goods.edit.btn"/></button>
        </c:if>
        <c:if test="${edit}">
            <button type="submit" class="btn btn-success" name="edit" value="false" onclick="change()"><fmt:message
                    key="goods.edit.exit.btn"/></button>
        </c:if>
    </form>
</div>
<div text-align : center>
    <nav aria-label="Page navigation example">
        <ul class="pagination justify-content-center">
            <c:choose>
                <c:when test="${currentPage eq 1}">
                    <li class="page-item disabled"><a class="page-link"
                                                      href="/fp/cabinet/commodity_expert_page?page=${currentPage - 1}">Previous</a>
                    </li>
                </c:when>
                <c:otherwise>
                    <li class="page-item"><a class="page-link"
                                             href="/fp/cabinet/commodity_expert_page?page=${currentPage - 1}">Previous</a>
                    </li>
                </c:otherwise>
            </c:choose>
            <c:forEach begin="1" end="${noOfPages}" var="i">
                <c:choose>
                    <c:when test="${currentPage eq i}">
                        <li class="page-item active">
                            <a class="page-link" href="#">${i} <span class="sr-only"></span></a>
                        </li>
                    </c:when>
                    <c:otherwise>
                        <li class="page-item">
                            <a class="page-link" href="/fp/cabinet/commodity_expert_page?page=${i}">${i}</a>
                        </li>
                    </c:otherwise>
                </c:choose>
            </c:forEach>
            <c:choose>
                <c:when test="${currentPage eq noOfPages}">
                    <li class="page-item disabled"><a class="page-link"
                                                      href="/fp/cabinet/commodity_expert_page?page=${currentPage + 1}">Next</a>
                    </li>
                </c:when>
                <c:otherwise>
                    <li class="page-item"><a class="page-link"
                                             href="/fp/cabinet/commodity_expert_page?page=${currentPage + 1}">Next</a>
                    </li>
                </c:otherwise>
            </c:choose>
        </ul>
    </nav>
</div>
<%@include file="includes/footer.jsp" %>
</body>
</html>