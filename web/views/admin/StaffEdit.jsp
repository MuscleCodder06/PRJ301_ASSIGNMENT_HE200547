<%-- 
    Document   : StaffEdit
    Created on : Mar 5, 2026, 11:26:20 PM
    Author     : ADMIN
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="models.User" %>
<%@page import="models.UserProfile" %>
<%@page import="models.Role" %>
<%@page import="java.util.List" %>
<%@page import="java.util.ArrayList" %>
<html>
    <head>

        <title>Staff Edit</title>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/styles.css">
    </head>
    <body>
        <jsp:include page="../header.jsp"/>
        <jsp:include page="../topnav.jsp"/>
        <h2>Edit Staff</h2>
        <form action="StaffEdit" method="POST">
            <%User user = (User)request.getAttribute("user");%>
            <%UserProfile usp = (UserProfile)request.getAttribute("userprf");%>
            <%if (user != null  && usp != null) {%>
            <table>
                <tr>
                    <td>Username:</td>
                    <td><input type="text" name="username" value="<%= user.getUsername() %>" readonly></td>
                </tr>
                <tr>
                    <td>Password:</td>
                    <td><input type="password" name="password" value="<%= user.getPassword() %>"/></td>
                </tr>
                <tr>
                    <td>Role:</td>
                    <td>
                        <% List<Role> roles = (ArrayList<Role>)request.getAttribute("roles"); %>
                        <select name="roleId">
                            <% for (Role role : roles) { %>
                            <% if(role.getRoleID() == user.getRole().getRoleID()) { %>
                            <option value="<%= role.getRoleID() %>" selected><%= role.getRoleName() %></option>
                            <% } else { %>
                            <option value="<%= role.getRoleID() %>"><%= role.getRoleName() %></option>
                            <% } %>
                            <% } %>
                        </select>
                    </td>
                </tr>
                <tr>
                    <td>FullName: </td>
                    <td><input type="text" name="fullname" value="<%= usp.getFullName() %>"></td>
                </tr>
                <tr>
                    <td>Emails: </td>
                    <td><input type="text" name="email" value="<%= usp.getEmail() %>"></td>
                </tr>
                <tr>
                    <td>Phone: </td>
                    <td><input type="text" name="phone" value="<%= usp.getPhone() %>"></td>
                </tr>
                <tr>
                    <td>Address: </td>
                    <td><input type="text" name="address" value="<%= usp.getAddress() %>"></td>
                </tr>
                <tr>
                    <td>Date of Birth: </td>
                    <td><input type="date" name="dob" value="<%= usp.getDateOfBirth() %>"></td>

                </tr>
                <tr>
                    <td>Gender: </td>
                    <td>
                        <select name= "gen">
                            <option value="Male">Male</option>
                            <option value="Female">Female</option>
                            <option value="Other">Other</option>
                        </select>    
                    </td>               
                </tr>
                <tr>
                    
                    <td style="text-align: center" colspan="2"><button type="submit">Save</button><td>
                </tr>
            </table>
            <%}%>
        </form>
        <jsp:include page="../footer.jsp"/>
    </body>
</html>
