package controllers;

import dal.CustomerBookingDAO;
import dal.MotorbikeDAO;
import dal.OrderPartDAO;
import dal.OrderServiceDAO;
import dal.PartDAO;
import dal.RepairOrderDAO;
import dal.ServicesDAO;
import dal.UserDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import models.CustomerBooking;
import models.OrderPart;
import models.OrderService;
import models.RepairOrder;
import models.User;

public class CreateOrderController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        if (user == null) {
            response.sendRedirect("Login");
            return;
        }

        loadFormData(request, user);

        String bookingIdRaw = request.getParameter("bookingId");
        if (bookingIdRaw != null && !bookingIdRaw.trim().isEmpty()) {
            try {
                int bookingId = Integer.parseInt(bookingIdRaw);
                CustomerBooking booking = new CustomerBookingDAO().getById(bookingId);
                if (booking != null) {
                    request.setAttribute("bookingId", bookingId);
                    request.setAttribute("ownerName", booking.getFullName());
                    request.setAttribute("phone", booking.getPhone());
                    request.setAttribute("des", booking.getProblem());
                } else {
                    request.setAttribute("error", "Booking not found.");
                }
            } catch (Exception e) {
                request.setAttribute("error", "Invalid booking ID.");
            }
        }

        request.getRequestDispatcher("views/CreateOrder.jsp").forward(request, response);
    }

    private void loadFormData(HttpServletRequest request, User user) {
        UserDAO uDao = new UserDAO();
        List<User> users = uDao.GetUsers();
        if (user.getRole().getRoleName().equalsIgnoreCase("staff")) {
            List<User> techList = new ArrayList<>();
            for (User u : users) {
                if ("staff".equalsIgnoreCase(u.getRole().getRoleName())) {
                    techList.add(u);
                }
            }
            request.setAttribute("technicians", techList);
        } else {
            request.setAttribute("technicians", users);
        }

        request.setAttribute("availableParts", new PartDAO().GetParts());
        request.setAttribute("availableServices", new ServicesDAO().GetServices());
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        try {
            String motoId = request.getParameter("motoId");
            String ownerName = request.getParameter("ownerName");
            String phone = request.getParameter("phone");
            String brand = request.getParameter("brand");
            String model = request.getParameter("model");
            String tech = request.getParameter("tech");
            String des = request.getParameter("des");
            String bookingIdRaw = request.getParameter("bookingId");
            String error = "";

            if (motoId == null || ownerName == null || phone == null
                    || brand == null || model == null || tech == null || des == null) {
                error = "Invalid request";
            } else {
                motoId = motoId.trim();
                ownerName = ownerName.trim();
                phone = phone.trim();
                brand = brand.trim();
                model = model.trim();
                tech = tech.trim();
                des = des.trim();

                if (ownerName.isEmpty()) {
                    error = "Owner name is required";
                } else if (ownerName.length() > 100) {
                    error = "Owner name too long";
                } else if (phone.isEmpty()) {
                    error = "Phone is required";
                } else if (!phone.matches("0\\d{9}")) {
                    error = "Phone must start with 0 and have 10 digits";
                } else if (motoId.isEmpty()) {
                    error = "License plate is required";
                } else if (!motoId.matches("^[0-9]{2}[A-Z0-9]{1,2}[0-9]{4,5}$")) {
                    error = "Invalid license plate (e.g., 29AB12345, 29Y523456)";
                } else if (brand.isEmpty()) {
                    error = "Brand is required";
                } else if (model.isEmpty()) {
                    error = "Model is required";
                } else if (tech.isEmpty()) {
                    error = "Technician is required";
                } else if (des.isEmpty()) {
                    error = "Description is required";
                } else if (des.length() > 255) {
                    error = "Description too long";
                }
            }

            if (!error.isEmpty()) {
                request.setAttribute("error", error);
                request.setAttribute("ownerName", ownerName);
                request.setAttribute("phone", phone);
                request.setAttribute("motoId", motoId);
                request.setAttribute("brand", brand);
                request.setAttribute("model", model);
                request.setAttribute("tech", tech);
                request.setAttribute("des", des);
                request.setAttribute("bookingId", bookingIdRaw);
                loadFormData(request, user);
                request.getRequestDispatcher("views/CreateOrder.jsp").forward(request, response);
                return;
            }

            MotorbikeDAO mDao = new MotorbikeDAO();
            mDao.CheckAndInsertMoto(motoId, ownerName, phone, brand, model);

            RepairOrder repairOrder = new RepairOrder();
            repairOrder.setMotoID(motoId);
            repairOrder.setCreatedBy(user.getUsername());
            repairOrder.setTechnicianUsername(tech);
            repairOrder.setDescription(des);

            int newOrderId = new RepairOrderDAO().CreateOrderReturnId(repairOrder);

            if (newOrderId > 0) {
                if (bookingIdRaw != null && !bookingIdRaw.trim().isEmpty()) {
                    try {
                        int bookingId = Integer.parseInt(bookingIdRaw);
                        new CustomerBookingDAO().finalizeBookingAfterOrderCreated(bookingId, motoId, newOrderId);
                    } catch (Exception e) {
                        System.out.println("Finalize booking error: " + e.getMessage());
                    }
                }

                String[] partIds = request.getParameterValues("partId");
                String[] quantities = request.getParameterValues("quantity");

                if (partIds != null) {
                    PartDAO pDao = new PartDAO();
                    OrderPartDAO opDao = new OrderPartDAO();

                    for (int i = 0; i < partIds.length; i++) {
                        String pIdStr = partIds[i];
                        if (pIdStr != null && !pIdStr.trim().isEmpty()) {
                            int partId = Integer.parseInt(pIdStr);
                            int qty = (quantities != null && quantities.length > i && !quantities[i].isEmpty())
                                    ? Integer.parseInt(quantities[i]) : 1;

                            if (pDao.CheckStock(partId, qty)) {
                                pDao.DeductStock(partId, qty);
                                opDao.Add(new OrderPart(0, newOrderId, partId, qty));
                            } else {
                                session.setAttribute("errorMsg", "Some parts were skipped because there was not enough stock.");
                            }
                        }
                    }
                }

                String[] serviceIds = request.getParameterValues("serviceId");
                if (serviceIds != null) {
                    OrderServiceDAO osDao = new OrderServiceDAO();
                    for (String sIdStr : serviceIds) {
                        if (sIdStr != null && !sIdStr.trim().isEmpty()) {
                            int serviceId = Integer.parseInt(sIdStr);
                            osDao.Add(new OrderService(0, newOrderId, serviceId));
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("CreateOrder doPost error: " + e.getMessage());
        }

        response.sendRedirect("RepairOrder");
    }
}
