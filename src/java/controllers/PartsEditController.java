/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controllers;

import dal.PartDAO;
import jakarta.servlet.RequestDispatcher;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import models.Part;
import models.User;

/**
 *
 * @author ADMIN
 */
public class PartsEditController extends HttpServlet {

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
            out.println("<title>Servlet PartsEditController</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet PartsEditController at " + request.getContextPath() + "</h1>");
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

        String name = request.getParameter("name");
        String priceRaw = request.getParameter("price");
        String stockRaw = request.getParameter("quantity");
        String des = request.getParameter("description");
        String error = " ";
        if (name == null || priceRaw == null || stockRaw == null) {
            error = "Invalid request";
        } else {

            name = name.trim();
            des = des == null ? "" : des.trim();

            if (name.isEmpty()) {
                error = "Part name is required";
            } else if (name.length() > 100) {
                error = "Name too long";
            } else if (!name.matches("^[\\p{L}0-9 ]+$")) {
                error = "Name invalid";
            } else {
                try {
                    double price = Double.parseDouble(priceRaw);
                    if (price < 0 || price > 1_000_000_000) {
                        error = "Price invalid range";
                    }
                } catch (Exception e) {
                    error = "Price must be number";
                }
            }

            if (error.isEmpty()) {
                try {
                    int stock = Integer.parseInt(stockRaw);
                    if (stock < 0 || stock > 100000) {
                        error = "Stock invalid range";
                    }
                } catch (Exception e) {
                    error = "Stock must be number";
                }
            }

            if (error.isEmpty() && des.length() > 255) {
                error = "Description too long";
            }
        }

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        if (user == null) {
            response.sendRedirect("Login");
        } else {
            if (!user.getRole().getRoleName().equalsIgnoreCase("admin")) {
                response.sendRedirect("Parts");
                return;
            }
            int id = Integer.parseInt(request.getParameter("id"));

            PartDAO dao = new PartDAO();
            Part part = dao.GetPartById(id);

            RequestDispatcher rd
                    = request.getRequestDispatcher("views/admin/PartsEdit.jsp");

            request.setAttribute("part", part);

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

        if (user == null) {
            response.sendRedirect("Login");
        } else {

            int id = Integer.parseInt(request.getParameter("id"));
            String name = request.getParameter("partname");
            double price = Double.parseDouble(request.getParameter("price"));
            int quantity = Integer.parseInt(request.getParameter("quantity"));
            String desc = request.getParameter("description");

            PartDAO dao = new PartDAO();

            dao.UpdatePart(new Part(
                    id,
                    name,
                    price,
                    quantity,
                    desc,
                    true
            ));

            response.sendRedirect("Parts");
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
