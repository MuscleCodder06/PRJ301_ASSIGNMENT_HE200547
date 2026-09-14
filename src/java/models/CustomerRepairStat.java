package models;

public class CustomerRepairStat {

    private int customerId;
    private String fullName;
    private String phone;
    private int totalOrders;

    public CustomerRepairStat() {
    }

    public CustomerRepairStat(int customerId, String fullName, String phone, int totalOrders) {
        this.customerId = customerId;
        this.fullName = fullName;
        this.phone = phone;
        this.totalOrders = totalOrders;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
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

    public int getTotalOrders() {
        return totalOrders;
    }

    public void setTotalOrders(int totalOrders) {
        this.totalOrders = totalOrders;
    }
}
