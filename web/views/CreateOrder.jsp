<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.List"%>
<%@page import="models.User"%>
<%@page import="models.Part"%>
<%@page import="models.Service"%>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Create Order</title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/styles.css" />
        <style>
            .old-form {
                margin: 20px;
                font-family: Arial, sans-serif;
                background: #fff;
                padding: 20px;
                border-radius: 5px;
                box-shadow: 0 0 10px rgba(0,0,0,0.1);
            }
            .old-form table {
                border-collapse: collapse;
                width: 70%;
                margin-bottom: 20px;
            }
            .old-form td {
                padding: 12px;
                border-bottom: 1px solid #ddd;
            }
            .old-form .label {
                font-weight: bold;
                width: 150px;
                color: #333;
            }
            .add-btn {
                background: #198754;
                color: white;
                border: none;
                padding: 6px 12px;
                cursor: pointer;
                border-radius: 4px;
                font-size: 13px;
                font-weight: bold;
            }
            .input-box {
                width: 90%;
                padding: 8px;
                border: 1px solid #ccc;
                border-radius: 4px;
            }
            .readonly-box {
                background-color: #e9ecef;
                border: 1px solid #ccc;
                padding: 8px;
                border-radius: 4px;
                color: #555;
                width: 90%;
                display: inline-block;
            }
        </style>
    </head>
    <body>
        <jsp:include page="header.jsp"/>
        <jsp:include page="topnav.jsp"/>

        <div class="old-form">
            <h2 style="color: #c82333; border-bottom: 2px solid #c82333; padding-bottom: 10px;">Create New Repair Order</h2>

            <%
                String errorMsg = (String) session.getAttribute("errorMsg");
                if (errorMsg != null) {
            %>
            <p style="color: red; font-weight: bold; background: #f8d7da; padding: 10px; border-radius: 5px;"><%= errorMsg %></p>
            <%
                    session.removeAttribute("errorMsg");
                }

                User currentUser = (User) session.getAttribute("user");
                String bookingId = request.getAttribute("bookingId") == null ? "" : String.valueOf(request.getAttribute("bookingId"));
                String ownerName = request.getAttribute("ownerName") == null ? "" : String.valueOf(request.getAttribute("ownerName"));
                String phone = request.getAttribute("phone") == null ? "" : String.valueOf(request.getAttribute("phone"));
                String motoId = request.getAttribute("motoId") == null ? "" : String.valueOf(request.getAttribute("motoId"));
                String brand = request.getAttribute("brand") == null ? "" : String.valueOf(request.getAttribute("brand"));
                String model = request.getAttribute("model") == null ? "" : String.valueOf(request.getAttribute("model"));
                String techValue = request.getAttribute("tech") == null ? "" : String.valueOf(request.getAttribute("tech"));
                String des = request.getAttribute("des") == null ? "" : String.valueOf(request.getAttribute("des"));
            %>

            <form action="CreateOrder" method="POST">
                <input type="hidden" name="bookingId" value="<%= bookingId %>">
                <table>
                    <tr>
                        <td class="label">Created By:</td>
                        <td>
                            <span class="readonly-box"><%= (currentUser != null) ? currentUser.getUsername() : "Unknown" %></span>
                            <input type="hidden" name="createdBy" value="<%= (currentUser != null) ? currentUser.getUsername() : "" %>">
                        </td>
                    </tr>
                    <tr>
                        <td class="label">Status:</td>
                        <td><span class="readonly-box" style="font-weight: bold; color: #ffc107; background-color: #fff3cd;">PENDING</span></td>
                    </tr>
                    <tr>
                        <td class="label">Moto ID:</td>
                        <td><input type="text" name="motoId" class="input-box" required value="<%= motoId %>" placeholder="Enter license plate"></td>
                    </tr>
                    <tr>
                        <td class="label">Customer Name:</td>
                        <td><input type="text" name="ownerName" class="input-box" required value="<%= ownerName %>" placeholder="Enter customer name"></td>
                    </tr>
                    <tr>
                        <td class="label">Phone:</td>
                        <td><input type="text" name="phone" class="input-box" required value="<%= phone %>" placeholder="Enter phone number"></td>
                    </tr>
                    <tr>
                        <td class="label">Brand:</td>
                        <td><input type="text" name="brand" class="input-box" value="<%= brand %>" placeholder="Enter brand"></td>
                    </tr>
                    <tr>
                        <td class="label">Model:</td>
                        <td><input type="text" name="model" class="input-box" value="<%= model %>" placeholder="Enter model"></td>
                    </tr>
                    <tr>
                        <td class="label">Technician:</td>
                        <td>
                            <select name="tech" class="input-box" required>
                                <%
                                    List<User> techs = (List<User>) request.getAttribute("technicians");
                                    if (techs != null) {
                                        for (User u : techs) {
                                            String selected = u.getUsername().equals(techValue) ? "selected" : "";
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
                        <td class="label">Description:</td>
                        <td><textarea name="des" class="input-box" required rows="4" placeholder="Describe the vehicle issue..."><%= des %></textarea></td>
                    </tr>
                </table>

                <hr style="border: 1px dashed #ccc; margin: 20px 0;">

                <h3 style="color:#198754;">Parts (Optional)</h3>
                <div id="parts-container">
                    <div class="part-row" style="margin-bottom: 10px;">
                        <select name="partId" style="padding: 6px; width: 300px;">
                            <option value="">-- Skip this item --</option>
                            <%
                                List<Part> parts = (List<Part>) request.getAttribute("availableParts");
                                if (parts != null) {
                                    for (Part p : parts) {
                            %>
                            <option value="<%= p.getPartID() %>"><%= p.getPartName() %> (Stock: <%= p.getQuantity() %>)</option>
                            <%
                                    }
                                }
                            %>
                        </select>
                        &nbsp; <b>SL:</b> <input type="number" name="quantity" min="1" value="1" style="width: 60px; padding: 6px;">
                    </div>
                </div>
                <button type="button" class="add-btn" onclick="addPartRow()">+ Add part row</button>

                <br><br>

                <h3 style="color:#0d6efd;">Services (Optional)</h3>
                <div id="services-container">
                    <div class="service-row" style="margin-bottom: 10px;">
                        <select name="serviceId" style="padding: 6px; width: 300px;">
                            <option value="">-- Skip this item --</option>
                            <%
                                List<Service> services = (List<Service>) request.getAttribute("availableServices");
                                if (services != null) {
                                    for (Service s : services) {
                            %>
                            <option value="<%= s.getServiceID() %>"><%= s.getServiceName() %></option>
                            <%
                                    }
                                }
                            %>
                        </select>
                    </div>
                </div>
                <button type="button" class="add-btn" style="background:#0d6efd;" onclick="addServiceRow()">+ Add service row</button>

                <br><br><br>
                <button type="submit" style="padding: 12px 30px; background-color: #c82333; color: white; border: none; font-weight: bold; border-radius: 4px; font-size: 16px; cursor: pointer;">Create Order Now</button>
                <a href="RepairOrder" style="margin-left: 15px; text-decoration: none; color: #6c757d; font-weight: bold;">Cancel</a>
            </form>
            <span style="color:red">
                <%= request.getAttribute("error") == null ? "" : request.getAttribute("error") %>
            </span>
        </div>

        <script>
            function addPartRow() {
                var container = document.getElementById("parts-container");
                var firstRow = container.getElementsByClassName("part-row")[0];
                var newRow = firstRow.cloneNode(true);
                newRow.querySelector('select').selectedIndex = 0;
                newRow.querySelector('input').value = 1;
                container.appendChild(newRow);
            }

            function addServiceRow() {
                var container = document.getElementById("services-container");
                var firstRow = container.getElementsByClassName("service-row")[0];
                var newRow = firstRow.cloneNode(true);
                newRow.querySelector('select').selectedIndex = 0;
                container.appendChild(newRow);
            }
        </script>

        <jsp:include page="footer.jsp"/>
    </body>
</html>
