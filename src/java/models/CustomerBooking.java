package models;

import java.util.Date;

public class CustomerBooking {

    private int bookingID;
    private String fullName;
    private String phone;
    private String motorbike;
    private String problem;
    private String status;
    private String generatedMotoID;
    private Integer confirmedOrderID;
    private Date createdDate;
    private Date confirmedDate;

    public CustomerBooking() {
    }

    public CustomerBooking(String fullName, String phone, String motorbike, String problem) {
        this.fullName = fullName;
        this.phone = phone;
        this.motorbike = motorbike;
        this.problem = problem;
    }

    public CustomerBooking(int bookingID, String fullName, String phone, String motorbike, String problem, String status, String generatedMotoID, Integer confirmedOrderID, Date createdDate, Date confirmedDate) {
        this.bookingID = bookingID;
        this.fullName = fullName;
        this.phone = phone;
        this.motorbike = motorbike;
        this.problem = problem;
        this.status = status;
        this.generatedMotoID = generatedMotoID;
        this.confirmedOrderID = confirmedOrderID;
        this.createdDate = createdDate;
        this.confirmedDate = confirmedDate;
    }

    public int getBookingID() {
        return bookingID;
    }

    public void setBookingID(int bookingID) {
        this.bookingID = bookingID;
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

    public String getMotorbike() {
        return motorbike;
    }

    public void setMotorbike(String motorbike) {
        this.motorbike = motorbike;
    }

    public String getProblem() {
        return problem;
    }

    public void setProblem(String problem) {
        this.problem = problem;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getGeneratedMotoID() {
        return generatedMotoID;
    }

    public void setGeneratedMotoID(String generatedMotoID) {
        this.generatedMotoID = generatedMotoID;
    }

    public Integer getConfirmedOrderID() {
        return confirmedOrderID;
    }

    public void setConfirmedOrderID(Integer confirmedOrderID) {
        this.confirmedOrderID = confirmedOrderID;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getConfirmedDate() {
        return confirmedDate;
    }

    public void setConfirmedDate(Date confirmedDate) {
        this.confirmedDate = confirmedDate;
    }
}
