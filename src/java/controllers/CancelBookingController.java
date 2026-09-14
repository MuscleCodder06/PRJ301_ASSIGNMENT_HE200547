package controllers;

import dal.CustomerBookingDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import models.User;

public class CancelBookingController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        // Yêu cầu phải đăng nhập
        if (user == null) {
            response.sendRedirect("Login");
            return;
        }

        String idRaw = request.getParameter("id");
        try {
            int bookingId = Integer.parseInt(idRaw);
            CustomerBookingDAO dao = new CustomerBookingDAO();
            
            boolean isCancelled = dao.cancelBooking(bookingId);
            
            if (isCancelled) {
                session.setAttribute("successMsg", "Booking #" + bookingId + " has been successfully cancelled.");
            } else {
                session.setAttribute("errorMsg", "Unable to cancel booking #" + bookingId + " (It may have already been confirmed or doesn't exist).");
            }
            
        } catch (Exception e) {
            session.setAttribute("errorMsg", "Invalid booking ID.");
        }
        
        // Trở lại trang danh sách sau khi hủy xong
        response.sendRedirect("BookingList");
    }
}