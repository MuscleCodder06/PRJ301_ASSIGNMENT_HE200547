package models;

public class TechnicianMonthlyStat {

    private String username;
    private String fullName;
    private int completedOrders;

    public TechnicianMonthlyStat() {
    }

    public TechnicianMonthlyStat(String username, String fullName, int completedOrders) {
        this.username = username;
        this.fullName = fullName;
        this.completedOrders = completedOrders;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public int getCompletedOrders() {
        return completedOrders;
    }

    public void setCompletedOrders(int completedOrders) {
        this.completedOrders = completedOrders;
    }
}
