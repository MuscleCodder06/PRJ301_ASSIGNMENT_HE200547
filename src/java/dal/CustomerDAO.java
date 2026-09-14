package dal;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import models.Customer;
import models.CustomerRepairStat;

public class CustomerDAO extends DBContext {

    PreparedStatement stm;
    ResultSet rs;

    public Customer getByPhone(String phone) {
        try {
            String sql = "SELECT * FROM Customers WHERE Phone = ?";
            stm = connection.prepareStatement(sql);
            stm.setString(1, phone);
            rs = stm.executeQuery();
            if (rs.next()) {
                return mapCustomer(rs);
            }
        } catch (Exception e) {
            System.out.println("getByPhone Customer: " + e.getMessage());
        }
        return null;
    }

    public Customer getById(int customerId) {
        try {
            String sql = "SELECT * FROM Customers WHERE CustomerID = ?";
            stm = connection.prepareStatement(sql);
            stm.setInt(1, customerId);
            rs = stm.executeQuery();
            if (rs.next()) {
                return mapCustomer(rs);
            }
        } catch (Exception e) {
            System.out.println("getById Customer: " + e.getMessage());
        }
        return null;
    }

    public int createCustomer(Customer customer) {
        try {
            String sql = """
                INSERT INTO Customers (FullName, Phone, Address, CreatedDate, IsActive)
                VALUES (?, ?, ?, GETDATE(), 1)
                """;
            stm = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            stm.setString(1, customer.getFullName());
            stm.setString(2, customer.getPhone());
            stm.setString(3, customer.getAddress());
            stm.executeUpdate();

            rs = stm.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (Exception e) {
            System.out.println("createCustomer: " + e.getMessage());
        }
        return 0;
    }

    public int getOrCreateCustomer(String fullName, String phone) {
        Customer existing = getByPhone(phone);
        if (existing != null) {
            return existing.getCustomerID();
        }

        Customer customer = new Customer();
        customer.setFullName((fullName != null && !fullName.trim().isEmpty()) ? fullName.trim() : "Khach vang lai");
        customer.setPhone(phone);
        customer.setAddress(null);
        return createCustomer(customer);
    }

    public List<CustomerRepairStat> getTopCustomersByRepairOrders(int limit) {
        List<CustomerRepairStat> topCustomers = new ArrayList<>();
        try {
            String sql = """
                SELECT TOP (?)
                    c.CustomerID,
                    c.FullName,
                    c.Phone,
                    COUNT(ro.OrderID) AS TotalOrders
                FROM Customers c
                INNER JOIN Motorbikes m ON m.CustomerID = c.CustomerID
                INNER JOIN RepairOrders ro ON ro.MotoID = m.MotoID
                WHERE ro.Status = 'COMPLETED' 
                GROUP BY c.CustomerID, c.FullName, c.Phone
                ORDER BY COUNT(ro.OrderID) DESC, c.FullName ASC
                """;
            stm = connection.prepareStatement(sql);
            stm.setInt(1, limit);
            rs = stm.executeQuery();
            while (rs.next()) {
                topCustomers.add(new CustomerRepairStat(
                        rs.getInt("CustomerID"),
                        rs.getString("FullName"),
                        rs.getString("Phone"),
                        rs.getInt("TotalOrders")
                ));
            }
        } catch (Exception e) {
            System.out.println("getTopCustomersByRepairOrders: " + e.getMessage());
        }
        return topCustomers;
    }

    private Customer mapCustomer(ResultSet resultSet) throws Exception {
        return new Customer(
                resultSet.getInt("CustomerID"),
                resultSet.getString("FullName"),
                resultSet.getString("Phone"),
                resultSet.getString("Address"),
                resultSet.getTimestamp("CreatedDate"),
                resultSet.getBoolean("IsActive")
        );
    }
}
