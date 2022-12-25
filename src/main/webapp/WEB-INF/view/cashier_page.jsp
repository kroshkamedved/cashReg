<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="my" uri="myTaglib" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="hi" tagdir="/WEB-INF/tags" %>
<html>
<head>
    <script type="text/javascript" src="${app}/js/index.js"></script>
    <title>Commodity expert page</title>
    <%@include file="includes/head.jsp" %>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <fmt:setLocale value="${loc}"/>
    <fmt:setBundle basename="language"/>
</head>
<body>
<%@include file="includes/cashier_navbar.jsp" %>
<input type="hidden" name="allGoods" value="${allGoods}">
<div class="container" >
    <hi:greetings/>
    <form autocomplete="off" action="/fp/controller" method="post">
        <input name="command" value="addProductToCart" type="hidden">
        <fieldset style="display: table-column; width: 100%">
            <legend><fmt:message key="cashier.actions.addProductToCart"/></legend>
            <p>
                <label><fmt:message key="common.info.cabinet.productName"/></label>
            <div class="autocomplete" style="width:300px;">
                <input type="text" name="prod_identifier" id="prod_identifier"
                       placeholder="*product name or id*">
            </div>
            </p>
            <p>
                <button type="submit" class="btn btn-light" onclick="return empty()">
                    <fmt:message key="cashier.actions.addProductToCartButton"/>
                </button>
            </p>
        </fieldset>
    </form>

    <script>
        function autocomplete(inp, arr) {
            /*the autocomplete function takes two arguments,
            the text field element and an array of possible autocompleted values:*/
            var currentFocus;
            /*execute a function when someone writes in the text field:*/
            inp.addEventListener("input", function (e) {
                var a, b, i, val = this.value;
                /*close any already open lists of autocompleted values*/
                closeAllLists();
                if (!val) {
                    return false;
                }
                currentFocus = -1;
                /*create a DIV element that will contain the items (values):*/
                a = document.createElement("DIV");
                a.setAttribute("id", this.id + "autocomplete-list");
                a.setAttribute("class", "autocomplete-items");
                /*append the DIV element as a child of the autocomplete container:*/
                this.parentNode.appendChild(a);
                /*for each item in the array...*/
                for (i = 0; i < arr.length; i++) {
                    /*check if the item starts with the same letters as the text field value:*/
                    if (arr[i].substr(0, val.length).toUpperCase() == val.toUpperCase()) {
                        /*create a DIV element for each matching element:*/
                        b = document.createElement("DIV");
                        /*make the matching letters bold:*/
                        b.innerHTML = "<strong>" + arr[i].substr(0, val.length) + "</strong>";
                        b.innerHTML += arr[i].substr(val.length);
                        /*insert a input field that will hold the current array item's value:*/
                        b.innerHTML += "<input type='hidden' value='" + arr[i] + "'>";
                        /*execute a function when someone clicks on the item value (DIV element):*/
                        b.addEventListener("click", function (e) {
                            /*insert the value for the autocomplete text field:*/
                            inp.value = this.getElementsByTagName("input")[0].value;
                            /*close the list of autocompleted values,
                            (or any other open lists of autocompleted values:*/
                            closeAllLists();
                        });
                        a.appendChild(b);
                    }
                }
            });
            /*execute a function presses a key on the keyboard:*/
            inp.addEventListener("keydown", function (e) {
                var x = document.getElementById(this.id + "autocomplete-list");
                if (x) x = x.getElementsByTagName("div");
                if (e.keyCode == 40) {
                    /*If the arrow DOWN key is pressed,
                    increase the currentFocus variable:*/
                    currentFocus++;
                    /*and and make the current item more visible:*/
                    addActive(x);
                } else if (e.keyCode == 38) { //up
                    /*If the arrow UP key is pressed,
                    decrease the currentFocus variable:*/
                    currentFocus--;
                    /*and and make the current item more visible:*/
                    addActive(x);
                } else if (e.keyCode == 13) {
                    /*If the ENTER key is pressed, prevent the form from being submitted,*/
                    e.preventDefault();
                    if (currentFocus > -1) {
                        /*and simulate a click on the "active" item:*/
                        if (x) x[currentFocus].click();
                    }
                }
            });

            function addActive(x) {
                /*a function to classify an item as "active":*/
                if (!x) return false;
                /*start by removing the "active" class on all items:*/
                removeActive(x);
                if (currentFocus >= x.length) currentFocus = 0;
                if (currentFocus < 0) currentFocus = (x.length - 1);
                /*add class "autocomplete-active":*/
                x[currentFocus].classList.add("autocomplete-active");
            }

            function removeActive(x) {
                /*a function to remove the "active" class from all autocomplete items:*/
                for (var i = 0; i < x.length; i++) {
                    x[i].classList.remove("autocomplete-active");
                }
            }

            function closeAllLists(elmnt) {
                /*close all autocomplete lists in the document,
                except the one passed as an argument:*/
                var x = document.getElementsByClassName("autocomplete-items");
                for (var i = 0; i < x.length; i++) {
                    if (elmnt != x[i] && elmnt != inp) {
                        x[i].parentNode.removeChild(x[i]);
                    }
                }
            }

            /*execute a function when someone clicks in the document:*/
            document.addEventListener("click", function (e) {
                closeAllLists(e.target);
            });
        }

        /*An array containing all the country names in the world:*/
        var countries = ${allGoods}    /*initiate the autocomplete function on the "myInput" element, and pass along the countries array as possible autocomplete values:*/
            autocomplete(document.getElementById("prod_identifier"), countries);
    </script>

    <div class="card w-80 mx-auto my-8">
        <c:if test="${fn:length(cart) > 0}">
        <div class="card-header text-center"><fmt:message key="cashier.cart.info.header"/></div>
        <div class="card-body" align="center">
            <form action="/fp/controller" method="post">
                <table class="table">
                    <thead class="thead-dark">
                    <tr>
                        <th scope="col"><fmt:message key="commodity.info.cabinet.table.itemId"/></th>
                        <th scope="col"><fmt:message key="commodity.info.cabinet.table.itemName"/></th>
                        <th scope="col"><fmt:message key="commodity.info.cabinet.table.description"/></th>
                        <th scope="col"><fmt:message key="commodity.info.cabinet.table.units"/></th>
                        <th scope="col"><fmt:message key="commodity.info.cabinet.table.pricePerUnit"/></th>
                        <th scope="col"><fmt:message key="common.info.cabinet.quantity"/></th>
                        <th scope="col"><fmt:message key="commodity.actions.deleteProductFromCart"/></th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach var="item" items="${cart.keySet()}">
                        <tr>
                            <td>${item.productID}</td>
                            <td>${item.productName}</td>
                            <td>${item.productDescription}</td>
                            <td><my:unitTag unit="${item.productUnitId}"/></td>
                            <td>${item.productPrice}</td>
                            <td>
                                <form action="/fp/controller" method="post">
                                    <input type="hidden" name="command" value="setItemQuantity">
                                    <select name="unit_quantity" id="select">
                                        <c:forEach begin="${cart.get(item)}" end="${item.productQuantity}"
                                                   var="k">
                                            <option value="${k}">${k}</option>
                                        </c:forEach>
                                    </select>
                                    <button type="submit" name="edit_goods_id" value="${item.productID}">OK
                                    </button>
                                </form>
                            </td>
                            <td>
                                <form action="/fp/cabinet/cashier_page" method="get">
                                    <input type="hidden" name="command" value="deleteItem">
                                    <button name="deleteItemId" value="${item.productID}"
                                            type="submit">X
                                    </button>
                                </form>
                            </td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
                <section>
                    <div class="buttons">

                        <c:if test="${cart.size() gt 0}">
                            <form action="/fp/cabinet/cashier_page" method="get">
                                <button type="submit" class="btn btn-danger" name="clear" value="true"/>
                            </form>
                            <form action="/fp/controller" method="post">
                                <input type="hidden" name="command" value="confirmCheck">
                                "<fmt:message key="cashier.cart.action.clean"/>"</button>
                                <button type="submit" class="btn btn-success" name="checkClosed" value="true">
                                    <fmt:message
                                            key="cashier.actions.confirmOrder"/></button>
                            </form>
                        </c:if>
                    </div>
                </section>
            </form>
            </form>
        </div>
    </div>
    </c:if>
    <%@include file="includes/footer.jsp" %>
</body>
</html>