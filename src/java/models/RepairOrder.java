package models;

import java.util.Date;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author ADMIN
 */
public class RepairOrder {
    private int orderID;
    private String motoID;
    private String createdBy;
    private String technicianUsername;
    private String description;
    private String status;
    private Date createdDate;

    public RepairOrder() {
    }

    public RepairOrder(int orderID, String motoID, String createdBy, String technicianUsername, String description, String status, Date createdDate) {
        this.orderID = orderID;
        this.motoID = motoID;
        this.createdBy = createdBy;
        this.technicianUsername = technicianUsername;
        this.description = description;
        this.status = status;
        this.createdDate = createdDate;
    }

    public int getOrderID() {
        return orderID;
    }

    public void setOrderID(int orderID) {
        this.orderID = orderID;
    }

    public String getMotoID() {
        return motoID;
    }

    public void setMotoID(String motoID) {
        this.motoID = motoID;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getTechnicianUsername() {
        return technicianUsername;
    }

    public void setTechnicianUsername(String technicianUsername) {
        this.technicianUsername = technicianUsername;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }
    
    
}
