/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dal;

import models.User;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import models.Role;

/**
 *
 * @author ADMIN
 */
public class UserDAO extends DBContext {
    
    PreparedStatement stm;
    ResultSet rs;
    
    public List<User> GetUsers() {
        List<User> list = new ArrayList<>();
        try {
            String sql = """
                    select *
                    from Users u
                    join Roles r on u.RoleID = r.RoleID
                    where u.IsActive = 1
                    """;
            
            stm = connection.prepareStatement(sql);
            rs = stm.executeQuery();
            
            while (rs.next()) {
                Role role = new Role(
                        rs.getInt("RoleID"),
                        rs.getString("RoleName")
                );
                
                User user = new User(
                        rs.getString("Username"),
                        rs.getString("Password"),
                        rs.getString("FullName"),
                        role,
                        rs.getBoolean("IsActive")
                );
                
                    list.add(user);
                
                
            }
        } catch (Exception e) {
            System.out.println("getUsers: " + e.getMessage());
        }
        return list;
    }
    
    public User GetUserByUsername(String username) {
        User user = null;
        try {
            String sql = "select * from Users u join Roles r on u.RoleID = r.RoleID where u.Username = ?";
            stm = connection.prepareStatement(sql);
            stm.setString(1, username);
            rs = stm.executeQuery();
            
            if (rs.next()) {
                user = new User(
                        rs.getString("Username"),
                        rs.getString("Password"),
                        rs.getString("FullName"),
                        new Role(rs.getInt("RoleID"), rs.getString("RoleName")),
                        rs.getBoolean("IsActive")
                );
            }
        } catch (Exception e) {
            System.out.println("getUserByUsername: " + e.getMessage());
        }
        return user;
    }
    
    public User CreateUser(User user) {
        
        User found = GetUserByUsername(user.getUsername());
        if (found != null) {
            return null;
        }
        
        try {
            String sql = """
                    insert into Users(Username, Password, FullName, RoleID, IsActive)
                    values(?,?,?,?,1)
                    """;
            
            stm = connection.prepareStatement(sql);
            stm.setString(1, user.getUsername());
            stm.setString(2, user.getPassword());
            stm.setString(3, user.getFullName());
            stm.setInt(4, user.getRole().getRoleID());
            stm.executeUpdate();
            
        } catch (Exception e) {
            System.out.println("createUser: " + e.getMessage());
        }
        return user;
    }
    
    public User UpdateUser(User user) {
        
        User found = GetUserByUsername(user.getUsername());
        if (found == null) {
            return null;
        }
        
        try {
            String sql = """
                    update Users
                    set Password = ?,
                        FullName = ?,
                        RoleID = ?,
                        IsActive = ?
                    where Username = ?
                    """;
            
            stm = connection.prepareStatement(sql);
            stm.setString(1, user.getPassword());
            stm.setString(2, user.getFullName());
            stm.setInt(3, user.getRole().getRoleID());
            stm.setBoolean(4, user.isIsActive());
            stm.setString(5, user.getUsername());
            stm.executeUpdate();
            
        } catch (Exception e) {
            System.out.println("updateUser: " + e.getMessage());
        }
        return user;
    }
    
    public User DeleteUser(String username) {
        
        User found = GetUserByUsername(username);
        if (found == null) {
            return null;
        }
        
        try {
            String sql = "update Users set IsActive = 0 where Username = ?";
            stm = connection.prepareStatement(sql);
            stm.setString(1, username);
            stm.executeUpdate();
        } catch (Exception e) {
            System.out.println("deleteUser: " + e.getMessage());
        }
        return found;
    }
    
    public List<User> SearchUser(String searchText, int rId) {
        List<User> users = new ArrayList<User>();
        try {
            String strSQL = """
                            select * from Users u join Roles r on u.RoleID = r.RoleID
                            where FullName like ?  
                            AND u.IsActive = 1
                            """;
            if (rId > 0) {
                strSQL += "and r.RoleID = ?";
            }
            stm = connection.prepareCall(strSQL);
            stm.setString(1, "%" + searchText + "%");
            if (rId > 0) {
                stm.setInt(2, rId);
                
            }
            rs = stm.executeQuery();
            while (rs.next()) {                
                users.add(new User(
                        rs.getString("Username"),
                        rs.getString("Password"),
                        rs.getString("FullName"),
                        new Role(rs.getInt("RoleID"), rs.getString("RoleName")),
                        rs.getBoolean("IsActive")
                ));
            }
        } catch (Exception e) {
            System.out.println("Search User: " + e.getMessage());
        }
        return users;
    }
}
