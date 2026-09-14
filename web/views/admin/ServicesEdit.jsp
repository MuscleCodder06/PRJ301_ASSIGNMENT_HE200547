<%-- 
    Document   : ServicesEdit
    Created on : Mar 10, 2026, 3:38:25 AM
    Author     : ADMIN
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="models.Service" %>
<%@page import="java.util.List" %>
<%@page import="java.util.ArrayList" %>
<html>
    <head>

        <title>Services Edit</title>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/styles.css">
    </head>
    <body>
        <jsp:include page="../header.jsp"/>
        <jsp:include page="../topnav.jsp"/>
        <h2>Edit Service</h2>
        <form action="ServicesEdit" method="POST">
            <%Service service = (Service)request.getAttribute("service");%>
            <%if (service != null) {%>
            <input type="hidden" name="id" value="<%= service.getServiceID()%>">
            <table>
                <tr>
                    <td>Service Name: </td>
                    <td><input type="text" name="servicename" value="<%= service.getServiceName()%>"></td>
                </tr>
                <tr>
                    <td>Price: </td>
                    <td><input type="text" name="price" value="<%= service.getPrice()%>"></td>
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
