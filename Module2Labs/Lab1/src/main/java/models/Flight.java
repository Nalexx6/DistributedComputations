package models;

import java.util.Date;

public class Flight {
    private String id;
    private String companyId;
    private String cityFrom;
    private String cityTo;
    private Integer passengersAmount;

    public Flight(){
        
    }
    
    public Flight(String id, String cityFrom, String cityTo, Integer passengersAmount) {
        this.id = id;
        this.cityFrom = cityFrom;
        this.cityTo = cityTo;
        this.passengersAmount = passengersAmount;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getCityFrom() {
        return cityFrom;
    }

    public void setCityFrom(String cityFrom) {
        this.cityFrom = cityFrom;
    }

    public String getCityTo() {
        return cityTo;
    }

    public void setCityTo(String cityTo) {
        this.cityTo = cityTo;
    }

    public Integer getPassengersAmount() {
        return passengersAmount;
    }

    public void setPassengersAmount(Integer passengersAmount) {
        this.passengersAmount = passengersAmount;
    }

}
