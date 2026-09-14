<%-- 
    Document   : ServicesCreate
    Created on : Mar 10, 2026, 3:22:25 AM
    Author     : ADMIN
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <title>Services Create</title>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/styles.css">
    </head>
    <body>
        <jsp:include page="../header.jsp"/>
        <jsp:include page="../topnav.jsp"/>
        <h2>Add a New Services</h2>
        <form action="ServicesCreate" method="POST">
            <table>
                <tr>
                    <td>Service Name: </td>
                    <td><input type="text" name="servicename"></td>
                </tr>
                <tr>
                    <td>Price: </td>
                    <td><input type="text" name="price"></td>
                </tr>
                
                <tr>
                    <td></td>
                    <td><button type="submit" class="btnCreate">Create</button></td>
                </tr>
            </table>

        </form>
        <jsp:include page="../footer.jsp"/>
    </body>
</html>
