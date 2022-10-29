<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <%@include file="includes/head.jsp" %>
    <jsp:useBean id="loc" scope="session" type="java.lang.String"/>
    <fmt:setLocale value="${loc}"/>
    <fmt:setBundle basename="language"/>
</head>
<body>
<%@include file="includes/commodity_navbar.jsp" %>
<title>Product successfully added</title>
<br>
<figure class="text-center">
    <blockquote class="blockquote">
        <p>Product "${product.productName}" successfully added to warehouse</p>
    </blockquote>
    <figcaption class="blockquote-footer">
        <fmt:message key="common.info.cabinet.productName"/> - ${product.productName}
        <br>
        <fmt:message key="common.info.cabinet.productDescription"/> - ${product.productDescription}
        <br>
        <fmt:message key="common.info.cabinet.quantity"/> - ${product.productQuantity}
    </figcaption>
</figure>
<%@include file="includes/footer.jsp" %>


</body>

</html>
