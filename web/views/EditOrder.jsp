<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="models.RepairOrder"%>
<%@page import="models.User"%>
<%@page import="java.util.List"%>
<!DOCTYPE html>
<html>
    <head>
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Edit Order</title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/styles.css" />
    </head>
    <body>
        <jsp:include page="header.jsp"/>
        <jsp:include page="topnav.jsp"/>
        
        <h2>Update Repair Order</h2>
        
        <% RepairOrder order = (RepairOrder) request.getAttribute("order"); %>
        
        <div>
            <form action="EditOrder" method="post">
                <input type="hidden" name="orderId" value="<%= order.getOrderID() %>">
                <input type="hidden" name="motoId" value="<%= order.getMotoID() %>">
                
                <table>
                    <tr>
                        <td colspan="3">
                            Order ID: <input type="text" value="<%= order.getOrderID() %>" readonly style="background:#e9ecef; width: 60px;">
                            &nbsp;&nbsp;&nbsp; MotoID: <input type="text" value="<%= order.getMotoID() %>" readonly style="background:#e9ecef;">
                        </td>
                    </tr>
                    <tr>
                        <td>Created By:</td>
                        <td><input type="text" value="<%= order.getCreatedBy() %>" readonly style="background:#e9ecef;"></td>
                    </tr>
                    
                    <tr>
                        <td>Technician:</td>
                        <td>
                            <select name="tech">
                                <% 
                                   List<User> listUsers = (List<User>) request.getAttribute("users");
                                   if (listUsers != null) {
                                       for (User u : listUsers) {
                                           // Tự động chọn đúng tên anh thợ đang phụ trách
                                           String selected = u.getUsername().equals(order.getTechnicianUsername()) ? "selected" : "";
                                %>
                                        <option value="<%= u.getUsername() %>" <%= selected %>><%= u.getFullName() %></option>
                                <% 
                                       }
                                   } 
                                %>
                            </select>
                        </td>
                    </tr>
                    
                    <tr>
                        <td>Status:</td>
                        <td>
                            <select name="status" style="font-weight:bold; color: #0d6efd;">
                                <option value="PENDING" <%= "PENDING".equals(order.getStatus()) ? "selected" : "" %>>PENDING (Waiting)</option>
                                <option value="PROCESSING" <%= "PROCESSING".equals(order.getStatus()) ? "selected" : "" %>>PROCESSING (In Progress)</option>
                                <option value="COMPLETED" <%= "COMPLETED".equals(order.getStatus()) ? "selected" : "" %>>COMPLETED (Finished)</option>
                            </select>
                        </td>
                    </tr>

                    <tr>
                        <td>Description:</td>
                        <td><textarea name="des" style="width:300px; height:100px;"><%= order.getDescription() %></textarea></td>
                    </tr>
                    
                    <tr>
                        <td colspan="2" style="text-align: center">
                            <button class="btnCreate" type="submit" style="margin-top: 15px; background-color: #0d6efd;">Update Order</button> 
                        </td>
                    </tr>
                </table>
            </form>
        </div>
        
        <jsp:include page="footer.jsp"/>
    </body>
</html>
