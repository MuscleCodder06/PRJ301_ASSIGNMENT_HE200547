/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import models.UserProfile;
/**
 *
 * @author ADMIN
 */
public class UserProfilesDAO extends DBContext {

    PreparedStatement stm;
    ResultSet rs;

    public UserProfile GetUserProfileByUsername(String username) {
        UserProfile profile = null;
        try {
            String strSQL = "select * from UserProfiles where Username = ?";
            stm = connection.prepareCall(strSQL);
            stm.setString(1, username);
            rs = stm.executeQuery();

            while (rs.next()) {
                String user = rs.getString("Username");
                String fullName = rs.getString("FullName");
                String email = rs.getString("Email");
                String phone = rs.getString("Phone");
                String address = rs.getString("Address");
                java.util.Date dob = rs.getDate("DateOfBirth");
                String gender = rs.getString("Gender");

                profile = new UserProfile(user, fullName, email, phone, address, dob, gender);
            }

        } catch (Exception ex) {
            System.out.println("GetUserProfileByUsername: " + ex.getMessage());
        }
        return profile;
    }

    public UserProfile CreateUserProfile(UserProfile profile) {

        UserProfile found = GetUserProfileByUsername(profile.getUsername());
        if (found != null) return null;

        try {
            String strSQL = "insert into UserProfiles "
                    + "(Username, FullName, Email, Phone, Address, DateOfBirth, Gender) "
                    + "values (?, ?, ?, ?, ?, ?, ?)";

            stm = connection.prepareCall(strSQL);

            stm.setString(1, profile.getUsername());
            stm.setString(2, profile.getFullName());
            stm.setString(3, profile.getEmail());
            stm.setString(4, profile.getPhone());
            stm.setString(5, profile.getAddress());
            stm.setDate(6, new java.sql.Date(profile.getDateOfBirth().getTime()));
            stm.setString(7, profile.getGender());
            stm.execute();

        } catch (Exception ex) {
            System.out.println("CreateUserProfile: " + ex.getMessage());
        }

        return profile;
    }

    public UserProfile UpdateUserProfile(UserProfile profile) {

        UserProfile found = GetUserProfileByUsername(profile.getUsername());
        if (found == null) return null;

        try {

            String strSQL = "update UserProfiles "
                    + "set FullName = ?, "
                    + "Email = ?, "
                    + "Phone = ?, "
                    + "Address = ?, "
                    + "DateOfBirth = ?, "
                    + "Gender = ? "
                    + "where Username = ?";

            stm = connection.prepareCall(strSQL);

            stm.setString(1, profile.getFullName());
            stm.setString(2, profile.getEmail());
            stm.setString(3, profile.getPhone());
            stm.setString(4, profile.getAddress());
            stm.setDate(5, new java.sql.Date(profile.getDateOfBirth().getTime()));
            stm.setString(6, profile.getGender());
            stm.setString(7, profile.getUsername());

            stm.execute();

        } catch (Exception ex) {
            System.out.println("UpdateUserProfile: " + ex.getMessage());
        }

        return profile;
    }

}
