/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controllers;

import dal.CustomerBookingDAO;
import dal.CustomerDAO;
import dal.RepairOrderDAO;
import jakarta.servlet.RequestDispatcher;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import models.TechnicianMonthlyStat;
import models.User;

public class DashboardController extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet AdminDashboardController</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet AdminDashboardController at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        if (user == null) {
            response.sendRedirect("Login");
            return;
        }

        RepairOrderDAO orderDao = new RepairOrderDAO();

        int totalOrders = orderDao.countTotalOrders();
        int pendingOrders = orderDao.countOrderByStatus("PENDING");
        int processingOrders = orderDao.countOrderByStatus("PROCESSING");
        int paidOrders = orderDao.countOrderByStatus("COMPLETED");
        int waitingBookings = new CustomerBookingDAO().countWaitingBookings();

        request.setAttribute("totalOrders", totalOrders);
        request.setAttribute("pendingOrders", pendingOrders);
        request.setAttribute("processingOrders", processingOrders);
        request.setAttribute("paidOrders", paidOrders);
        request.setAttribute("waitingBookings", waitingBookings);

        if ("admin".equalsIgnoreCase(user.getRole().getRoleName())) {
            double revenueDay = orderDao.getRevenue("DAY");
            double revenueMonth = orderDao.getRevenue("MONTH");
            double revenueYear = orderDao.getRevenue("YEAR");
            TechnicianMonthlyStat topTechnician = orderDao.getTopTechnicianThisMonth();
            CustomerDAO customerDAO = new CustomerDAO();

            request.setAttribute("revenueDay", revenueDay);
            request.setAttribute("revenueMonth", revenueMonth);
            request.setAttribute("revenueYear", revenueYear);
            request.setAttribute("topTechnician", topTechnician);
            request.setAttribute("topCustomers", customerDAO.getTopCustomersByRepairOrders(5));
        }

        RequestDispatcher rd = request.getRequestDispatcher("views/Dashboard.jsp");
        rd.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }
}
