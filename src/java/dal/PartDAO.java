package dal;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import models.Part;

public class PartDAO extends DBContext {

    PreparedStatement stm;
    ResultSet rs;

   
    public Part GetPartById(int id) {
        Part p = null;
        try {
            String strSQL = "select * from Parts where PartID = ?";
            stm = connection.prepareCall(strSQL);
            stm.setInt(1, id);
            rs = stm.executeQuery();

            if (rs.next()) {
                p = new Part(
                        rs.getInt("PartID"),
                        rs.getString("PartName"),
                        rs.getDouble("Price"),
                        rs.getInt("Quantity"),
                        rs.getString("Description"),
                        rs.getBoolean("IsActive")
                );
            }
        } catch (Exception e) {
            System.out.println("GetPartById: " + e.getMessage());
        }
        return p;
    }

    public List<Part> GetParts() {
        List<Part> list = new ArrayList<>();
        try {
            String strSQL = "select * from Parts where IsActive = 1";
            stm = connection.prepareCall(strSQL);
            rs = stm.executeQuery();

            while (rs.next()) {
                Part p = new Part(
                        rs.getInt("PartID"),
                        rs.getString("PartName"),
                        rs.getDouble("Price"),
                        rs.getInt("Quantity"),
                        rs.getString("Description"),
                        rs.getBoolean("IsActive")
                );
                list.add(p);
            }
        } catch (Exception e) {
            System.out.println("GetParts: " + e.getMessage());
        }
        return list;
    }

    public Part CreatePart(Part p) {
        try {
            String strSQL = """
                    insert into Parts(PartName, Price, Quantity, Description, IsActive)
                    values(?,?,?,?,1)
                    """;
            stm = connection.prepareCall(strSQL);
            stm.setString(1, p.getPartName());
            stm.setDouble(2, p.getPrice());
            stm.setInt(3, p.getQuantity());
            stm.setString(4, p.getDescription());
            stm.execute();
        } catch (Exception e) {
            System.out.println("CreatePart: " + e.getMessage());
        }
        return p;
    }

    public Part UpdatePart(Part p) {

        Part found = GetPartById(p.getPartID());
        if (found == null) {
            return null;
        }

        try {
            String strSQL = """
                    update Parts
                    set PartName = ?,
                        Price = ?,
                        Quantity = ?,
                        Description = ?,
                        IsActive = ?
                    where PartID = ?
                    """;

            stm = connection.prepareCall(strSQL);
            stm.setString(1, p.getPartName());
            stm.setDouble(2, p.getPrice());
            stm.setInt(3, p.getQuantity());
            stm.setString(4, p.getDescription());
            stm.setBoolean(5, p.isIsActive());
            stm.setInt(6, p.getPartID());
            stm.execute();

        } catch (Exception e) {
            System.out.println("UpdatePart: " + e.getMessage());
        }

        return p;
    }

    public Part DeletePart(int id) {

        Part found = GetPartById(id);
        if (found == null) {
            return null;
        }

        try {
            String strSQL = "update Parts set IsActive = 0 where PartID = ?";
            stm = connection.prepareCall(strSQL);
            stm.setInt(1, id);
            stm.execute();
        } catch (Exception e) {
            System.out.println("DeletePart: " + e.getMessage());
        }

        return found;
    }

    public List<Part> searchPartByName(String name) {

    List<Part> list = new ArrayList<>();

    String sql = "SELECT * FROM Parts WHERE PartName LIKE ? AND IsActive = 1";

    try {

        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setString(1, "%" + name + "%");

        ResultSet rs = ps.executeQuery();

        while (rs.next()) {

            Part p = new Part(
                    rs.getInt("PartID"),
                    rs.getString("PartName"),
                    rs.getDouble("Price"),
                    rs.getInt("Quantity"),
                    rs.getString("Description"),
                    rs.getBoolean("IsActive")
            );

            list.add(p);
        }

    } catch (Exception e) {
        System.out.println("Search by Parts Name: "+e.getMessage());
    }

    return list;
}
    
    public boolean CheckStock(int partId, int quantity) {
        try {
            String strSQL = "select Quantity from Parts where PartID = ? and IsActive = 1";
            stm = connection.prepareCall(strSQL);
            stm.setInt(1, partId);
            rs = stm.executeQuery();

            if (rs.next()) {
                return rs.getInt("Quantity") >= quantity;
            }
        } catch (Exception e) {
            System.out.println("CheckStock: " + e.getMessage());
        }
        return false;
    }

    public void DeductStock(int partId, int quantity) {
        try {
            String strSQL = """
                    update Parts
                    set Quantity = Quantity - ?
                    where PartID = ? and Quantity >= ?
                    """;

            stm = connection.prepareCall(strSQL);
            stm.setInt(1, quantity);
            stm.setInt(2, partId);
            stm.setInt(3, quantity);
            stm.execute();

        } catch (Exception e) {
            System.out.println("DeductStock: " + e.getMessage());
        }
    }
}
