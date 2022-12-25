<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="hi" tagdir="/WEB-INF/tags" %>

<html>
<head>
    <%@include file="includes/head.jsp" %>
    <fmt:setLocale value="${loc}"/>
    <fmt:setBundle basename="language"/>
    <title><fmt:message key="senior.cabinet.common.header"/></title>
</head>
<body>
<%@include file="includes/admin_navbar.jsp" %>
<hi:greetings/>
<%@include file="includes/footer.jsp" %>
</body>
</html>