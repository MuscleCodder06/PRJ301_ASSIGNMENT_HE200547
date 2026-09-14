<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.List"%>
<%@page import="models.CustomerBooking"%>
<!DOCTYPE html>
<html>
    <head>
        <title>Booking List</title>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/styles.css">
        <style>
            .btnCancel {
                background-color: #dc3545;
                color: white;
                padding: 6px 12px;
                text-decoration: none;
                border-radius: 4px;
                font-weight: bold;
                border: none;
                cursor: pointer;
                margin-left: 5px;
            }
            .btnCancel:hover {
                background-color: #c82333;
            }
            .action-buttons {
                display: flex;
                gap: 5px;
                justify-content: center;
            }
        </style>
    </head>
    <body>
        <jsp:include page="header.jsp"/>
        <jsp:include page="topnav.jsp"/>

        <div style="width: 95%; margin: 20px auto;">
            <h2 style="border-left: 4px solid #0d6efd; padding-left: 10px; color: #333;">Customer Booking List</h2>

            <%
                String successMsg = (String) session.getAttribute("successMsg");
                String errorMsg = (String) session.getAttribute("errorMsg");
                if (successMsg != null) {
            %>
            <div style="color: #198754; font-weight: bold; background: #d1e7dd; padding: 10px; border-radius: 5px; margin-bottom: 15px;">
                ✅ <%= successMsg %>
            </div>
            <%
                    session.removeAttribute("successMsg");
                }
                if (errorMsg != null) {
            %>
            <div style="color: #dc3545; font-weight: bold; background: #f8d7da; padding: 10px; border-radius: 5px; margin-bottom: 15px;">
                ⚠️ <%= errorMsg %>
            </div>
            <%
                    session.removeAttribute("errorMsg");
                }
            %>

            <div class="table-container" style="background: #fff; padding: 20px; border-radius: 8px; box-shadow: 0 0 10px rgba(0,0,0,0.1);">
                <table class="data-table" style="width: 100%; border-collapse: collapse;">
                    <thead>
                        <tr style="background-color: #0d6efd; color: white;">
                            <th style="padding: 12px; text-align: left;">Booking ID</th>
                            <th style="padding: 12px; text-align: left;">Full Name</th>
                            <th style="padding: 12px; text-align: left;">Phone</th>
                            <th style="padding: 12px; text-align: left;">Problem</th>
                            <th style="padding: 12px; text-align: left;">Booking Date</th>
                            <th style="padding: 12px; text-align: center;">Action</th>
                        </tr>
                    </thead>
                    <tbody>
                        <% List<CustomerBooking> bookings = (List<CustomerBooking>) request.getAttribute("bookings"); %>
                        <% if (bookings != null && !bookings.isEmpty()) { %>
                        <% for (CustomerBooking booking : bookings) { %>
                        <tr style="border-bottom: 1px solid #ddd;">
                            <td style="padding: 12px;"><%= booking.getBookingID() %></td>
                            <td style="padding: 12px; font-weight: bold;"><%= booking.getFullName() %></td>
                            <td style="padding: 12px;"><%= booking.getPhone() %></td>
                            <td style="padding: 12px;"><%= booking.getProblem() %></td>
                            <td style="padding: 12px;"><%= booking.getCreatedDate() %></td>
                            <td class="action-buttons" style="padding: 12px;">
                                <a href="ConfirmBooking?id=<%= booking.getBookingID() %>" class="btnCreate" style="background-color: #198754; color: white; padding: 6px 12px; text-decoration: none; border-radius: 4px; font-weight: bold;" onclick="return confirm('Are you sure you want to Confirm and create a Repair Order for this booking?');">Confirm</a>
                                <a href="CancelBooking?id=<%= booking.getBookingID() %>" class="btnCancel" onclick="return confirm('⚠️ WARNING: Are you sure you want to CANCEL this booking?');">Cancel</a>
                            </td>
                        </tr>
                        <% } %>
                        <% } else { %>
                        <tr>
                            <td colspan="6" style="text-align: center; color: red; padding: 20px; font-weight: bold;">There are no pending bookings.</td>
                        </tr>
                        <% } %>
                    </tbody>
                </table>
            </div>
        </div>
        <jsp:include page="footer.jsp"/>
    </body>
</html>