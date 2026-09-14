package dal;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Date;

public class InvoiceDAO extends DBContext {
    
    // Lấy Ngày thanh toán (Hàm cũ nãy mình làm)
    public Date GetPaymentDate(int orderId) {
        try {
            String sql = "SELECT PaymentDate FROM Invoices WHERE OrderID = ?";
            PreparedStatement stm = connection.prepareStatement(sql);
            stm.setInt(1, orderId);
            ResultSet rs = stm.executeQuery();
            if (rs.next()) {
                return rs.getTimestamp("PaymentDate"); 
            }
        } catch (Exception e) {
            System.out.println("Lỗi GetPaymentDate: " + e.getMessage());
        }
        return null;
    }

    // THÊM HÀM NÀY: Dùng để lưu Hóa đơn mới vào DB
    public void AddInvoice(int orderId, double totalAmount) {
        try {
            String sql = "INSERT INTO Invoices (OrderID, TotalAmount, PaymentDate) VALUES (?, ?, GETDATE())";
            // Vì InvoiceDAO kế thừa DBContext (nằm cùng package dal) nên gọi connection thoải mái
            PreparedStatement stm = connection.prepareStatement(sql);
            stm.setInt(1, orderId);
            stm.setDouble(2, totalAmount);
            stm.execute();
        } catch (Exception e) {
            System.out.println("Lỗi AddInvoice: " + e.getMessage());
        }
    }
}