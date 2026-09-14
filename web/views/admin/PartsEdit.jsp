<%-- 
    Document   : PartsEdit
    Created on : Mar 10, 2026, 2:33:57 AM
    Author     : ADMIN
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="models.Part" %>
<%@page import="java.util.List" %>
<%@page import="java.util.ArrayList" %>
<html>
    <head>

        <title>Parts Edit</title>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/styles.css">
    </head>
    <body>
        <jsp:include page="../header.jsp"/>
        <jsp:include page="../topnav.jsp"/>
        <h2>Edit Parts</h2>
        <form action="PartsEdit" method="POST">
            <%Part part = (Part)request.getAttribute("part");%>
            <%if (part != null) {%>
            <input type="hidden" name="id" value="<%= part.getPartID()%>">
            <table>
                <tr>
                    <td>Part Name: </td>
                    <td><input type="text" name="partname" value="<%= part.getPartName()%>"></td>
                </tr>
                <tr>
                    <td>Price: </td>
                    <td><input type="text" name="price" value="<%= part.getPrice()%>"></td>
                </tr>
                <tr>
                    <td>Quantity: </td>
                    <td><input type="text" name="quantity" value="<%= part.getQuantity()%>"></td>
                </tr>
                <tr>
                    <td>Description: </td>
                    <td><input type="text" name="description" value="<%= part.getDescription()%>"></td>
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
