package dal;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import models.OrderPart;

public class OrderPartDAO extends DBContext {

    PreparedStatement stm;
    ResultSet rs;

    // =============================
    // 1. Add Part vào Order
    // =============================
    public boolean Add(OrderPart op) {
        try {
            String sql = """
                    insert into OrderParts (OrderID, PartID, Quantity)
                    values (?, ?, ?)
                    """;

            stm = connection.prepareStatement(sql);
            stm.setInt(1, op.getOrderID());
            stm.setInt(2, op.getPartID());
            stm.setInt(3, op.getQuantity());

            return stm.executeUpdate() > 0;

        } catch (Exception e) {
            System.out.println("add OrderPart: " + e.getMessage());
            return false;
        }
    }

    // =============================
    // 2. Lấy tất cả Part theo OrderID
    // =============================
    public List<OrderPart> GetByOrderID(int orderID) {
        List<OrderPart> list = new ArrayList<>();
        try {
            String sql = "select * from OrderParts where OrderID = ?";
            stm = connection.prepareStatement(sql);
            stm.setInt(1, orderID);
            rs = stm.executeQuery();

            while (rs.next()) {
                OrderPart op = new OrderPart(
                        rs.getInt("OrderPartID"),
                        rs.getInt("OrderID"),
                        rs.getInt("PartID"),
                        rs.getInt("Quantity")
                );
                list.add(op);
            }

        } catch (Exception e) {
            System.out.println("getByOrderID: " + e.getMessage());
        }
        return list;
    }

    // =============================
    // 3. Get theo ID (PK)
    // =============================
    public OrderPart GetByID(int orderPartID) {
        try {
            String sql = "select * from OrderParts where OrderPartID = ?";
            stm = connection.prepareStatement(sql);
            stm.setInt(1, orderPartID);
            rs = stm.executeQuery();

            if (rs.next()) {
                return new OrderPart(
                        rs.getInt("OrderPartID"),
                        rs.getInt("OrderID"),
                        rs.getInt("PartID"),
                        rs.getInt("Quantity")
                );
            }

        } catch (Exception e) {
            System.out.println("getByID: " + e.getMessage());
        }
        return null;
    }

    // =============================
    // 4. Update Quantity
    // =============================
    public boolean UpdateQuantity(int orderPartID, int quantity) {
        try {
            String sql = """
                    update OrderParts
                    set Quantity = ?
                    where OrderPartID = ?
                    """;

            stm = connection.prepareStatement(sql);
            stm.setInt(1, quantity);
            stm.setInt(2, orderPartID);

            return stm.executeUpdate() > 0;

        } catch (Exception e) {
            System.out.println("updateQuantity: " + e.getMessage());
            return false;
        }
    }

    // =============================
    // 5. Delete
    // =============================
    public boolean Delete(int orderPartID) {
        try {
            String sql = "delete from OrderParts where OrderPartID = ?";
            stm = connection.prepareStatement(sql);
            stm.setInt(1, orderPartID);

            return stm.executeUpdate() > 0;

        } catch (Exception e) {
            System.out.println("delete OrderPart: " + e.getMessage());
            return false;
        }
    }
}