package controllers;

import dal.RepairOrderDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import models.User;

public class DeleteOrderController extends HttpServlet {

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
            RepairOrderDAO rDao = new RepairOrderDAO();
            
            // DAO sẽ lo việc kiểm tra và xóa cả OrderPart lẫn RepairOrder
            rDao.DeleteOrder(orderId);

        } catch (Exception e) {
            System.out.println("Delete Controller Error: " + e.getMessage());
        }
        
        // Xóa xong thì quay về trang danh sách
        response.sendRedirect("RepairOrder");
    }
}