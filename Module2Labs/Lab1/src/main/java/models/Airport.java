package models;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Airport {
    private List<AirCompany> companies;

    public Airport() {
        this.companies = new ArrayList<>();
    }

    public void addAirCompany(AirCompany company){
        companies.add(company);
    }

    public void addFlight(Flight flight){
        AirCompany c = new AirCompany();
        c.setId(flight.getCompanyId());
        companies.get(companies.indexOf(c)).addFlight(flight);
    }

    public void deleteFlight(Flight flight, AirCompany airCompany){
        companies.get(companies.indexOf(airCompany)).removeFlight(flight);
    }


    public List<AirCompany> getCompanies() {
        return companies;
    }

}
