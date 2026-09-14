<%-- 
    Document   : StaffDetail
    Created on : Mar 5, 2026, 10:33:55 PM
    Author     : ADMIN
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="models.UserProfile" %>
<!DOCTYPE html>
<html>
    <head>
        
        <title>Staff Detail</title>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/styles.css">
    </head>
    <body>
        <jsp:include page="../header.jsp"/>
        <jsp:include page="../topnav.jsp"/>
        <div>
            <h2>Personal information</h2>
            <% UserProfile usp = (UserProfile)request.getAttribute("userprofile"); %>
            <%if(usp != null){%>
            <table>
                <tr>
                    <td>Username: </td>
                    <td><%= usp.getUsername() %></td>
                </tr>
                <tr>
                    <td>FullName: </td>
                    <td><%= usp.getFullName() %></td>
                </tr>
                <tr>
                    <td>Emails: </td>
                    <td><%= usp.getEmail() %></td>
                </tr>
                <tr>
                    <td>Phone: </td>
                    <td><%= usp.getPhone() %></td>
                </tr>
                <tr>
                    <td>Address: </td>
                    <td><%= usp.getAddress() %></td>
                </tr>
                <tr>
                    <td>Date of Birth: </td>
                    <td><%= usp.getDateOfBirth() %></td>
                </tr>
                <tr>
                    <td>Gender: </td>
                    <td><%= usp.getGender() %></td>
                </tr>
            </table>
            
            <%}%>
        </div>
        
        <jsp:include page="../footer.jsp"/>
    </body>
</html>
