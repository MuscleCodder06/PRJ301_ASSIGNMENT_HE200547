package controllers;

import dal.MotorbikeDAO;
import dal.OrderPartDAO;
import dal.OrderServiceDAO;
import dal.PartDAO;
import dal.RepairOrderDAO;
import dal.ServicesDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import models.Motorbike;
import models.OrderPart;
import models.OrderService;
import models.Part;
import models.RepairOrder;
import models.Service;
import models.User;

public class OrderDetailController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        if (user == null) {
            response.sendRedirect("Login");
            return;
        }

        String idRaw = request.getParameter("id");
        try {
            int orderId = Integer.parseInt(idRaw);
            loadOrderDetailData(request, orderId);
            if (request.getAttribute("order") == null) {
                response.sendRedirect("RepairOrder");
                return;
            }
            request.getRequestDispatcher("views/OrderDetail.jsp").forward(request, response);
        } catch (Exception e) {
            System.out.println("OrderDetail doGet error: " + e.getMessage());
            response.sendRedirect("RepairOrder");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        String orderIdRaw = request.getParameter("orderId");
        String partIdRaw = request.getParameter("partId");
        String qtyRaw = request.getParameter("quantity");
        String serviceIdRaw = request.getParameter("serviceId");

        int orderId = 0;
        int partId = 0;
        int qty = 0;
        String error = "";

        if (action == null || orderIdRaw == null) {
            error = "Invalid request.";
        } else {
            action = action.trim();

            if (orderIdRaw.trim().isEmpty()) {
                error = "Order ID is required.";
            } else {
                try {
                    orderId = Integer.parseInt(orderIdRaw);
                    if (orderId <= 0) {
                        error = "Invalid order ID.";
                    }
                } catch (Exception e) {
                    error = "Order ID must be a number.";
                }
            }

            if (error.isEmpty() && !action.equals("addPart") && !action.equals("addService")) {
                error = "Invalid action.";
            } else if (error.isEmpty() && action.equals("addPart")) {
                if (partIdRaw == null || partIdRaw.trim().isEmpty()) {
                    error = "Part ID is required.";
                } else {
                    try {
                        partId = Integer.parseInt(partIdRaw);
                        if (partId <= 0) {
                            error = "Invalid part ID.";
                        }
                    } catch (Exception e) {
                        error = "Part ID must be a number.";
                    }
                }

                if (error.isEmpty()) {
                    if (qtyRaw == null || qtyRaw.trim().isEmpty()) {
                        error = "Quantity is required.";
                    } else {
                        try {
                            qty = Integer.parseInt(qtyRaw);
                            if (qty <= 0 || qty > 1000) {
                                error = "Invalid quantity.";
                            }
                        } catch (Exception e) {
                            error = "Quantity must be a number.";
                        }
                    }
                }
            } else if (error.isEmpty() && action.equals("addService")) {
                if (serviceIdRaw == null || serviceIdRaw.trim().isEmpty()) {
                    error = "Service ID is required.";
                } else {
                    try {
                        int serviceId = Integer.parseInt(serviceIdRaw);
                        if (serviceId <= 0) {
                            error = "Invalid service ID.";
                        }
                    } catch (Exception e) {
                        error = "Service ID must be a number.";
                    }
                }
            }
        }

        if (!error.isEmpty()) {
            try {
                loadOrderDetailData(request, orderId);
            } catch (Exception ignored) {
            }
            request.setAttribute("error", error);
            request.getRequestDispatcher("views/OrderDetail.jsp").forward(request, response);
            return;
        }

        try {
            if ("addPart".equals(action)) {
                PartDAO partDAO = new PartDAO();
                OrderPartDAO orderPartDAO = new OrderPartDAO();

                if (partDAO.CheckStock(partId, qty)) {
                    partDAO.DeductStock(partId, qty);
                    orderPartDAO.Add(new OrderPart(0, orderId, partId, qty));
                } else {
                    request.getSession().setAttribute("errorMsg", "Not enough stock for the requested quantity.");
                }
            } else if ("addService".equals(action)) {
                int serviceId = Integer.parseInt(request.getParameter("serviceId"));
                OrderServiceDAO orderServiceDAO = new OrderServiceDAO();
                orderServiceDAO.Add(new OrderService(0, orderId, serviceId));
            }
        } catch (Exception e) {
            System.out.println("OrderDetail doPost error: " + e.getMessage());
        }

        response.sendRedirect("OrderDetail?id=" + orderId);
    }

    private void loadOrderDetailData(HttpServletRequest request, int orderId) {
        RepairOrderDAO repairOrderDAO = new RepairOrderDAO();
        MotorbikeDAO motorbikeDAO = new MotorbikeDAO();
        PartDAO partDAO = new PartDAO();
        OrderPartDAO orderPartDAO = new OrderPartDAO();
        ServicesDAO servicesDAO = new ServicesDAO();
        OrderServiceDAO orderServiceDAO = new OrderServiceDAO();

        RepairOrder order = repairOrderDAO.GetOrderById(orderId);
        if (order == null) {
            return;
        }

        Motorbike moto = motorbikeDAO.GetByMotoID(order.getMotoID());
        request.setAttribute("availableParts", partDAO.GetParts());
        request.setAttribute("addedParts", orderPartDAO.GetByOrderID(orderId));
        request.setAttribute("partDao", partDAO);
        request.setAttribute("availableServices", servicesDAO.GetServices());
        request.setAttribute("addedServices", orderServiceDAO.getByOrderID(orderId));
        request.setAttribute("serviceDao", servicesDAO);
        request.setAttribute("moto", moto);
        request.setAttribute("order", order);
    }
}
