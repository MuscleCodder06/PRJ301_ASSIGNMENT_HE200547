package dal;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import models.RepairOrder;
import models.TechnicianMonthlyStat;

public class RepairOrderDAO extends DBContext {

    PreparedStatement stm;
    ResultSet rs;

    public RepairOrder GetOrderById(int id) {
        RepairOrder r = null;
        try {
            String strSQL = "select * from RepairOrders where OrderID = ?";
            stm = connection.prepareCall(strSQL);
            stm.setInt(1, id);
            rs = stm.executeQuery();

            if (rs.next()) {
                r = new RepairOrder(
                        rs.getInt("OrderID"),
                        rs.getString("MotoID"),
                        rs.getString("CreatedBy"),
                        rs.getString("TechnicianUsername"),
                        rs.getString("Description"),
                        rs.getString("Status"),
                        rs.getDate("CreatedDate")
                );
            }
        } catch (Exception e) {
            System.out.println("GetOrderById: " + e.getMessage());
        }
        return r;
    }

    public List<RepairOrder> GetOrders() {
        List<RepairOrder> list = new ArrayList<>();
        try {
            String strSQL = "select * from RepairOrders order by OrderID asc";
            stm = connection.prepareCall(strSQL);
            rs = stm.executeQuery();

            while (rs.next()) {
                RepairOrder r = new RepairOrder(
                        rs.getInt("OrderID"),
                        rs.getString("MotoID"),
                        rs.getString("CreatedBy"),
                        rs.getString("TechnicianUsername"),
                        rs.getString("Description"),
                        rs.getString("Status"),
                        rs.getDate("CreatedDate")
                );
                list.add(r);
            }
        } catch (Exception e) {
            System.out.println("GetOrders: " + e.getMessage());
        }
        return list;
    }

    // Trả về OrderID vừa tạo ra để add thêm phụ tùng
    public int CreateOrderReturnId(RepairOrder r) {
        int generatedId = 0;
        try {
            String strSQL = """
                    insert into RepairOrders
                    (MotoID, CreatedBy, TechnicianUsername, Description, Status, CreatedDate)
                    values(?,?,?,?, 'PENDING', GETDATE())
                    """;
            stm = connection.prepareStatement(strSQL, PreparedStatement.RETURN_GENERATED_KEYS);
            stm.setString(1, r.getMotoID());
            stm.setString(2, r.getCreatedBy());
            stm.setString(3, r.getTechnicianUsername());
            stm.setString(4, r.getDescription());
            stm.executeUpdate();

            rs = stm.getGeneratedKeys();
            if (rs.next()) {
                generatedId = rs.getInt(1); 
            }
        } catch (Exception e) {
            System.out.println("Lỗi CreateOrderReturnId: " + e.getMessage());
        }
        return generatedId;
    }

    public RepairOrder UpdateOrder(RepairOrder r) {
        RepairOrder found = GetOrderById(r.getOrderID());
        if (found == null || "PAID".equals(found.getStatus())) return null;

        try {
            String strSQL = "update RepairOrders set MotoID = ?, TechnicianUsername = ?, Description = ? where OrderID = ?";
            stm = connection.prepareCall(strSQL);
            stm.setString(1, r.getMotoID());
            stm.setString(2, r.getTechnicianUsername());
            stm.setString(3, r.getDescription());
            stm.setInt(4, r.getOrderID());
            stm.execute();
        } catch (Exception e) {
            System.out.println("UpdateOrder: " + e.getMessage());
        }
        return r;
    }

    // ===============================================
    // XÓA PHIẾU (Đã vá lỗi khóa ngoại từ bảng Booking)
    // ===============================================
    public RepairOrder DeleteOrder(int id) {
        RepairOrder found = GetOrderById(id);
        if (found == null || "COMPLETED".equals(found.getStatus())) return null; 

        try {
            // 1. GỠ KHÓA NGOẠI TỪ BẢNG CustomerBookings (Đồng thời trả lịch hẹn về trạng thái WAITING để không mất đơn của khách)
            String revertBookingSQL = "UPDATE CustomerBookings SET ConfirmedOrderID = NULL, Status = 'WAITING' WHERE ConfirmedOrderID = " + id;
            connection.prepareStatement(revertBookingSQL).execute();

            // 2. DỌN SẠCH CÁC BẢNG CON KHÁC
            connection.prepareStatement("DELETE FROM Invoices WHERE OrderID = " + id).execute();
            connection.prepareStatement("DELETE FROM OrderParts WHERE OrderID = " + id).execute();
            connection.prepareStatement("DELETE FROM OrderServices WHERE OrderID = " + id).execute();
            
            // 3. XÓA BẢNG CHA (Phiếu sửa chữa) SAU CÙNG
            String strSQL = "DELETE FROM RepairOrders WHERE OrderID = ?";
            stm = connection.prepareCall(strSQL);
            stm.setInt(1, id);
            stm.execute();
        } catch (Exception e) {
            System.out.println("Lỗi DeleteOrder: " + e.getMessage());
        }
        return found;
    }

    public void UpdateStatus(int orderId, String status) {
        try {
            String strSQL = "update RepairOrders set Status = ? where OrderID = ?";
            stm = connection.prepareCall(strSQL);
            stm.setString(1, status);
            stm.setInt(2, orderId);
            stm.execute();
        } catch (Exception e) {
            System.out.println("UpdateStatus: " + e.getMessage());
        }
    }

    public int countTotalOrders() {
        int count = 0;
        try {
            String sql = "SELECT COUNT(*) FROM RepairOrders";
            stm = connection.prepareCall(sql);
            rs = stm.executeQuery();
            if (rs.next()) count = rs.getInt(1);
        } catch (Exception e) {}
        return count;
    }

    public int countOrderByStatus(String status) {
        int count = 0;
        try {
            String sql = "SELECT COUNT(*) FROM RepairOrders WHERE Status = ?";
            stm = connection.prepareCall(sql);
            stm.setString(1, status);
            rs = stm.executeQuery();
            if (rs.next()) count = rs.getInt(1);
        } catch (Exception e) {}
        return count;
    }

    public TechnicianMonthlyStat getTopTechnicianThisMonth() {
        TechnicianMonthlyStat topTech = null;
        try {
            // Đếm số đơn đã COMPLETED trong tháng hiện tại
            String sql = """
                SELECT TOP 1
                    u.Username,
                    u.FullName,
                    COUNT(ro.OrderID) AS CompletedOrders
                FROM RepairOrders ro
                JOIN Users u ON ro.TechnicianUsername = u.Username
                WHERE ro.Status = 'COMPLETED'
                  AND MONTH(ro.CreatedDate) = MONTH(GETDATE())
                  AND YEAR(ro.CreatedDate) = YEAR(GETDATE())
                GROUP BY u.Username, u.FullName
                ORDER BY CompletedOrders DESC
                """;
            stm = connection.prepareStatement(sql);
            rs = stm.executeQuery();
            
            if (rs.next()) {
                topTech = new TechnicianMonthlyStat(
                    rs.getString("Username"),
                    rs.getString("FullName"),
                    rs.getInt("CompletedOrders")
                );
            }
        } catch (Exception e) {
            System.out.println("Lỗi getTopTechnicianThisMonth: " + e.getMessage());
        }
        return topTech;
    }

    // ===============================================
    // DOANH THU (Đã vá lỗi, nay lấy theo PaymentDate từ bảng Invoices)
    // ===============================================
    public double getRevenue(String period) {
        double total = 0;
        try {
            String dateCondition = "";
            if ("DAY".equals(period)) {
                dateCondition = "CAST(PaymentDate AS DATE) = CAST(GETDATE() AS DATE)";
            } else if ("MONTH".equals(period)) {
                dateCondition = "MONTH(PaymentDate) = MONTH(GETDATE()) AND YEAR(PaymentDate) = YEAR(GETDATE())";
            } else if ("YEAR".equals(period)) {
                dateCondition = "YEAR(PaymentDate) = YEAR(GETDATE())";
            }

            // Gọi SUM từ bảng Invoices siêu gọn nhẹ
            String sql = "SELECT ISNULL(SUM(TotalAmount), 0) AS Total FROM Invoices WHERE " + dateCondition;
            
            stm = connection.prepareStatement(sql);
            rs = stm.executeQuery();
            if (rs.next()) {
                total = rs.getDouble("Total");
            }
        } catch (Exception e) {
            System.out.println("Lỗi getRevenue: " + e.getMessage());
        }
        return total;
    }
    // =========================
    // TÌM KIẾM THEO BIỂN SỐ XE 
    // =========================
    public List<RepairOrder> SearchByMotoID(String motoID) {
        List<RepairOrder> list = new ArrayList<>();
        try {
            String sql = """
                select * from RepairOrders
                where MotoID like ?
                order by OrderID asc
                """;
            stm = connection.prepareStatement(sql);
            stm.setString(1, "%" + motoID + "%");
            rs = stm.executeQuery();

            while (rs.next()) {
                RepairOrder r = new RepairOrder(
                        rs.getInt("OrderID"),
                        rs.getString("MotoID"),
                        rs.getString("CreatedBy"),
                        rs.getString("TechnicianUsername"),
                        rs.getString("Description"),
                        rs.getString("Status"),
                        rs.getDate("CreatedDate")
                );
                list.add(r);
            }
        } catch (Exception e) {
            System.out.println("SearchByMotoID: " + e.getMessage());
        }
        return list;
    }
}
