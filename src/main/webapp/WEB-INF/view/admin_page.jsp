<%@ page import="com.elearn.db.DBManager" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<html>
<head>
    <title>Admin page</title>
    <%@include file="../../includes/head.jsp" %>
</head>
<body>
<%@include file="../../includes/admin_navbar.jsp" %>
<h1 align="center">
    Hello, ${usr.login}
    <br>
    <% out.print(DBManager.getInstance().getConnection());%>
</h1>

<%@include file="../../includes/footer.jsp" %>
</body>
</html>