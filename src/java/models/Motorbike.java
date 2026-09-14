/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models;

/**
 *
 * @author ADMIN
 */
public class Motorbike {

    private String motoID;
    private int customerID;
    private String brand;
    private String model;
    private String ownerName;
    private String phone;

    public Motorbike(String motoID, String brand, String model, String ownerName, String phone) {
        this.motoID = motoID;
        this.customerID = 0;
        this.brand = brand;
        this.model = model;
        this.ownerName = ownerName;
        this.phone = phone;
    }

    public Motorbike(String motoID, int customerID, String brand, String model, String ownerName, String phone) {
        this.motoID = motoID;
        this.customerID = customerID;
        this.brand = brand;
        this.model = model;
        this.ownerName = ownerName;
        this.phone = phone;
    }

    public Motorbike() {
    }

    public String getMotoID() {
        return motoID;
    }

    public String getBrand() {
        return brand;
    }

    public int getCustomerID() {
        return customerID;
    }

    public String getModel() {
        return model;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public String getPhone() {
        return phone;
    }

    public void setMotoID(String motoID) {
        this.motoID = motoID;
    }

    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
