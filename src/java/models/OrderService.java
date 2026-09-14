/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models;

/**
 *
 * @author ADMIN
 */
public class OrderService {
    private int orderServiceID;
    private int orderID;
    private int serviceID;

    public OrderService() {}

    public OrderService(int orderServiceID, int orderID, int serviceID) {
        this.orderServiceID = orderServiceID;
        this.orderID = orderID;
        this.serviceID = serviceID;
    }

    public int getOrderServiceID() {
        return orderServiceID;
    }

    public void setOrderServiceID(int orderServiceID) {
        this.orderServiceID = orderServiceID;
    }

    public int getOrderID() {
        return orderID;
    }

    public void setOrderID(int orderID) {
        this.orderID = orderID;
    }

    public int getServiceID() {
        return serviceID;
    }

    public void setServiceID(int serviceID) {
        this.serviceID = serviceID;
    }
}
