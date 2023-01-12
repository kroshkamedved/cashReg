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
    <title><fmt:message key="common.info.cabinet.checks"/></title>
</head>
<body>
<c:choose>
<c:when test="${usr.role eq UserRole.SENIOR_CASHIER}">
<%@include file="includes/admin_navbar.jsp" %>
<div class="container">
    <form action="/fp/cabinet/admin_page/checks" method="get">
        </c:when>
        <c:when test="${usr.role eq UserRole.CASHIER}">
            <%@include file="includes/cashier_navbar.jsp" %>
        <div style="text-align: center">
            TOTAL SOLD FOR TODAY: ${totalOrdersForToday}
        </div>
        <div class="container">
            <form action="/fp/cabinet/cashier_page/checks" method="get">
                </c:when>
                </c:choose>
                <select style="margin-top: 25px" name="recordsPerPage">
                    <option selected>${recordsPerPage}</option>
                    <c:forEach begin="1" end="${orders.size()}"
                               var="k">
                        <option value="${k}">${k}</option>
                    </c:forEach>
                </select>
                <button type="submit"><fmt:message key="common.info.cabinet.checks.recordsPerPage"/></button>
            </form>
            <c:if test="${usr.role eq UserRole.SENIOR_CASHIER}">
                <div style="margin-top: 25px">
                    <form action="/fp/cabinet/admin_page/checks" method="get">
                        <input type="date" name="checksForDate">
                        <button type="submit">
                            <fmt:message key="common.info.cabinet.checks.updateOrdersList"/>
                        </button>
                    </form>
                </div>
            </c:if>
            <c:forEach var="order" items="${orders}" begin="${(page-1)*recordsPerPage}"
                       end="${((page-1)*recordsPerPage)+recordsPerPage-1}">
                <table class="table">
                    <thead>
                    <tr>
                        <th>Order #${order.id}</th>
                        <c:if test="${usr.role eq UserRole.SENIOR_CASHIER}">
                            <th><fmt:message key="senior.cabinet.checks.action.deleteWholeOrder"/>${order.id}</th>
                        </c:if>
                    </tr>
                    </thead>
                    <tbody>
                    <tr>
                        <td>
                            <table class="table">
                                <thead>
                                <tr>
                                    <th scope="col"><fmt:message key="senior.cabinet.checks.orderId"/></th>
                                    <th scope="col"><fmt:message key="senior.cabinet.checks.cashierId"/></th>
                                    <th scope="col"><fmt:message key="commodity.info.cabinet.table.itemName"/></th>
                                    <th scope="col"><fmt:message key="commodity.info.cabinet.table.description"/></th>
                                    <th scope="col"><fmt:message key="commodity.info.cabinet.table.units"/></th>
                                    <th scope="col"><fmt:message key="commodity.info.cabinet.table.pricePerUnit"/></th>
                                    <th scope="col"><fmt:message key="common.info.cabinet.quantity"/></th>
                                </tr>
                                </thead>
                                <tbody>
                                <c:forEach var="item" items="${order.orderItems}">
                                <td>${order.id}</td>
                                <td>${order.cashierId}</td>
                                <td>${item.productName}</td>
                                <td>${item.productDescription}</td>
                                <td><my:unitTag unit="${item.productUnitId}"/></td>
                                <td>${item.productPrice}</td>
                                <td>${item.productQuantity}</td>

                                <c:if test="${usr.role eq UserRole.SENIOR_CASHIER}">
                                    <td>
                                        <form action="/fp/controller" method="post">
                                            <input type="hidden" name="command" value="deleteItemFromOrder">
                                            <input type="hidden" name="orderId" value="${order.id}">
                                            <input type="hidden" name="productQuantity" value="${item.productQuantity}">
                                            <button name="deleteItemId" value="${item.productID}"
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
            <div text-align : center>
                <nav aria-label="Page navigation">
                    <ul class="pagination justify-content-center">
                        <c:choose>
                            <c:when test="${page eq 1}">
                                <li class="page-item disabled"><a class="page-link"
                                                                  href="#">Previous</a>
                                </li>
                            </c:when>
                            <c:otherwise>
                                <li class="page-item"><a class="page-link"
                                                         href="/fp/cabinet/${rolePage}/checks?page=${page - 1}">Previous</a>
                                </li>
                            </c:otherwise>
                        </c:choose>
                        <c:forEach begin="1" end="${noOfPages}" var="i">
                            <c:choose>
                                <c:when test="${page eq i}">
                                    <li class="page-item active">
                                        <a class="page-link" href="#">${i} <span class="sr-only"></span></a>
                                    </li>
                                </c:when>
                                <c:otherwise>
                                    <li class="page-item">
                                        <a class="page-link" href="/fp/cabinet/${rolePage}/checks?page=${i}">${i}</a>
                                    </li>
                                </c:otherwise>
                            </c:choose>
                        </c:forEach>
                        <c:choose>
                            <c:when test="${page eq noOfPages}">
                                <li class="page-item disabled"><a class="page-link"
                                                                  href="#">Next</a>
                                </li>
                            </c:when>
                            <c:otherwise>
                                <li class="page-item"><a class="page-link"
                                                         href="/fp/cabinet/${rolePage}/checks?page=${page + 1}">Next</a>
                                </li>
                            </c:otherwise>
                        </c:choose>
                    </ul>
                </nav>
            </div>
            <%--   </c:if>--%>
        </div>
        <%@include file="includes/footer.jsp" %>
</body>
</html>
