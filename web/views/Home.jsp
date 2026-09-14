<%-- 
    Document   : AdminMain
    Created on : Mar 5, 2026, 1:35:24 AM
    Author     : ADMIN
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>

    <head>
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Home</title>
        <link rel="stylesheet" href="static/css/styles.css" />
    </head>

    <body>
        <jsp:include page="header_1.jsp"/>
        <jsp:include page="topnav.jsp"/>
        <div class="hero">
            <div class="hero-text">
                <h1>Repair Shop Management</h1>
                <h3>Welcome to the system</h3>
                <a href="CreateOrder"><button type="button" class="btnCreate"> <label>+ Create New Order</label></button></a>
                <a href="CustomerBooking"><button type="button" class="btnDetails"><label>Customer Booking</label></button></a>
            </div>
        </div>
        
        <jsp:include page="footer.jsp"/>
    </body>
</html>
