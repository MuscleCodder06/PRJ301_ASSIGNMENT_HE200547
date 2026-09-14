<%-- 
    Document   : OrderList (STAFF VIEW)
    Created on : Mar 14, 2026
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@page import="models.RepairOrder" %>
<%@page import="java.util.List" %>
<%@page import="java.util.ArrayList" %>
<!DOCTYPE html>
<html>
    <head>
        <title>Repair Order List</title>
        <meta charset="UTF-8">
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/styles.css">
    </head>
    <body>
        <div>
            <jsp:include page="../header.jsp"/>
            <jsp:include page="../topnav.jsp"/>

            <% String searchText = request.getParameter("searchText");%>
            <%if (searchText == null) searchText = "";%>

            <div>
                <form action="RepairOrder" method="POST">
                    <a href="CreateOrder"><button class="btnCreate" type="button">Create new Order</button></a>
                    <table>
                        <tr>
                            <td colspan="2">Search by MotoID: <input type="text" name="searchText" value="<%= searchText%>">
                                <button type="submit">Search</button>
                            </td>
                        </tr>
                    </table>
                </form>
            </div>

            <h3>| List of Repair Orders (Staff)</h3>

            <div class="table-container">
                <table class="data-table">
                    <tr>
                        <th>Order ID</th>
                        <th>MotoID</th>
                        <th>Created By</th>
                        <th>Technician</th>
                        <th>Description</th>
                        <th>Received Date</th>
                        <th>Payment Date</th>
                        <th>Status</th>
                        <th>Actions</th>
                    </tr>

                    <% List<RepairOrder> orders = (List<RepairOrder>)request.getAttribute("orders"); %>
                    <% if(orders != null && !orders.isEmpty()) { %>
                    <% for(RepairOrder o : orders){ %>
                    <% pageContext.setAttribute("o", o); %>
                    <tr>
                        <td>${pageScope.o.getOrderID()}</td>
                        <td>${pageScope.o.getMotoID()}</td>
                        <td>${pageScope.o.getCreatedBy()}</td>
                        <td>${pageScope.o.getTechnicianUsername()}</td>
                        <td>${pageScope.o.getDescription()}</td>
                        <td>${pageScope.o.getCreatedDate()}</td>
                        <td>
                    <c:if test="${idao != null}">
                        <fmt:formatDate 
                            value="${idao.GetPaymentDate(pageScope.o.getOrderID())}" 
                            pattern="yyyy-MM-dd"/>
                    </c:if>
                    </td>
                    <td style="font-weight: bold;">${pageScope.o.getStatus()}</td>

                    <td>
                        <select onchange="handleAction(this)">
                            <option value="" selected>Action</option>

                            <% if ("PROCESSING".equals(o.getStatus()) || "COMPLETED".equals(o.getStatus())) { %>
                            <option value="detail-${pageScope.o.getOrderID()}">Details</option>
                            <% } %>
                            <%
                                if ( !"COMPLETED".equals(o.getStatus())){
                            %>
                            <option value="edit-${pageScope.o.getOrderID()}">Edit</option>
                            <%}%>
                            <option value="invoice-${pageScope.o.getOrderID()}">Invoice</option>
                        </select>
                    </td>
                    </tr>
                    <% } %>
                    <% } else { %>
                    <tr><td colspan="7" style="text-align: center; color: red;">No data available.</td></tr>
                    <% } %>
                </table>
            </div>
        </div>

        <div class="errSearch">
            ${requestScope.errorSearch} 
        </div>

        <jsp:include page="../footer.jsp"/>

    </div>

    <script>
        function handleAction(select) {

            let value = select.value;

            if (value === "")
                return;

            let parts = value.split("-");
            let action = parts[0];
            let id = parts[1];

            if (action === "detail") {
                window.location = "OrderDetail?id=" + id; // SỬA DÒNG NÀY (Đổi thành OrderDetail)
            }

            if (action === "edit") {
                window.location = "EditOrder?id=" + id; // SỬA DÒNG NÀY NÈ
            }

            if (action === "invoice") {
                window.location = "Invoice?id=" + id; // GỌI THẲNG SANG INVOICE CONTROLLER
            }

            // Trả select về mặc định sau khi chuyển trang
            select.value = "";
        }
    </script>
</body>
</html>
