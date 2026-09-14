<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="models.RepairOrder"%>
<%@page import="models.Part"%>
<%@page import="models.OrderPart"%>
<%@page import="dal.PartDAO"%>
<%@page import="models.Service"%>
<%@page import="models.OrderService"%>
<%@page import="models.Motorbike"%>
<%@page import="dal.ServicesDAO"%>
<%@page import="java.util.List"%>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Order Detail</title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/styles.css" />
        <style>
            .detail-box { width: 80%; margin: 30px auto; padding: 30px; border: 1px solid #ddd; background: #fdfdfd; border-radius: 8px; box-shadow: 0 4px 8px rgba(0,0,0,0.05); }
            .info-grid { display: grid; grid-template-columns: 1fr 1fr; gap: 15px; margin-bottom: 20px; }
            .info-item { font-size: 16px; padding: 10px; background: #f8f9fa; border: 1px solid #eee; border-radius: 4px;}
            .info-item span { font-weight: bold; color: #555; }
            .status-badge { padding: 3px 10px; border-radius: 4px; font-weight: bold; color: white; font-size: 14px; }
            .status-PENDING { background-color: #ffc107; color: #000; }
            .status-PROCESSING { background-color: #0d6efd; }
            .status-COMPLETED { background-color: #198754; }
            .list-table { width: 100%; border-collapse: collapse; margin-top: 10px; margin-bottom: 30px;}
            .list-table th, .list-table td { border: 1px solid #ddd; padding: 10px; text-align: left; }
            .list-table th { background-color: #e9ecef; color: #333; }
            .action-form-container { display: flex; gap: 20px; margin-bottom: 20px; }
            .add-box { flex: 1; background: #eef2f5; padding: 15px; border-radius: 5px; border: 1px dashed #adb5bd; }
            .btn-action { background-color: #198754; color: white; padding: 8px 15px; border: none; border-radius: 4px; cursor: pointer; font-weight: bold; margin-left: 10px;}
            .btn-back { background-color: #6c757d; color: white; padding: 10px 20px; border: none; border-radius: 4px; cursor: pointer; font-weight: bold; }
        </style>
    </head>
    <body>
        <jsp:include page="header.jsp"/>
        <jsp:include page="topnav.jsp"/>

        <%
           String errorMsg = (String) session.getAttribute("errorMsg");
           if (errorMsg != null) {
        %>
            <script>alert('<%= errorMsg %>');</script>
        <%
               session.removeAttribute("errorMsg");
           }
        %>

        <%
            RepairOrder order = (RepairOrder) request.getAttribute("order");
            Motorbike moto = (Motorbike) request.getAttribute("moto");
            List<Part> availableParts = (List<Part>) request.getAttribute("availableParts");
            List<OrderPart> addedParts = (List<OrderPart>) request.getAttribute("addedParts");
            PartDAO partDao = (PartDAO) request.getAttribute("partDao");

            List<Service> availableServices = (List<Service>) request.getAttribute("availableServices");
            List<OrderService> addedServices = (List<OrderService>) request.getAttribute("addedServices");
            ServicesDAO serviceDao = (ServicesDAO) request.getAttribute("serviceDao");
        %>

        <div class="detail-box">
            <h2 style="text-align: center; color: #333; border-bottom: 2px solid #0d6efd; padding-bottom: 10px; margin-bottom: 25px;">
                Repair Order Details - #<%= order.getOrderID() %>
            </h2>

            <div class="info-grid">
                <div class="info-item"><span>Moto ID:</span> <b style="color:#dc3545; font-size:18px;"><%= order.getMotoID() %></b></div>
                <div class="info-item"><span>Status:</span> <span class="status-badge status-<%= order.getStatus() %>"><%= order.getStatus() %></span></div>
                <div class="info-item"><span>Customer:</span> <%= moto != null ? moto.getOwnerName() : "N/A" %></div>
                <div class="info-item"><span>Phone:</span> <%= moto != null ? moto.getPhone() : "N/A" %></div>
                <div class="info-item"><span>Brand:</span> <%= moto != null ? moto.getBrand() : "N/A" %></div>
                <div class="info-item"><span>Model:</span> <%= moto != null ? moto.getModel() : "N/A" %></div>
                <div class="info-item"><span>Technician:</span> <%= order.getTechnicianUsername() %></div>
                <div class="info-item"><span>Created By:</span> <%= order.getCreatedBy() %> (<%= order.getCreatedDate() %>)</div>
            </div>

            <% if ("PENDING".equals(order.getStatus()) || "PROCESSING".equals(order.getStatus())) { %>
            <div class="action-form-container">
                <div class="add-box">
                    <form action="OrderDetail" method="POST" style="display: flex; align-items: center;">
                        <input type="hidden" name="action" value="addPart">
                        <input type="hidden" name="orderId" value="<%= order.getOrderID() %>">
                        <select name="partId" required style="flex: 1; padding: 5px;">
                            <option value="">-- Select Part --</option>
                            <% if (availableParts != null) {
                                for (Part p : availableParts) { %>
                                    <option value="<%= p.getPartID() %>"><%= p.getPartName() %> (Stock: <%= p.getQuantity() %>)</option>
                            <%  } } %>
                        </select>
                        <input type="number" name="quantity" min="1" value="1" style="width: 50px; padding: 5px; margin-left: 10px;" required>
                        <button type="submit" class="btn-action">+ Part</button>
                    </form>
                </div>

                <div class="add-box">
                    <form action="OrderDetail" method="POST" style="display: flex; align-items: center;">
                        <input type="hidden" name="action" value="addService">
                        <input type="hidden" name="orderId" value="<%= order.getOrderID() %>">
                        <select name="serviceId" required style="flex: 1; padding: 5px;">
                            <option value="">-- Select Service --</option>
                            <% if (availableServices != null) {
                                for (Service s : availableServices) { %>
                                    <option value="<%= s.getServiceID() %>"><%= s.getServiceName() %></option>
                            <%  } } %>
                        </select>
                        <button type="submit" class="btn-action" style="background-color:#0d6efd">+ Service</button>
                    </form>
                </div>
            </div>
            <% } %>

            <h3>Used Parts</h3>
            <table class="list-table">
                <tr><th>No.</th><th>Part Name</th><th>Quantity</th></tr>
                <% if (addedParts != null && !addedParts.isEmpty()) { int stt = 1; for (OrderPart op : addedParts) { Part p = partDao.GetPartById(op.getPartID()); if (p != null) { %>
                <tr><td><%= stt++ %></td><td style="font-weight: bold; color: #198754;"><%= p.getPartName() %></td><td><%= op.getQuantity() %></td></tr>
                <% } } } else { %><tr><td colspan="3" style="text-align:center; color:gray;">No parts used yet.</td></tr><% } %>
            </table>

            <h3>Applied Services</h3>
            <table class="list-table">
                <tr><th>No.</th><th>Service Name</th></tr>
                <% if (addedServices != null && !addedServices.isEmpty()) { int stt2 = 1; for (OrderService os : addedServices) { Service s = serviceDao.GetServiceById(os.getServiceID()); if (s != null) { %>
                <tr><td><%= stt2++ %></td><td style="font-weight: bold; color: #0d6efd;"><%= s.getServiceName() %></td></tr>
                <% } } } else { %><tr><td colspan="2" style="text-align:center; color:gray;">No services added yet.</td></tr><% } %>
            </table>

            <div style="text-align: center; margin-top: 30px;">
                <button class="btn-back" onclick="window.location='RepairOrder'">Back to List</button>
            </div>
        </div>
        <jsp:include page="footer.jsp"/>
    </body>
</html>
