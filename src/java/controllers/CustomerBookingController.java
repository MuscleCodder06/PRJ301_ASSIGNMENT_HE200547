package controllers;

import dal.CustomerBookingDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import models.CustomerBooking;

public class CustomerBookingController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("views/CustomerBooking.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        String fullName = safeTrim(request.getParameter("fullName"));
        String phone = safeTrim(request.getParameter("phone"));
        String problem = safeTrim(request.getParameter("problem"));
        String motorbike = "Chua cap nhat";

        CustomerBooking booking = new CustomerBooking(fullName, phone, motorbike, problem);
        request.setAttribute("booking", booking);

        String error = validateBooking(booking);
        if (!error.isEmpty()) {
            request.setAttribute("error", error);
            request.getRequestDispatcher("views/CustomerBooking.jsp").forward(request, response);
            return;
        }

        boolean created = new CustomerBookingDAO().createBooking(booking);
        if (!created) {
            request.setAttribute("error", "The system could not save your request right now. Please try again.");
            request.getRequestDispatcher("views/CustomerBooking.jsp").forward(request, response);
            return;
        }

        request.setAttribute("successMessage", "Your booking request has been recorded. The shop will contact you soon.");
        request.setAttribute("booking", new CustomerBooking());
        request.getRequestDispatcher("views/CustomerBooking.jsp").forward(request, response);
    }

    private String safeTrim(String value) {
        return value == null ? "" : value.trim();
    }

    private String validateBooking(CustomerBooking booking) {
        if (booking.getFullName().isEmpty()) {
            return "Please enter the full name.";
        }
        if (booking.getFullName().length() > 100) {
            return "Full name must not exceed 100 characters.";
        }
        if (booking.getPhone().isEmpty()) {
            return "Please enter a phone number.";
        }
        if (!booking.getPhone().matches("0\\d{9}")) {
            return "Phone number must contain 10 digits and start with 0.";
        }
        if (booking.getProblem().isEmpty()) {
            return "Please describe the vehicle issue.";
        }
        if (booking.getProblem().length() > 255) {
            return "Issue description must not exceed 255 characters.";
        }
        return "";
    }
}
