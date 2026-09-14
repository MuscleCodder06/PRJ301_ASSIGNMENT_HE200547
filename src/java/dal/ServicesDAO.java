

package dal;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import models.Service;

public class ServicesDAO extends DBContext {

    PreparedStatement stm;
    ResultSet rs;

    public Service GetServiceById(int id) {
        Service s = null;
        try {
            String sql = "select * from Services where ServiceID = ?";
            stm = connection.prepareCall(sql);
            stm.setInt(1, id);
            rs = stm.executeQuery();
            if (rs.next()) {
                s = new Service(
                        rs.getInt("ServiceID"),
                        rs.getString("ServiceName"),
                        rs.getDouble("Price"),
                        rs.getBoolean("IsActive")
                );
            }
        } catch (Exception e) {
            System.out.println("GetServiceById: " + e.getMessage());
        }
        return s;
    }

    public List<Service> GetServices() {
        List<Service> list = new ArrayList<>();
        try {
            String sql = "select * from Services where IsActive = 1";
            stm = connection.prepareCall(sql);
            rs = stm.executeQuery();
            while (rs.next()) {
                Service s = new Service(
                        rs.getInt("ServiceID"),
                        rs.getString("ServiceName"),
                        rs.getDouble("Price"),
                        rs.getBoolean("IsActive")
                );
                list.add(s);
            }
        } catch (Exception e) {
            System.out.println("GetServices: " + e.getMessage());
        }
        return list;
    }

    public void CreateService(Service s) {
        try {
            String sql = "insert into Services(ServiceName, Price, IsActive) values(?,?,1)";
            stm = connection.prepareCall(sql);
            stm.setString(1, s.getServiceName());
            stm.setDouble(2, s.getPrice());
            stm.execute();
        } catch (Exception e) {
            System.out.println("CreateService: " + e.getMessage());
        }
    }

    public void UpdateService(Service s) {
        try {
            String sql = "update Services set ServiceName=?, Price=?, IsActive=? where ServiceID=?";
            stm = connection.prepareCall(sql);
            stm.setString(1, s.getServiceName());
            stm.setDouble(2, s.getPrice());
            stm.setBoolean(3, s.isIsActive());
            stm.setInt(4, s.getServiceID());
            stm.execute();
        } catch (Exception e) {
            System.out.println("UpdateService: " + e.getMessage());
        }
    }

    public void DeleteService(int id) {
        try {
            String sql = "update Services set IsActive = 0 where ServiceID=?";
            stm = connection.prepareCall(sql);
            stm.setInt(1, id);
            stm.execute();
        } catch (Exception e) {
            System.out.println("DeleteService: " + e.getMessage());
        }
    }
    
    
}