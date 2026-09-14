<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@page import="models.RepairOrder"%>
<%@page import="models.Motorbike"%>
<%@page import="models.OrderPart"%>
<%@page import="dal.PartDAO"%>
<%@page import="models.Part"%>
<%@page import="models.OrderService"%>
<%@page import="dal.ServicesDAO"%>
<%@page import="dal.InvoiceDAO"%>
<%@page import="models.Service"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Date"%>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Invoice - #${requestScope.order.orderID}</title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/styles.css" />
        <style>
            body { background-color: #f4f6f9; }
            .invoice-box { max-width: 800px; margin: 40px auto; padding: 40px; background: #fff; box-shadow: 0 0 15px rgba(0, 0, 0, 0.1); font-family: 'Courier New', Courier, monospace; }
            .invoice-header { text-align: center; border-bottom: 2px dashed #333; padding-bottom: 20px; margin-bottom: 20px; }
            .shop-name { font-size: 28px; font-weight: bold; text-transform: uppercase; }
            .info-section { display: flex; justify-content: space-between; margin-bottom: 20px; line-height: 1.6; }
            .invoice-table { width: 100%; border-collapse: collapse; margin-bottom: 20px; }
            .invoice-table th, .invoice-table td { border-bottom: 1px solid #ddd; padding: 12px 5px; text-align: left; }
            .invoice-table th { font-weight: bold; border-bottom: 2px solid #333; }
            .invoice-table .right { text-align: right; }
            .total-section { text-align: right; border-top: 2px dashed #333; padding-top: 20px; font-size: 18px; }
            .total-amount { font-size: 24px; font-weight: bold; color: #dc3545; }
            .no-print { text-align: center; margin-top: 30px; display: flex; justify-content: center; gap: 15px; align-items: center; }
            .btn-print { background-color: #0d6efd; color: white; padding: 10px 25px; font-size: 16px; border: none; cursor: pointer; border-radius: 4px; font-weight: bold; }
            .btn-back { background-color: #6c757d; color: white; padding: 10px 25px; font-size: 16px; border: none; cursor: pointer; border-radius: 4px; font-weight: bold; }
            .btn-pay { background-color: #198754; color: white; padding: 10px 25px; font-size: 16px; border: none; cursor: pointer; border-radius: 4px; font-weight: bold; }
            @media print { body { background-color: #fff; } .no-print { display: none; } .invoice-box { box-shadow: none; margin: 0; padding: 0; } jsp\:include { display: none; } }
        </style>
    </head>
    <body>
        <div class="no-print"><jsp:include page="header.jsp"/></div>

        <% 
            RepairOrder order = (RepairOrder) request.getAttribute("order"); 
            Motorbike moto = (Motorbike) request.getAttribute("moto"); 
            List<OrderPart> parts = (List<OrderPart>) request.getAttribute("parts");
            PartDAO partDao = (PartDAO) request.getAttribute("partDao");
            List<OrderService> services = (List<OrderService>) request.getAttribute("services");
            ServicesDAO serviceDao = (ServicesDAO) request.getAttribute("serviceDao");
            double grandTotal = 0;
        %>

        <div class="invoice-box">
            <div class="invoice-header">
                <div class="shop-name">Motorbike Repair Shop</div>
                <div>Address: Hoa Lac Hi-Tech Park, Hanoi</div>
                <div>Hotline: 0123.456.789</div>
                <h2 style="margin-top: 20px;">PAYMENT INVOICE</h2>
                <div>Order ID: #<%= order.getOrderID() %> | Received date: <%= order.getCreatedDate() %></div>
                
                <% 
                   if ("COMPLETED".equals(order.getStatus())) { 
                       InvoiceDAO invDao = new InvoiceDAO();
                       Date payDate = invDao.GetPaymentDate(order.getOrderID());
                       if (payDate != null) {
                %>
                <div style="color: #198754; font-size: 16px; font-weight: bold; margin-top: 10px;">
                    ✅ Payment Date: <%= payDate %>
                </div>
                <%     } 
                   } 
                %>
            </div>

            <div class="info-section">
                <div>
                    <b>Customer:</b> <%= (moto != null) ? moto.getOwnerName() : "N/A" %><br>
                    <b>Phone:</b> <%= (moto != null) ? moto.getPhone() : "N/A" %><br>
                    <b>Moto ID:</b> <%= order.getMotoID() %> (<%= (moto != null) ? moto.getBrand() : "" %>)
                </div>
                <div style="text-align: right;">
                    <b>Technician:</b> <%= order.getTechnicianUsername() %><br>
                    <b>Status:</b> <span style="color:#0d6efd; font-weight:bold;"><%= order.getStatus() %></span>
                </div>
            </div>

            <h4>PART DETAILS:</h4>
            <table class="invoice-table">
                <tr><th>No.</th><th>Part Name</th><th class="right">Unit Price</th><th class="right">Qty</th><th class="right">Amount</th></tr>
                <% if (parts != null && !parts.isEmpty()) { int stt = 1; for (OrderPart op : parts) { Part p = partDao.GetPartById(op.getPartID()); if (p != null) { double thanhTien = p.getPrice() * op.getQuantity(); grandTotal += thanhTien; %>
                <tr><td><%= stt++ %></td><td><%= p.getPartName() %></td><td class="right"><%= p.getPrice() %></td><td class="right"><%= op.getQuantity() %></td><td class="right"><%= thanhTien %></td></tr>
                <% } } } else { %><tr><td colspan="5" style="text-align:center;">No parts added.</td></tr><% } %>
            </table>

            <h4>SERVICE DETAILS:</h4>
            <table class="invoice-table">
                <tr><th>No.</th><th>Service Name</th><th class="right">Service Fee</th></tr>
                <% if (services != null && !services.isEmpty()) { int stt2 = 1; for (OrderService os : services) { Service s = serviceDao.GetServiceById(os.getServiceID()); if (s != null) { double phiDichVu = s.getPrice(); grandTotal += phiDichVu; %>
                <tr><td><%= stt2++ %></td><td><%= s.getServiceName() %></td><td class="right"><%= phiDichVu %></td></tr>
                <% } } } else { %><tr><td colspan="3" style="text-align:center;">No services added.</td></tr><% } %>
            </table>
            
            <div class="total-section">
                <span>GRAND TOTAL: </span>
                <span class="total-amount" ><%= grandTotal %> VND</span>
            </div>
        </div>

        <div class="no-print">
            <button class="btn-print" onclick="window.print()">Print Invoice</button>

            <% if (!"COMPLETED".equals(order.getStatus())) { %>
            <form action="Invoice" method="POST" style="margin: 0;">
                <input type="hidden" name="orderId" value="<%= order.getOrderID() %>">
                <input type="hidden" name="total" value="<%= grandTotal %>">
                <button type="submit" class="btn-pay" onclick="return confirm('Has the customer paid <%= grandTotal %> VND?');">Confirm Payment</button>
            </form>
            <% } %>

            <button class="btn-back" onclick="window.location = 'RepairOrder'">Back</button>
        </div>
        <br><br>
        <div class="no-print"><jsp:include page="footer.jsp"/></div>
    </body>
</html>
