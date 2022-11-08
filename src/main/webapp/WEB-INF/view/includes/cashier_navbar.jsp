<conteiner>
    <nav class="navbar navbar-expand-lg bg-light">
        <div class="container">
            <a class="navbar-brand" href="${app}/cabinet/cashier_page">Cashier cabinet</a>
            <button class="navbar-toggler" type="button" data-bs-toggle="collapse"
                    data-bs-target="#navbarSupportedContent"
                    aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
                <span class="navbar-toggler-icon"></span>
            </button>
            <div class="collapse navbar-collapse" id="navbarSupportedContent">
                <ul class="navbar-nav ms-auto mb-2 mb-lg-0">
                    <ul>
                        <li class="nav-item dropdown">
                            <a class="nav-link dropdown-toggle" role="button" data-bs-toggle="dropdown"
                               aria-expanded="false">
                                <fmt:message key="common.info.language"/>
                            </a>
                            <ul class="dropdown-menu">
                                <c:url var="engLoc" value="/controller">
                                    <c:param name="command" value="changeLanguage"></c:param>
                                    <c:param name="loc" value="eng"></c:param>
                                </c:url>
                                <c:url var="uaLoc" value="/controller">
                                    <c:param name="command" value="changeLanguage"></c:param>
                                    <c:param name="loc" value="ua"></c:param>
                                </c:url>
                                <li><a class="dropdown-item" href="${uaLoc}">UA</a></li>
                                <li><a class="dropdown-item" href="${engLoc}">ENG</a></li>
                            </ul>
                        </li>
                    </ul>
                    <li class="nav-item">
                        <!--a class="nav-link active" aria-current="page" href="index.jsp">Home</a!-->
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" aria-current="page" href="${app}/cabinet/cashier_page/checks">Checks</a>
                    </li>

                    <li class="nav-item">
                        <a class="nav-link active" aria-current="page" href="/fp/logout"><fmt:message
                                key="common.info.cabinet.logout"/></a>
                    </li>
                    <!--li class="nav-item">
                        <a class="nav-link disabled">Disabled</a>
                    </li!-->
                </ul>
                <%--

                                <form class="d-flex" role="search">
                                    <input class="form-control me-2" type="search" placeholder="Search" aria-label="Search">
                                    <button class="btn btn-outline-success" type="submit">Search</button>
                                </form>
                --%>

            </div>
        </div>
    </nav>
</conteiner>