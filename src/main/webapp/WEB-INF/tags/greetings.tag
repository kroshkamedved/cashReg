<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ tag pageEncoding="UTF-8" %>
<fmt:setLocale value="${loc}"/>
<fmt:setBundle basename="language"/>
<h1 style= text-align:center;margin:50px>
    <fmt:message key="common.info.cabinet.greetings"/>, ${usr.role} ${usr.login}
    <br>
    <hr>
</h1>