/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controllers;

import dal.ServicesDAO;
import jakarta.servlet.RequestDispatcher;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import models.Service;
import models.User;

/**
 *
 * @author ADMIN
 */
public class ServicesEditController extends HttpServlet {

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
            out.println("<title>Servlet ServicesEditController</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet ServicesEditController at " + request.getContextPath() + "</h1>");
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
            response.sendRedirect("Login");
        } else {
            if (!user.getRole().getRoleName().equalsIgnoreCase("admin")) {
                response.sendRedirect("Services");
                return;
            }
            int id = Integer.parseInt(request.getParameter("id"));

            ServicesDAO dao = new ServicesDAO();
            Service s = dao.GetServiceById(id);

            request.setAttribute("service", s);

            RequestDispatcher rd
                    = request.getRequestDispatcher("views/admin/ServicesEdit.jsp");

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

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        String name = request.getParameter("name");
        String priceRaw = request.getParameter("price");
        String error = " ";

        if (name == null || priceRaw == null) {
            error = "Invalid request";
        } else {

            name = name.trim();

            if (name.isEmpty()) {
                error = "Service name is required";
            } else if (name.length() > 100) {
                error = "Name too long";
            } else if (!name.matches("^[\\p{L}0-9 ]+$")) {
                error = "Name invalid";
            } else {
                try {
                    double price = Double.parseDouble(priceRaw);
                    if (price < 0 || price > 1_000_000_000) {
                        error = "Price invalid";
                    }
                } catch (Exception e) {
                    error = "Price must be number";
                }
            }
        }
        if (user == null) {
            response.sendRedirect("Login");
        } else {

            int id = Integer.parseInt(request.getParameter("id"));

            double price = Double.parseDouble(request.getParameter("price"));

            ServicesDAO dao = new ServicesDAO();

            dao.UpdateService(new Service(
                    id,
                    name,
                    price,
                    true
            ));

            response.sendRedirect("Services");
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
