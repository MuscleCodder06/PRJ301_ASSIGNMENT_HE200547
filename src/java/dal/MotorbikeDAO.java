package dal;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import models.Motorbike;

public class MotorbikeDAO extends DBContext {

    PreparedStatement stm;
    ResultSet rs;

    public Motorbike GetByMotoID(String motoID) {
        Motorbike motorbike = null;
        try {
            String sql = """
                SELECT m.MotoID, m.CustomerID, m.Brand, m.Model, c.FullName AS OwnerName, c.Phone
                FROM Motorbikes m
                INNER JOIN Customers c ON c.CustomerID = m.CustomerID
                WHERE m.MotoID = ?
                """;
            stm = connection.prepareStatement(sql);
            stm.setString(1, motoID);
            rs = stm.executeQuery();

            if (rs.next()) {
                motorbike = mapMotorbike(rs);
            }
        } catch (Exception e) {
            System.out.println("GetByMotoID: " + e.getMessage());
        }
        return motorbike;
    }

    public List<Motorbike> GetMotorbikes() {
        List<Motorbike> list = new ArrayList<>();
        try {
            String sql = """
                SELECT m.MotoID, m.CustomerID, m.Brand, m.Model, c.FullName AS OwnerName, c.Phone
                FROM Motorbikes m
                INNER JOIN Customers c ON c.CustomerID = m.CustomerID
                ORDER BY m.MotoID
                """;
            stm = connection.prepareStatement(sql);
            rs = stm.executeQuery();

            while (rs.next()) {
                list.add(mapMotorbike(rs));
            }
        } catch (Exception e) {
            System.out.println("GetMotorbikes: " + e.getMessage());
        }
        return list;
    }

    public boolean CreateMotorbike(Motorbike m) {
        try {
            String sql = """
                INSERT INTO Motorbikes (MotoID, CustomerID, Brand, Model)
                VALUES (?, ?, ?, ?)
                """;
            stm = connection.prepareStatement(sql);
            stm.setString(1, m.getMotoID());
            stm.setInt(2, m.getCustomerID());
            stm.setString(3, m.getBrand());
            stm.setString(4, m.getModel());
            return stm.executeUpdate() > 0;
        } catch (Exception e) {
            System.out.println("CreateMotorbike: " + e.getMessage());
            return false;
        }
    }

    public boolean UpdateMotorbike(Motorbike m) {
        try {
            String sql = """
                UPDATE Motorbikes
                SET CustomerID = ?, Brand = ?, Model = ?
                WHERE MotoID = ?
                """;
            stm = connection.prepareStatement(sql);
            stm.setInt(1, m.getCustomerID());
            stm.setString(2, m.getBrand());
            stm.setString(3, m.getModel());
            stm.setString(4, m.getMotoID());
            return stm.executeUpdate() > 0;
        } catch (Exception e) {
            System.out.println("UpdateMotorbike: " + e.getMessage());
            return false;
        }
    }

    public boolean DeleteMotorbike(String motoID) {
        try {
            String sql = "DELETE FROM Motorbikes WHERE MotoID = ?";
            stm = connection.prepareStatement(sql);
            stm.setString(1, motoID);
            return stm.executeUpdate() > 0;
        } catch (Exception e) {
            System.out.println("DeleteMotorbike: " + e.getMessage());
            return false;
        }
    }

    public void CheckAndInsertMoto(String motoId, String ownerName, String phone, String brand, String model) {
        try {
            String checkSql = "SELECT 1 FROM Motorbikes WHERE MotoID = ?";
            PreparedStatement checkStm = connection.prepareStatement(checkSql);
            checkStm.setString(1, motoId);
            ResultSet checkRs = checkStm.executeQuery();

            if (!checkRs.next()) {
                int customerId = new CustomerDAO().getOrCreateCustomer(ownerName, phone);
                String insertSql = """
                    INSERT INTO Motorbikes (MotoID, CustomerID, Brand, Model)
                    VALUES (?, ?, ?, ?)
                    """;
                PreparedStatement insertStm = connection.prepareStatement(insertSql);
                insertStm.setString(1, motoId);
                insertStm.setInt(2, customerId);
                insertStm.setString(3, (brand != null && !brand.isEmpty()) ? brand : "Unknown");
                insertStm.setString(4, (model != null && !model.isEmpty()) ? model : "Unknown");
                insertStm.executeUpdate();
            }
        } catch (Exception e) {
            System.out.println("CheckAndInsertMoto: " + e.getMessage());
        }
    }

    private Motorbike mapMotorbike(ResultSet resultSet) throws Exception {
        return new Motorbike(
                resultSet.getString("MotoID"),
                resultSet.getInt("CustomerID"),
                resultSet.getString("Brand"),
                resultSet.getString("Model"),
                resultSet.getString("OwnerName"),
                resultSet.getString("Phone")
        );
    }
}
