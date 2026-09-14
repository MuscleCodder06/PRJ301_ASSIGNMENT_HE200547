/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models;

/**
 *
 * @author ADMIN
 */
public class OrderPart {

    private int orderPartID;
    private int orderID;
    private int partID;
    private int quantity;

    public OrderPart() {
    }

    public OrderPart(int orderPartID, int orderID, int partID, int quantity) {
        this.orderPartID = orderPartID;
        this.orderID = orderID;
        this.partID = partID;
        this.quantity = quantity;
    }

    public int getOrderPartID() {
        return orderPartID;
    }

    public void setOrderPartID(int orderPartID) {
        this.orderPartID = orderPartID;
    }

    public int getOrderID() {
        return orderID;
    }

    public void setOrderID(int orderID) {
        this.orderID = orderID;
    }

    public int getPartID() {
        return partID;
    }

    public void setPartID(int partID) {
        this.partID = partID;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
