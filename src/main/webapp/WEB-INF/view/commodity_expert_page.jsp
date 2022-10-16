<%@ page import="com.elearn.db.DBManager" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<html>
<head>
    <title>Commodity expert page</title>
    <%@include file="../../includes/head.jsp" %>
    <meta name="viewport" content="width=device-width, initial-scale=1">

</head>
<body>
<%@include file="../../includes/commodity_navbar.jsp" %>
<div class="container">

    <h1 align="center">
        Hello, ${usr.role} ${usr.login}
        <br>
    </h1>
    <div class="card w-50 mx-auto my-5">
        <div class="card-header text-center">add new product</div>
        <div class="card-body">

            <form action="/fp/controller" method="post">
                <input name="command" value="addProduct" type="hidden">
                <fieldset style="display: table-column;width: 100%">
                    <legend>Fulfill the form to add the product</legend>
                    <p>
                        <label for="select">Product units</label>
                        <select id="select">
                            <option value="">Choose...</option>
                            <option value="${category.id}">${category.name}</option>
                        </select>
                    </p>

                    <p>
                        <label>Product name</label>
                        <input type="text" name="prod_name" placeholder="*product name*">
                    </p>

                    <p>
                        <label>Product description</label>
                        <textarea name="description" rows="2" style="height: 50px;"></textarea>
                    </p>
                    <p>
                        <label>Product quantity</label>
                        <input type="number" name="prod_quantity" placeholder="*product quantity*">
                    </p>

                    <p>
                        <button type="submit">Add product</button>
                    </p>

                </fieldset>
            </form>
        </div>


    </div>
    <%@include file="../../includes/footer.jsp" %>
</body>
</html>