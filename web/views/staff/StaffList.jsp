<%-- 
    Document   : StaffList
    Created on : Mar 5, 2026, 9:15:48 PM
    Author     : ADMIN
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="models.User" %>
<%@page import="java.util.List" %>
<%@page import="java.util.ArrayList" %>
<%@page import="models.Role" %>
<!DOCTYPE html>
<html>
    <head>

        <title>Staff List</title>
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
            
            <% String rId = request.getParameter("roleId"); %>
            <% String searchText = request.getParameter("searchText");%>
            <%if (searchText == null) searchText = "";%>
            <% int roleId = 0; %>
            <% if (rId != null) roleId = Integer.parseInt(rId); %>
            <div>

                <form action="Staff" method="POST">
                    <a href="StaffCreate"><button class="btnCreate" type="button">Add new Staff</button>></a>
                    <table>
                        <tr>
                            <td colspan="2">Search by name: <input type="text" name="searchText" value="<%= searchText%>">
                                | Role: 
                                <% List<Role> roles = (ArrayList<Role>)request.getAttribute("roles"); %>
                                <select name="roleId">
                                    <% for (Role r : roles) { %>
                                    <% pageContext.setAttribute("role", r); %>
                                    <% if(r.getRoleID() == roleId) { %>
                                    <option value="${pageScope.role.getRoleID()}" selected>${pageScope.role.getRoleName()}</option>
                                    <% } else { %>
                                    <option value="${pageScope.role.getRoleID()}">${pageScope.role.getRoleName()}</option>
                                    <% } %>
                                    <% } %>
                                </select>
                                <button type="submit">Search</button>
                            </td>

                        </tr>

                    </table>


                </form>

            </div>
            <h3>|List of Staffs</h3>
            <div class="table-container">
                <table class="data-table">
                    <tr>
                        <th>UserName</th>
                        <th>Password</th>
                        <th>FullName</th>
                        <th>RoleName</th>
                        <th>Actions</th>
                    </tr>
                    <%List<User> accounts = (ArrayList<User>)request.getAttribute("accounts");%>
                    <%for(User user : accounts){%>
                    <%pageContext.setAttribute("accounts", user);%>
                    <tr>
                        <td>${pageScope.accounts.getUsername()}</td>
                        <td>${pageScope.accounts.getPassword()}</td>
                        <td>${pageScope.accounts.getFullName()}</td>
                        <td>${pageScope.accounts.getRole().getRoleName()}</td>
                        <td>
                            <a href="UserProfiles?id=${pageScope.accounts.getUsername()}"><button class="btnDetails" type="button">Detail</button></a>
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

        if (action === "detail") {
            window.location = "UserProfiles?id=" + id;
        }

        if (action === "edit") {
            window.location = "StaffEdit?id=" + id;
        }

        if (action === "delete") {
            if (confirm("Are you sure to delete?")) {
                window.location = "StaffDelete?id=" + id;
            }
        }
    }
</script>
</body>
</html>
