<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html>
<head>
    <%@include file="includes/head.jsp" %>
    <jsp:useBean id="loc" scope="session" type="java.lang.String"/>
    <fmt:setLocale value="${loc}"/>
    <fmt:setBundle basename="language"/>
    <title>CHECK CONFIRMED</title>
</head>
<body>
<%@include file="includes/cashier_navbar.jsp" %>
<title>Check successfully confirmed</title>
<br>
<figure class="text-center">
    <blockquote class="blockquote">
        <p><fmt:message key="cashier.info.checkInfo"/></p>
    </blockquote>
    <figcaption class="blockquote-footer">
        <fmt:message key="cashier.info.orderCreated"/>${orderId}
    </figcaption>
</figure>
<%@include file="includes/footer.jsp" %>
</body>
</html>
