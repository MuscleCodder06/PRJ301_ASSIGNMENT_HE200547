/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controllers;

import dal.RoleDAO;
import dal.UserDAO;
import dal.UserProfilesDAO;
import jakarta.servlet.RequestDispatcher;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import models.Role;
import models.User;
import models.UserProfile;

/**
 *
 * @author ADMIN
 */
public class StaffEditController extends HttpServlet {

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
            out.println("<title>Servlet StaffEditController</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet StaffEditController at " + request.getContextPath() + "</h1>");
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
        User login = (User) session.getAttribute("user");
        if (login == null) {
            response.sendRedirect("Login");
        } else {
            if (!login.getRole().getRoleName().equalsIgnoreCase("admin")) {
                response.sendRedirect("Staff");
                return;
            }
            String username = request.getParameter("id");
            UserDAO udao = new UserDAO();
            RoleDAO rdao = new RoleDAO();
            List<Role> roles = rdao.GetRoles();
            UserProfilesDAO uspdao = new UserProfilesDAO();
            User user = udao.GetUserByUsername(username);
            UserProfile usp = uspdao.GetUserProfileByUsername(username);
            RequestDispatcher rd = request.getRequestDispatcher("views/admin/StaffEdit.jsp");

            request.setAttribute("user", user);
            request.setAttribute("userprf", usp);
            request.setAttribute("roles", roles);
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
        User login = (User) session.getAttribute("user");
        if (login == null) {
            response.sendRedirect("Login");
        } else {
            String uName = request.getParameter("username");
            String psd = request.getParameter("password");
            int rID = Integer.parseInt(request.getParameter("roleId"));
            RoleDAO rdao = new RoleDAO();
            String rName = "";
            List<Role> roles = rdao.GetRoles();
            for (Role role : roles) {
                if (role.getRoleID() == rID) {
                    rName = role.getRoleName();
                }
            }
            String fullname = request.getParameter("fullname");
            String email = request.getParameter("email");
            String phone = request.getParameter("phone");
            String adr = request.getParameter("address");
            String dateStr = request.getParameter("dob");
            String gender = request.getParameter("gen");
            Date date = null;
            String error = " ";
            String username = request.getParameter("username");
            String password = request.getParameter("password");
            String fullName = request.getParameter("fullName");
            String roleIdRaw = request.getParameter("roleId");

            if (username == null || password == null || fullName == null
                    || email == null || phone == null || roleIdRaw == null) {
                error = "Invalid request";
            } else {

                username = username.trim();
                password = password.trim();
                fullName = fullName.trim();
                email = email.trim().toLowerCase();
                phone = phone.trim();

                // USERNAME
                if (username.isEmpty()) {
                    error = "Username is required";
                } else if (username.length() < 3 || username.length() > 20) {
                    error = "Username must be 3-20 characters";
                } else if (!username.matches("^[a-zA-Z0-9_]+$")) {
                    error = "Username only letters, numbers, underscore";
                } // PASSWORD (MẠNH)
                else if (password.length() < 8) {
                    error = "Password must be >= 8 characters";
                } else if (!password.matches(".*[A-Z].*")) {
                    error = "Password must have uppercase letter";
                } else if (!password.matches(".*[a-z].*")) {
                    error = "Password must have lowercase letter";
                } else if (!password.matches(".*\\d.*")) {
                    error = "Password must have number";
                } // FULLNAME
                else if (fullName.isEmpty()) {
                    error = "Full name is required";
                } else if (fullName.length() > 100) {
                    error = "Full name too long";
                } else if (!fullName.matches("^[\\p{L} ]+$")) {
                    error = "Full name invalid";
                } // EMAIL (CHẶT)
                else if (!email.matches("^[A-Za-z0-9+_.-]+@gmail\\.com$")) {
                    error = "Email must be valid @gmail.com";
                } // PHONE (VN)
                else if (!phone.matches("^0[3|5|7|8|9][0-9]{8}$")) {
                    error = "Phone invalid (VN format)";
                } // ROLE
                else {
                    try {
                        int roleId = Integer.parseInt(roleIdRaw);
                        if (roleId <= 0) {
                            error = "Invalid role";
                        }
                    } catch (Exception e) {
                        error = "Role must be number";
                    }
                }
            }

            if (!error.isEmpty()) {
                request.setAttribute("error", error);
                request.getRequestDispatcher("views/admin/StaffCreate.jsp").forward(request, response);
                return;
            }
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            try {
                date = format.parse(dateStr);
            } catch (ParseException ex) {
                Logger.getLogger(StaffEditController.class.getName()).log(Level.SEVERE, null, ex);
            }
            UserDAO dao = new UserDAO();
            dao.UpdateUser(new User(uName, psd, fullname, new Role(rID, rName), true));
            UserProfilesDAO uspdao = new UserProfilesDAO();
            uspdao.UpdateUserProfile(new UserProfile(uName, fullname, email, phone, adr, date, gender));
            response.sendRedirect("Staff");
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
