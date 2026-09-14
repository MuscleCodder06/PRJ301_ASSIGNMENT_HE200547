package controllers;

import dal.CustomerBookingDAO;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import models.User;

public class BookingListController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        if (user == null) {
            response.sendRedirect("Login");
            return;
        }

        CustomerBookingDAO dao = new CustomerBookingDAO();
        request.setAttribute("bookings", dao.getWaitingBookings());

        RequestDispatcher rd = request.getRequestDispatcher("views/BookingList.jsp");
        rd.forward(request, response);
    }
}
