<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
    <head>
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Booking</title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/styles.css" />
    </head>
    <body>
        <h2>Customer Booking</h2>

        <%
            String error = (String) request.getAttribute("error");
            String successMessage = (String) request.getAttribute("successMessage");
            models.CustomerBooking booking = (models.CustomerBooking) request.getAttribute("booking");
        %>

        <% if (error != null && !error.isEmpty()) { %>
        <div class="errSearch"><%= error %></div>
        <% } %>

        <% if (successMessage != null && !successMessage.isEmpty()) { %>
        <div style="color: green; font-weight: bold; margin: 10px 0;"><%= successMessage %></div>
        <% } %>

        <form action="CustomerBooking" method="POST">
            <table>
                <tr>
                    <td>Full Name:</td>
                    <td><input type="text" name="fullName" value="<%= booking != null ? booking.getFullName() : "" %>" maxlength="100"></td>
                </tr>
                <tr>
                    <td>Phone:</td>
                    <td><input type="text" name="phone" value="<%= booking != null ? booking.getPhone() : "" %>" maxlength="10"></td>
                </tr>
                <tr>
                    <td>Problem:</td>
                    <td>
                        
                        <textarea name="problem" style="width:300px; height:100px;" placeholder="Customers can include the bike name here. The Moto ID will be added later" ><%= booking != null ? booking.getProblem() : " " %></textarea>
                        
                    </td>
                </tr>
                <tr>
                    <td style="text-align: center" colspan="2">
                        <button type="submit">Send Booking</button>
                    </td>
                    
                </tr>
            </table>
        </form>
    </body>
</html>
