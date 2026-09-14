<%-- 
    Document   : StaffCreate
    Created on : Mar 6, 2026, 4:02:23 AM
    Author     : ADMIN
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <title>Staff Create</title>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/styles.css">
    </head>
    <body>
        <jsp:include page="../header.jsp"/>
        <jsp:include page="../topnav.jsp"/>
        <h2>Add a New Staff</h2>
        <form action="StaffCreate" method="POST">
            <table>
                <tr>
                    <td>Username: </td>
                    <td><input type="text" name="username"></td>
                </tr>
                <tr>
                    <td>Password: </td>
                    <td><input type="password" name="password"></td>
                </tr>
                <tr>
                    <td>FullName: </td>
                    <td><input type="text" name="fullname"></td>
                </tr>
                <tr>
                    <td>Role: </td>
                    <td><select name="roleId">
                            <option value="1">ADMIN</option>
                            <option value="2">STAFF</option>
                        </select>
                    </td>
                </tr>
                <tr>
                    <td>Emails: </td>
                    <td><input type="text" name="email"></td>
                </tr>
                <tr>
                    <td>Phone: </td>
                    <td><input type="text" name="phone"></td>
                </tr>
                <tr>
                    <td>Address: </td>
                    <td><input type="text" name="address"></td>
                </tr>
                <tr>
                    <td>Date of Birth: </td>
                    <td><input type="date" name="dob"></td>
                </tr>
                <tr>
                    <td>Gender: </td>
                    <td><select name="gen">
                            <option value="Male">Male</option>
                            <option value="Female">Female</option>
                            <option value="Other">Other</option>
                        </select>
                    </td>
                </tr>
                <tr>
                    <td></td>
                    <td><button class="btnCreate">Create</button></td>
                </tr>
            </table>

        </form>
        <jsp:include page="../footer.jsp"/>
    </body>
</html>
