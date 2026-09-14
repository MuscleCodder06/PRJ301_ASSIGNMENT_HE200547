package dal;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import models.CustomerBooking;

public class CustomerBookingDAO extends DBContext {

    PreparedStatement stm;
    ResultSet rs;

    public CustomerBookingDAO() {
        super();
        ensureTableExists();
    }

    private void ensureTableExists() {
        try {
            String sql = """
                IF OBJECT_ID('CustomerBookings', 'U') IS NULL
                BEGIN
                    CREATE TABLE CustomerBookings (
                        BookingID INT IDENTITY(1,1) PRIMARY KEY,
                        CustomerID INT NULL,
                        FullName NVARCHAR(100) NOT NULL,
                        Phone VARCHAR(20) NOT NULL,
                        Motorbike NVARCHAR(100) NOT NULL,
                        Problem NVARCHAR(255) NOT NULL,
                        Status VARCHAR(20) NOT NULL DEFAULT 'WAITING',
                        GeneratedMotoID NVARCHAR(20) NULL,
                        ConfirmedOrderID INT NULL,
                        CreatedDate DATETIME NOT NULL DEFAULT GETDATE(),
                        ConfirmedDate DATETIME NULL
                    )
                END
                """;
            stm = connection.prepareStatement(sql);
            stm.execute();

            String alterSql = """
                IF COL_LENGTH('CustomerBookings', 'ConfirmedDate') IS NULL
                BEGIN
                    ALTER TABLE CustomerBookings ADD ConfirmedDate DATETIME NULL
                END
                """;
            stm = connection.prepareStatement(alterSql);
            stm.execute();
        } catch (Exception e) {
            System.out.println("ensureTableExists CustomerBookings: " + e.getMessage());
        }
    }

    public boolean createBooking(CustomerBooking booking) {
        try {
            String sql = """
                INSERT INTO CustomerBookings (CustomerID, FullName, Phone, Motorbike, Problem, Status, CreatedDate)
                VALUES (?, ?, ?, ?, ?, 'WAITING', GETDATE())
                """;
            int customerId = new CustomerDAO().getOrCreateCustomer(booking.getFullName(), booking.getPhone());
            stm = connection.prepareStatement(sql);
            stm.setInt(1, customerId);
            stm.setString(2, booking.getFullName());
            stm.setString(3, booking.getPhone());
            stm.setString(4, booking.getMotorbike() == null || booking.getMotorbike().trim().isEmpty() ? "Chua cap nhat" : booking.getMotorbike());
            stm.setString(5, booking.getProblem());
            return stm.executeUpdate() > 0;
        } catch (Exception e) {
            System.out.println("createBooking: " + e.getMessage());
            return false;
        }
    }

    public int countWaitingBookings() {
        int count = 0;
        try {
            String sql = "SELECT COUNT(*) FROM CustomerBookings WHERE Status = 'WAITING'";
            stm = connection.prepareStatement(sql);
            rs = stm.executeQuery();
            if (rs.next()) {
                count = rs.getInt(1);
            }
        } catch (Exception e) {
            System.out.println("countWaitingBookings: " + e.getMessage());
        }
        return count;
    }

    public List<CustomerBooking> getWaitingBookings() {
        List<CustomerBooking> list = new ArrayList<>();
        try {
            String sql = "SELECT * FROM CustomerBookings WHERE Status = 'WAITING' ORDER BY BookingID DESC";
            stm = connection.prepareStatement(sql);
            rs = stm.executeQuery();
            while (rs.next()) {
                list.add(mapBooking(rs));
            }
        } catch (Exception e) {
            System.out.println("getWaitingBookings: " + e.getMessage());
        }
        return list;
    }

    public CustomerBooking getById(int bookingId) {
        try {
            String sql = "SELECT * FROM CustomerBookings WHERE BookingID = ?";
            stm = connection.prepareStatement(sql);
            stm.setInt(1, bookingId);
            rs = stm.executeQuery();
            if (rs.next()) {
                return mapBooking(rs);
            }
        } catch (Exception e) {
            System.out.println("getById booking: " + e.getMessage());
        }
        return null;
    }

    public boolean moveOutOfWaitingList(int bookingId) {
        try {
            String sql = """
                UPDATE CustomerBookings
                SET Status = 'CONFIRMED'
                WHERE BookingID = ? AND Status = 'WAITING'
                """;
            stm = connection.prepareStatement(sql);
            stm.setInt(1, bookingId);
            return stm.executeUpdate() > 0;
        } catch (Exception e) {
            System.out.println("moveOutOfWaitingList: " + e.getMessage());
            return false;
        }
    }

    public void finalizeBookingAfterOrderCreated(int bookingId, String motoId, int orderId) {
        try {
            String sql = """
                UPDATE CustomerBookings
                SET GeneratedMotoID = ?,
                    ConfirmedOrderID = ?,
                    ConfirmedDate = GETDATE()
                WHERE BookingID = ?
                """;
            stm = connection.prepareStatement(sql);
            stm.setString(1, motoId);
            stm.setInt(2, orderId);
            stm.setInt(3, bookingId);
            stm.executeUpdate();
        } catch (Exception e) {
            System.out.println("finalizeBookingAfterOrderCreated: " + e.getMessage());
        }
    }

    public int confirmBooking(int bookingId, String confirmedBy) {
        CustomerBooking booking = getById(bookingId);
        if (booking == null || !"WAITING".equalsIgnoreCase(booking.getStatus())) {
            return 0;
        }

        String generatedMotoID = "BOOKING-" + bookingId;
        try {
            connection.setAutoCommit(false);
            int customerId = getCustomerIdForBooking(booking);

            String checkMotoSql = "SELECT 1 FROM Motorbikes WHERE MotoID = ?";
            stm = connection.prepareStatement(checkMotoSql);
            stm.setString(1, generatedMotoID);
            rs = stm.executeQuery();

            if (!rs.next()) {
                String insertMotoSql = """
                    INSERT INTO Motorbikes (MotoID, CustomerID, Brand, Model)
                    VALUES (?, ?, ?, ?)
                    """;
                stm = connection.prepareStatement(insertMotoSql);
                stm.setString(1, generatedMotoID);
                stm.setInt(2, customerId);
                stm.setString(3, "Customer Booking");
                stm.setString(4, booking.getMotorbike() == null || booking.getMotorbike().trim().isEmpty() ? "Chua cap nhat" : booking.getMotorbike());
                stm.executeUpdate();
            }

            String insertOrderSql = """
                INSERT INTO RepairOrders
                (MotoID, CreatedBy, TechnicianUsername, Description, Status, CreatedDate)
                VALUES (?, ?, ?, ?, 'PENDING', GETDATE())
                """;
            stm = connection.prepareStatement(insertOrderSql, PreparedStatement.RETURN_GENERATED_KEYS);
            stm.setString(1, generatedMotoID);
            stm.setString(2, confirmedBy);
            stm.setString(3, confirmedBy);
            stm.setString(4, booking.getProblem());
            stm.executeUpdate();

            rs = stm.getGeneratedKeys();
            int newOrderId = 0;
            if (rs.next()) {
                newOrderId = rs.getInt(1);
            }

            if (newOrderId <= 0) {
                connection.rollback();
                connection.setAutoCommit(true);
                return 0;
            }

            String updateSql = """
                UPDATE CustomerBookings
                SET Status = 'CONFIRMED',
                    GeneratedMotoID = ?,
                    ConfirmedOrderID = ?,
                    ConfirmedDate = GETDATE()
                WHERE BookingID = ?
                """;
            stm = connection.prepareStatement(updateSql);
            stm.setString(1, generatedMotoID);
            stm.setInt(2, newOrderId);
            stm.setInt(3, bookingId);
            stm.executeUpdate();

            connection.commit();
            connection.setAutoCommit(true);
            return newOrderId;
        } catch (Exception e) {
            try {
                connection.rollback();
                connection.setAutoCommit(true);
            } catch (Exception rollbackEx) {
                System.out.println("confirmBooking rollback: " + rollbackEx.getMessage());
            }
            System.out.println("confirmBooking: " + e.getMessage());
            return 0;
        }
    }

    private CustomerBooking mapBooking(ResultSet resultSet) throws Exception {
        int confirmedOrderId = resultSet.getInt("ConfirmedOrderID");
        return new CustomerBooking(
                resultSet.getInt("BookingID"),
                resultSet.getString("FullName"),
                resultSet.getString("Phone"),
                resultSet.getString("Motorbike"),
                resultSet.getString("Problem"),
                resultSet.getString("Status"),
                resultSet.getString("GeneratedMotoID"),
                resultSet.wasNull() ? null : confirmedOrderId,
                resultSet.getTimestamp("CreatedDate"),
                resultSet.getTimestamp("ConfirmedDate")
        );
    }

    private int getCustomerIdForBooking(CustomerBooking booking) {
        try {
            String sql = "SELECT CustomerID FROM CustomerBookings WHERE BookingID = ?";
            stm = connection.prepareStatement(sql);
            stm.setInt(1, booking.getBookingID());
            rs = stm.executeQuery();
            if (rs.next()) {
                int customerId = rs.getInt("CustomerID");
                if (!rs.wasNull()) {
                    return customerId;
                }
            }
        } catch (Exception e) {
            System.out.println("getCustomerIdForBooking: " + e.getMessage());
        }
        return new CustomerDAO().getOrCreateCustomer(booking.getFullName(), booking.getPhone());
    }
    // Hàm cập nhật trạng thái Booking thành CANCELLED
    public boolean cancelBooking(int bookingId) {
        try {
            String sql = """
                UPDATE CustomerBookings
                SET Status = 'CANCELLED'
                WHERE BookingID = ? AND Status = 'WAITING'
                """;
            stm = connection.prepareStatement(sql);
            stm.setInt(1, bookingId);
            return stm.executeUpdate() > 0;
        } catch (Exception e) {
            System.out.println("Lỗi cancelBooking: " + e.getMessage());
            return false;
        }
    }
}
