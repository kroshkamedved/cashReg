<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
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
    <p>
        <a class="btn btn-primary" data-bs-toggle="collapse" href="#CollapseAdding" role="button"
           aria-expanded="false" aria-controls="CollapseAdding">Add new product</a>
        <a class="btn btn-primary" data-bs-toggle="collapse" href="#collapseAllgoods" role="button"
           aria-expanded="false" aria-controls="collapseAllgoods">Change stock quantities</a>
    </p>
    <div class="col">
        <div class="collapse multi-collapse" id="CollapseAdding">
            <div class="card w-50 mx-auto my-5">
                <div class="card-header text-center">add new product</div>
                <div class="card-body">

                    <form action="/fp/controller" method="post">
                        <input name="command" value="addProduct" type="hidden">
                        <fieldset style="display: table-column; width: 100%">
                            <legend>Fulfill the form to add the product</legend>
                            <p>
                                <label for="select">Product units</label>
                                <select name="unit_id" id="select">
                                    <c:forEach items="${units}" var="unit">
                                        <option value="${unit.id}">${unit.name}</option>
                                    </c:forEach>
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
                                <label>Product price per unit</label>
                                <input type="number" name="product_price" placeholder="*product price*">
                            </p>
                            <p>
                                <button type="submit">Add product</button>
                            </p>

                        </fieldset>
                    </form>
                </div>
            </div>
        </div>
    </div>
    <div class="col">
        <div class="collapse multi-collapse" id="collapseAllgoods">
            <div class="card w-80 mx-auto my-8">
                <div class="card-header text-center">stock</div>
                <div class="card-body" align="center">

                   <%-- <form action="/fp/controller" method="get">
                        <input name="command" value="changeStock" type="hidden">--%>
                        <table border="1" cellpadding="5" cellspacing="5">
                            <tr>
                                <th>Item ID</th>
                                <th>Item name</th>
                                <th>Description</th>
                                <th>Units id</th>
                                <th>Price per unit</th>
                                <th>Remaining quantity</th>
                                <th>Change remaining quantity</th>
                                <th>Submit</th>
                            </tr>

                            <c:forEach var="itemDTO" items="${itemDTOList}">
                                <tr>
                                    <td>${itemDTO.productID}</td>
                                    <td>${itemDTO.productName}</td>
                                    <td>${itemDTO.productDescription}</td>
                                    <td>${itemDTO.productUnitId}</td>
                                    <td>${itemDTO.productPrice}</td>
                                    <td>${itemDTO.productQuantity}</td>
                                    <td>
                                        <form action="/fp/controller" method="post">
                                            <input type="hidden" name="command" value="changeStock">
                                            <input type="hidden" name="productId" value=${itemDTO.productID}>
                                            <input type="text" name="newStock"
                                                   placeholder=${itemDTO.productQuantity} min="0"
                                                   max="${itemDTO.productQuantity}*10">
                                            <button type="submit" class="btn btn-success" type="submit">SUBMIT</button>
                                        </form>
                                    </td>
                                        <%--<td><input type="text" name="newStock"
                                                   value=${itemDTO.productQuantity}></td>
                                        <td>
                                            <button type="submit" class="btn btn-success">Submit</button>
                                        </td>--%>
                                </tr>
                            </c:forEach>
                        </table>
                    </form>
                </div>
            </div>
        </div>
    </div>
    <%@include file="../../includes/footer.jsp" %>
</body>
</html>