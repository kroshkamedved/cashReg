<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.elearn.fp.db.entity.UserRole" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="my" uri="myTaglib" %>
<%@ taglib prefix="C" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <script type="text/javascript" src="${app}/js/index.js"></script>
    <%@include file="includes/head.jsp" %>
    <fmt:setLocale value="${loc}"/>
    <fmt:setBundle basename="language"/>
    <title>TEMP CHECKS TITLE</title>
</head>
<body>
<c:choose>
    <c:when test="${usr.role eq UserRole.SENIOR_CASHIER}">
        <%@include file="includes/admin_navbar.jsp" %>

    </c:when>
    <c:when test="${usr.role eq UserRole.CASHIER}">
        <%@include file="includes/cashier_navbar.jsp" %>
    </c:when>
</c:choose>

<div class="container">
    <form action="/fp/cabinet/admin_page/checks" method="get">
        <select name="recordsPerPage">
            <option selected>${recordsPerPage}</option>
            <c:forEach begin="1" end="${orders.size()}"
                       var="k">
                <option value="${k}">${k}</option>
            </c:forEach>
        </select>
        <button type="submit">RecordsPerPage</button>
    </form>
    <c:if test="${usr.role eq UserRole.SENIOR_CASHIER}">
        <form action="/fp/cabinet/admin_page/checks" method="get">
            <input type="date" name="checksForDate">
            <button type="submit">get orders</button>
        </form>
    </c:if>
    <c:forEach var="order" items="${orders}" begin="${(page-1)*recordsPerPage}"
               end="${((page-1)*recordsPerPage)+recordsPerPage-1}">
        <table class="table">
            <thead>
            <tr>
                <th>Order #${order.id}</th>
                <c:if test="${usr.role eq UserRole.SENIOR_CASHIER}">
                    <th>delete order ${order.id}</th>
                </c:if>
            </tr>
            </thead>
            <tbody>
            <tr>
                <td>
                    <table class="table">
                        <thead>
                        <tr>
                            <th>Order ID</th>
                            <th>Cashier ID</th>
                            <th>Product name</th>
                            <th>Description</th>
                            <th>Units id</th>
                            <th>Price per unit</th>
                            <th><fmt:message key="common.info.cabinet.quantity"/></th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:forEach var="itemDTO" items="${order.orderItems}">
                        <td>${order.id}</td>
                        <td>${order.cashierId}</td>
                        <td>${itemDTO.productName}</td>
                        <td>${itemDTO.productDescription}</td>
                        <td><my:unitTag unit="${itemDTO.productUnitId}"/></td>
                        <td>${itemDTO.productPrice}</td>
                        <td>${itemDTO.productQuantity}</td>

                        <c:if test="${usr.role eq UserRole.SENIOR_CASHIER}">
                            <td>
                                <form action="/fp/controller" method="post">
                                    <input type="hidden" name="command" value="deleteItemFromOrder">
                                    <input type="hidden" name="orderId" value="${order.id}">
                                    <input type="hidden" name="productQuantity" value="${itemDTO.productQuantity}">
                                    <button name="deleteItemId" value="${itemDTO.productID}"
                                            type="submit">X
                                    </button>
                                </form>
                            </td>
                        </c:if>
                        </tbody>
                        </c:forEach>
                    </table>
                </td>
                <c:if test="${usr.role eq UserRole.SENIOR_CASHIER}">
                    <td>
                        <form action="/fp/controller" method="post">
                            <input type="hidden" name="command" value="deleteWholeOrder">
                            <button name="deleteOrderId" value="${order.id}" class="btn-close"
                                    aria-label="Close"></button>
                        </form>
                    </td>
                </c:if>
            </tr>
            </tbody>
        </table>
    </c:forEach>
    <section>
        <%--  <c:if test="${usr.role eq UserRole.SENIOR_CASHIER}">--%>
        <div class="counter">
            <h5>${page}</h5>
            <div class="buttons">
                <form action="/fp/controller" method="get">
                    <c:if test="${page > 1}">
                        <td>
                            <a href="/fp/cabinet/admin_page/checks?page=${page-1}">Previous</a>
                        </td>
                    </c:if>
                    <table>
                        <tr>
                            <c:forEach var="i" begin="1" end="${noOfPages}">
                                <c:choose>
                                    <c:when test="${page eq i}">
                                        <td>${i}</td>
                                    </c:when>
                                    <c:otherwise>
                                        <td>
                                            <a href="/fp/cabinet/admin_page/checks?page=${i}">${i}</a>
                                        </td>
                                    </c:otherwise>
                                </c:choose>
                            </c:forEach>
                        </tr>
                    </table>
                    <c:if test="${page lt noOfPages}">
                        <td>
                            <a href="/fp/cabinet/admin_page/checks?page=${page + 1}">Next</a>
                        </td>
                    </c:if>
                </form>
            </div>
        </div>
    </section>
    <%--   </c:if>--%>
</div>
<%@include file="includes/footer.jsp" %>
</body>
</html>
