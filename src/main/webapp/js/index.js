function empty() {
    var x;
    x = document.getElementById("prod_identifier").value;
    var numeric_alpha = /^(?=.*[a-zA-Z])(?=.*[0-9])/;
    if (x == "" || numeric_alpha.test(x)) {
        alert("Enter a Valid Id or Name");
        return false;
    }
}

/*function handleClick(clickedId) {
    if (clickedId == "eng")
        document.getElementById('locId').value = "eng";
    else
        document.getElementById('locId').value = "ua";
    $("form").submit();
}*/

/*   <script type="text/javascript" src="${pageContext.request.contextPath}/js/index.js"></script>
   <li>
       <a class="dropdown-item" onclick="handleClick(loc)" id="eng">ENG</a></li>
</li>
   <li>
       <a class="dropdown-item" onclick="handleClick(loc)" id="ua">UA</a></li>
</li>
   <input type="hidden" name=loc id="locId" />
}*/
