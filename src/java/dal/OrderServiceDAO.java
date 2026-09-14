package dal;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import models.OrderService;

public class OrderServiceDAO extends DBContext {

    PreparedStatement stm;
    ResultSet rs;

    // =========================
    // Add Service vào Order
    // =========================
    public boolean Add(OrderService os) {
        try {
            String sql = """
                    insert into OrderServices (OrderID, ServiceID)
                    values (?, ?)
                    """;

            stm = connection.prepareStatement(sql);
            stm.setInt(1, os.getOrderID());
            stm.setInt(2, os.getServiceID());

            return stm.executeUpdate() > 0;

        } catch (Exception e) {
            System.out.println("add OrderService: " + e.getMessage());
            return false;
        }
    }

    // =========================
    // Lấy theo OrderID
    // =========================
    public List<OrderService> getByOrderID(int orderID) {
        List<OrderService> list = new ArrayList<>();
        try {
            String sql = "select * from OrderServices where OrderID = ?";
            stm = connection.prepareStatement(sql);
            stm.setInt(1, orderID);
            rs = stm.executeQuery();

            while (rs.next()) {
                OrderService os = new OrderService(
                        rs.getInt("OrderServiceID"),
                        rs.getInt("OrderID"),
                        rs.getInt("ServiceID")
                );
                list.add(os);
            }

        } catch (Exception e) {
            System.out.println("getByOrderID: " + e.getMessage());
        }
        return list;
    }

    // =========================
    // Get by ID (PK)
    // =========================
    public OrderService getByID(int orderServiceID) {
        try {
            String sql = "select * from OrderServices where OrderServiceID = ?";
            stm = connection.prepareStatement(sql);
            stm.setInt(1, orderServiceID);
            rs = stm.executeQuery();

            if (rs.next()) {
                return new OrderService(
                        rs.getInt("OrderServiceID"),
                        rs.getInt("OrderID"),
                        rs.getInt("ServiceID")
                );
            }

        } catch (Exception e) {
            System.out.println("getByID: " + e.getMessage());
        }
        return null;
    }

    // =========================
    // Delete theo ID
    // =========================
    public boolean delete(int orderServiceID) {
        try {
            String sql = "delete from OrderServices where OrderServiceID = ?";
            stm = connection.prepareStatement(sql);
            stm.setInt(1, orderServiceID);

            return stm.executeUpdate() > 0;

        } catch (Exception e) {
            System.out.println("delete OrderService: " + e.getMessage());
            return false;
        }
    }
}