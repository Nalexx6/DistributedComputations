package models;

public class Flight {
    private long id;
    private long companyId;
    private String cityFrom;
    private String cityTo;
    private Integer passengersAmount;

    public Flight(){
        
    }
    
    public Flight(long id, String cityFrom, String cityTo, Integer passengersAmount, long companyId) {
        this.id = id;
        this.cityFrom = cityFrom;
        this.cityTo = cityTo;
        this.passengersAmount = passengersAmount;
        this.companyId = companyId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(long companyId) {
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
