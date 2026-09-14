<%-- 
    Document   : Header
    Created on : Mar 5, 2026, 12:55:16 PM
    Author     : ADMIN
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>

<div class="header">
    <div> <a style="margin: 0px auto" href="Login"><h2 style="color: black; margin: 3px auto">Motorbike Repair Shop</h2></a></div>
    
    <div> <h3 style="margin: 0px auto">Welcome: ${sessionScope.user.getFullName()}</h3> </div>
    
    <div>
        <a style="margin: 0px auto" href="Logout">
            <button class="btnLogout">Logout</button>
        </a>
    </div>
</div>