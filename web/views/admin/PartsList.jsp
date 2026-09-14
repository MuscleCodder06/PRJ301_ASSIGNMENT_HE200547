<%-- 
    Document   : PartsList
    Created on : Mar 10, 2026, 1:11:06 AM
    Author     : ADMIN
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="models.Part" %>
<%@page import="java.util.List" %>
<%@page import="java.util.ArrayList" %>
<!DOCTYPE html>
<html>
    <head>
        <title>Parts List</title>
        <meta charset="UTF-8">
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/styles.css">
    </head>
    <body>
        <div>
            <jsp:include page="../header.jsp"/>
            <!-- Sidebar -->
            <jsp:include page="../topnav.jsp"/>
            
            <% String searchText = request.getParameter("searchText");%>
            <%if (searchText == null) searchText = "";%>
            
            <div>
                <form action="Parts" method="POST">
                    <a href="PartsCreate"><button class="btnCreate" type="button">Add new Parts</button></a>
                    <table>
                        <tr>
                            <td colspan="2">Search by name: <input type="text" name="searchText" value="<%= searchText%>">
                                <button type="submit">Search</button>
                            </td>
                        </tr>
                    </table>
                </form>

            </div>
            <h3>|List of Parts</h3>
            <div class="table-container">
                <table class="data-table">
                    <tr>
                        <th>ID</th>
                        <th>PartName</th>
                        <th>Price(VND)</th>
                        <th>Quantity</th>
                        <th>Description</th>
                        <th>Actions</th>
                    </tr>
                    <%List<Part> parts = (ArrayList<Part>)request.getAttribute("parts");%>
                    <%for(Part p : parts){%>
                    <%pageContext.setAttribute("p", p);%>
                    <tr>
                        <td>${pageScope.p.getPartID()}</td>
                        <td>${pageScope.p.getPartName()}</td>
                        <td>${pageScope.p.getPrice()}</td>
                        <td>${pageScope.p.getQuantity()}</td>
                        <td>${pageScope.p.getDescription()}</td>
                        <td>
                            <select onchange="handleAction(this)">
                                <option value="" selected>Action</option>
                                <option value="edit-${pageScope.p.getPartID()}">Edit</option>
                                <option value="delete-${pageScope.p.getPartID()}">Delete</option>
                            </select>
                        </td>

                    </tr>
                    <%}%>
                </table>
            </div>


        </div>
                <div class="errSearch">
            ${requestScope.errorSearch} 
        </div>
        <!-- Footer -->
        <jsp:include page="../footer.jsp"/>

    </div>


<script>
    function handleAction(select) {

        let value = select.value;

        if (value === "")
            return;

        let parts = value.split("-");
        let action = parts[0];
        let id = parts[1];

        if (action === "edit") {
            window.location = "PartsEdit?id=" + id;
        }

        if (action === "delete") {
            if (confirm("Are you sure to delete?")) {
                window.location = "PartsDelete?id=" + id;
            }
        }
    }
</script>
</body>
</html>
