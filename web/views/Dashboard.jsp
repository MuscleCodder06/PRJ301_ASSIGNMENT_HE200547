<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="dal.CustomerDAO"%>
<%@page import="dal.RepairOrderDAO"%>
<%@page import="java.util.List"%>
<%@page import="models.CustomerRepairStat"%>
<%@page import="models.TechnicianMonthlyStat"%>
<%@page import="models.User"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE html>
<html>
    <head>
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Dashboard</title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/styles.css" />
        <style>
            .main-content {
                padding: 10px 0;
            }
            .page-title {
                font-weight: bold;
                font-size: 18px;
                margin-top: 20px;
                margin-bottom: 15px;
                color: #333;
                border-left: 4px solid #dc3545;
                padding-left: 10px;
            }
            .dashboard-grid {
                display: flex;
                gap: 20px;
                flex-wrap: wrap;
                align-items: stretch;
            }
            .dashboard-card-link {
                flex: 1;
                min-width: 200px;
                display: flex;
                color: inherit;
                text-decoration: none;
                padding: 0;
                margin: 0;
            }
            .stat-card {
                flex: 1;
                width: 100%;
                height: 100%;
                min-width: 200px;
                padding: 25px 15px;
                text-align: center;
                border-radius: 8px;
                box-shadow: 0 4px 6px rgba(0,0,0,0.1);
                transition: transform 0.2s;
                font-family: Arial, sans-serif;
                box-sizing: border-box;
            }
            .stat-card:hover {
                transform: translateY(-5px);
                box-shadow: 0 6px 12px rgba(0,0,0,0.15);
            }
            .stat-title {
                font-size: 18px;
                font-weight: bold;
                margin-bottom: 15px;
                text-transform: uppercase;
            }
            .stat-number {
                font-size: 40px;
                font-weight: bold;
                margin: 0;
            }
            .stat-revenue {
                font-size: 30px;
                font-weight: bold;
                margin: 0;
            }
            .bg-red {
                background-color: #dc3545;
                color: white;
            }
            .bg-blue {
                background-color: #0d6efd;
                color: white;
            }
            .bg-yellow {
                background-color: #ffc107;
                color: #000;
            }
            .bg-cyan {
                background-color: #0dcaf0;
                color: #000;
            }
            .bg-green {
                background-color: #198754;
                color: white;
            }
            .bg-day {
                background: linear-gradient(135deg, #17a2b8, #117a8b);
                color: white;
            }
            .bg-month {
                background: linear-gradient(135deg, #6f42c1, #563d7c);
                color: white;
            }
            .bg-year {
                background: linear-gradient(135deg, #dc3545, #c82333);
                color: white;
            }
            .bg-technician {
                background: linear-gradient(135deg, #fd7e14, #e8590c);
                color: white;
            }
            .stat-subtitle {
                margin-top: 10px;
                font-size: 14px;
                font-weight: 600;
                line-height: 1.4;
                opacity: 0.95;
            }
            .report-table-wrap {
                background: #fff;
                border-radius: 10px;
                box-shadow: 0 4px 12px rgba(0,0,0,0.08);
                overflow: hidden;
            }
            .report-table {
                width: 100%;
                border-collapse: collapse;
            }
            .report-table th,
            .report-table td {
                padding: 12px 16px;
                text-align: left;
                border-bottom: 1px solid #e9ecef;
            }
            .report-table th {
                background: #f8f9fa;
                font-size: 14px;
                text-transform: uppercase;
                color: #495057;
            }
            .report-table tr:last-child td {
                border-bottom: none;
            }
            .rank-badge {
                display: inline-block;
                min-width: 28px;
                padding: 4px 8px;
                border-radius: 999px;
                background: #dc3545;
                color: #fff;
                font-weight: 700;
                text-align: center;
            }
        </style>
    </head>

    <body>
        <div>
            <jsp:include page="header.jsp"/>
            <jsp:include page="topnav.jsp"/>

            <div class="main-content">
                <div class="page-title">Repair Order Overview</div>
                <div class="dashboard-grid">
                    <a href="BookingList" class="dashboard-card-link">
                        <div class="stat-card bg-red">
                            <div class="stat-title">Booking</div>
                            <div class="stat-number">${waitingBookings != null ? waitingBookings : '0'}</div>
                        </div>
                    </a>
                    <div class="dashboard-card-link">
                        <div class="stat-card bg-yellow">
                            <div class="stat-title">Pending</div>
                            <div class="stat-number">${pendingOrders != null ? pendingOrders : '0'}</div>
                        </div>
                    </div>
                    <div class="dashboard-card-link">
                        <div class="stat-card bg-cyan">
                            <div class="stat-title">Processing</div>
                            <div class="stat-number">${processingOrders != null ? processingOrders : '0'}</div>
                        </div>
                    </div>
                    <div class="dashboard-card-link">
                        <div class="stat-card bg-green">
                            <div class="stat-title">Completed</div>
                            <div class="stat-number">${paidOrders != null ? paidOrders : '0'}</div>
                        </div>
                    </div>
                    <div class="dashboard-card-link">
                        <div class="stat-card bg-blue">
                            <div class="stat-title">Total Orders</div>
                            <div class="stat-number">${totalOrders != null ? totalOrders : '0'}</div>
                        </div>
                    </div>
                </div>

                <%
                    User currentUser = (User) session.getAttribute("user");
                    if (currentUser != null && "admin".equalsIgnoreCase(currentUser.getRole().getRoleName())) {
                        TechnicianMonthlyStat topTechnician = (TechnicianMonthlyStat) request.getAttribute("topTechnician");
                        List<CustomerRepairStat> topCustomers = (List<CustomerRepairStat>) request.getAttribute("topCustomers");
                        if (topTechnician == null) {
                            topTechnician = new RepairOrderDAO().getTopTechnicianThisMonth();
                        }
                        if (topCustomers == null) {
                            topCustomers = new CustomerDAO().getTopCustomersByRepairOrders(5);
                        }
                %>

                <div class="page-title" style="margin-top: 40px;">Monthly Technician Performance</div>
                <div class="dashboard-grid">
                    <div class="dashboard-card-link">
                        <div class="stat-card bg-technician">
                            <div class="stat-title">Top Technician This Month</div>
                            <div class="stat-number"><%= topTechnician != null ? topTechnician.getCompletedOrders() : 0 %></div>
                            <div class="stat-subtitle"><%= topTechnician != null ? topTechnician.getFullName() : "No completed orders yet" %></div>
                        </div>
                    </div>
                </div>

                <div class="page-title" style="margin-top: 40px;">Financial Report (VND)</div>
                <div class="dashboard-grid">
                    <div class="dashboard-card-link">
                        <div class="stat-card bg-day">
                            <div class="stat-title">Today's Revenue</div>
                            <div class="stat-revenue">${revenueDay != null ? revenueDay : '0'}</div>
                        </div>
                    </div>
                    <div class="dashboard-card-link">
                        <div class="stat-card bg-month">
                            <div class="stat-title">This Month's Revenue</div>
                            <div class="stat-revenue">${revenueMonth != null ? revenueMonth : '0'}</div>
                        </div>
                    </div>
                    <div class="dashboard-card-link">
                        <div class="stat-card bg-year">
                            <div class="stat-title">This Year's Revenue</div>
                            <div class="stat-revenue">${revenueYear != null ? revenueYear : '0'}</div>
                        </div>
                    </div>
                </div>

                <div class="page-title" style="margin-top: 40px;">Top Customers by Repair Orders</div>
                <div class="report-table-wrap">
                    <table class="report-table">
                        <thead>
                            <tr>
                                <th>Rank</th>
                                <th>Customer</th>
                                <th>Phone</th>
                                <th>Total Repair Orders</th>
                            </tr>
                        </thead>
                        <tbody>
                            <%
                                if (topCustomers != null && !topCustomers.isEmpty()) {
                                    int rank = 1;
                                    for (CustomerRepairStat customerStat : topCustomers) {
                            %>
                            <tr>
                                <td><span class="rank-badge"><%= rank++ %></span></td>
                                <td><%= customerStat.getFullName() %></td>
                                <td><%= customerStat.getPhone() %></td>
                                <td><%= customerStat.getTotalOrders() %></td>
                            </tr>
                            <%
                                    }
                                } else {
                            %>
                            <tr>
                                <td colspan="4">No customer repair data yet.</td>
                            </tr>
                            <%
                                }
                            %>
                        </tbody>
                    </table>
                </div>

                <% } %>
            </div>

            <jsp:include page="footer.jsp"/>
        </div>
    </body>
</html>
