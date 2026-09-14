package models;

import java.util.Date;

public class Customer {

    private int customerID;
    private String fullName;
    private String phone;
    private String address;
    private Date createdDate;
    private boolean active;

    public Customer() {
    }

    public Customer(int customerID, String fullName, String phone, String address, Date createdDate, boolean active) {
        this.customerID = customerID;
        this.fullName = fullName;
        this.phone = phone;
        this.address = address;
        this.createdDate = createdDate;
        this.active = active;
    }

    public int getCustomerID() {
        return customerID;
    }

    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
