<html>
<head>
    <title>Login page</title>
    <%@include file="includes/head.jsp" %>
</head>
<body>
<div class="container">
    <div class="card w-50 mx-auto my-5">
        <div class="card-header text-center">User Login</div>
        <div class="card-body">

            <form action="controller" method="post">
                <input name="command" value="login" type="hidden">

                <div class="form-group">
                    <label>Email Address</label>
                    <input type="text" class="form-control" name="login" placeholder="Enter Your Login" required>
                </div>
                <div class="form-group">
                    <label>Password</label>
                    <input type="password" class="form-control" name="password" placeholder="********" required>
                </div>

                <div class="text-center">
                    <button type="submit" class="btn btn-primary">Login</button>
                </div>


                <%@include file="includes/footer.jsp" %>

</body>

</html>
