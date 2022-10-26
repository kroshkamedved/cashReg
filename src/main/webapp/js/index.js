function handleClick(clickedId) {
    if (clickedId == "eng")
        document.getElementById('locId').value = "eng";
    else
        document.getElementById('locId').value = "ua";
    $("form").submit();
}

/*   <script type="text/javascript" src="${pageContext.request.contextPath}/js/index.js"></script>
   <li>
       <a class="dropdown-item" onclick="handleClick(loc)" id="eng">ENG</a></li>
</li>
   <li>
       <a class="dropdown-item" onclick="handleClick(loc)" id="ua">UA</a></li>
</li>
   <input type="hidden" name=loc id="locId" />
}*/
