package controllers;

import dal.MotorbikeDAO;
import dal.OrderPartDAO;
import dal.OrderServiceDAO;
import dal.PartDAO;
import dal.RepairOrderDAO;
import dal.ServicesDAO;
import dal.DBContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import models.Motorbike;
import models.RepairOrder;
import models.User;

public class InvoiceController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        if (session.getAttribute("user") == null) {
            response.sendRedirect("Login");
            return;
        }

        try {
            int orderId = Integer.parseInt(request.getParameter("id"));

            RepairOrderDAO rDao = new RepairOrderDAO();
            MotorbikeDAO mDao = new MotorbikeDAO();
            OrderPartDAO opDao = new OrderPartDAO();
            PartDAO pDao = new PartDAO();
            ServicesDAO sDao = new ServicesDAO();
            OrderServiceDAO osDao = new OrderServiceDAO();

            RepairOrder order = rDao.GetOrderById(orderId);
            if (order == null) {
                response.sendRedirect("RepairOrder");
                return;
            }
            Motorbike moto = mDao.GetByMotoID(order.getMotoID());

            request.setAttribute("order", order);
            request.setAttribute("moto", moto);
            request.setAttribute("parts", opDao.GetByOrderID(orderId));
            request.setAttribute("partDao", pDao);
            request.setAttribute("services", osDao.getByOrderID(orderId));
            request.setAttribute("serviceDao", sDao);

            request.getRequestDispatcher("views/Invoice.jsp").forward(request, response);
        } catch (Exception e) {
            System.out.println("Invoice Error: " + e.getMessage());
            response.sendRedirect("RepairOrder");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String orderIdRaw = request.getParameter("orderId");
        String error = "";
        if (orderIdRaw == null || orderIdRaw.trim().isEmpty()) {
            error = "Order ID is required";
        } else {
            try {
                int orderId = Integer.parseInt(orderIdRaw);
                if (orderId <= 0) {
                    error = "Invalid order ID";
                }
            } catch (Exception e) {
                error = "Order ID must be number";
            }
        }

        if (!error.isEmpty()) {
            request.setAttribute("error", error);
            request.getRequestDispatcher("views/admin/Invoice.jsp").forward(request, response);
            return;
        }
        try {
            int orderId = Integer.parseInt(request.getParameter("orderId"));
            // Lấy tổng tiền từ JSP (đảm bảo input hidden ở JSP tên là "total")
            double totalAmount = Double.parseDouble(request.getParameter("total"));

            // 1. GỌI DAO ĐỂ LƯU VÀO BẢNG INVOICES (Hết bị lỗi protected nhé)
            dal.InvoiceDAO invDao = new dal.InvoiceDAO();
            invDao.AddInvoice(orderId, totalAmount);

            // 2. CẬP NHẬT TRẠNG THÁI ORDER THÀNH COMPLETED
            dal.RepairOrderDAO rDao = new dal.RepairOrderDAO();
            rDao.UpdateStatus(orderId, "COMPLETED");

        } catch (Exception e) {
            System.out.println("Payment Error: " + e.getMessage());
        }
        response.sendRedirect("RepairOrder");
    }
}
