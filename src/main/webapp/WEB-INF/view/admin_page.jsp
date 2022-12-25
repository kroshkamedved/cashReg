<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html; charset=UTF-8" %>

<html>
<head>
    <%@include file="includes/head.jsp" %>
    <jsp:useBean id="loc" scope="session" type="java.lang.String"/>
    <fmt:setLocale value="${loc}"/>
    <fmt:setBundle basename="language"/>
    <title><fmt:message key="senior.cabinet.common.header"/> </title>
</head>
<body>
<%@include file="includes/admin_navbar.jsp" %>
<h1 style= text-align:center;margin:100px>
    <fmt:message key="common.info.cabinet.greetings"/>, ${usr.role} ${usr.login}
    <br>

</h1>

<%@include file="includes/footer.jsp" %>
</body>
</html>