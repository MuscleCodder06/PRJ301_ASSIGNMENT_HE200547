/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controllers;

import dal.UserDAO;
import jakarta.servlet.RequestDispatcher;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import models.User;

/**
 *
 * @author ADMIN
 */
public class LoginController extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet LoginController</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet LoginController at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        if (user == null) {
            RequestDispatcher rd = request.getRequestDispatcher("index.jsp");
            rd.forward(request, response);
        } else {
            RequestDispatcher rd = request.getRequestDispatcher("views/Home.jsp");
            request.setAttribute("user", user.getUsername());
            rd.forward(request, response);

        }
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Lay username va password tu view
        String username = request.getParameter("username");
        String password = request.getParameter("password");
// validatrion 
        String error = "";
        if (username == null || password == null) {
            error = "Invalid request";
        } else {
            username = username.trim();
            password = password.trim();
            if (username.isEmpty()) {
                error = "Username is required";
            } else if (username.length() > 20) {
                error = "Username must be <= 20 characters";
            } else if (!username.matches("^[a-zA-Z0-9_]+$")) {
                error = "Username only contains letters, numbers and _";
            } else if (password.isEmpty()) {
                error = "Password is required";
            } else if (password.length() > 255) {
                error = "Password too long";
            } else if (password.length() < 8) {
                error = "Password must be at least 8 characters";
            } else if (password.contains(" ")) {
                error = "Password cannot contain spaces";
            } else if (!password.matches(".*[A-Za-z].*")) {
                error = "Password must contain at least one letter";
            } else if (!password.matches(".*\\d.*")) {
                error = "Password must contain at least one number";
            }
        }

        if (!error.isEmpty()) {
            RequestDispatcher rd = request.getRequestDispatcher("index.jsp");
            request.setAttribute("username", username);
            request.setAttribute("error", error);
            rd.forward(request, response);
            return;
        }

        // Kiem tra username va password
        UserDAO dao = new UserDAO();
        User user = dao.GetUserByUsername(username);

        if (user != null
                && username.equals(user.getUsername())
                && password.equals(user.getPassword())) {

            HttpSession session = request.getSession();
            session.setAttribute("user", user);
            RequestDispatcher rd = request.getRequestDispatcher("views/Home.jsp");
            request.setAttribute("username", username);
            rd.forward(request, response);

        } else {
            RequestDispatcher rd = request.getRequestDispatcher("index.jsp");
            request.setAttribute("username", username);
            request.setAttribute("password", password);
            request.setAttribute("error", "Username and password are not valid.");
            rd.forward(request, response);
        }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
