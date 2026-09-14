package controllers;

import dal.CustomerBookingDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import models.User;

public class ConfirmBookingController extends HttpServlet {

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
            int bookingId = Integer.parseInt(idRaw);
            CustomerBookingDAO bookingDao = new CustomerBookingDAO();
            if (bookingDao.getById(bookingId) == null) {
                session.setAttribute("errorMsg", "Unable to confirm this booking.");
                response.sendRedirect("BookingList");
                return;
            }
            bookingDao.moveOutOfWaitingList(bookingId);
            response.sendRedirect("CreateOrder?bookingId=" + bookingId);
        } catch (Exception e) {
            session.setAttribute("errorMsg", "Invalid booking ID.");
            response.sendRedirect("BookingList");
        }
    }
}
