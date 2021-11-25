package models;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AirCompany {
    private String id;
    private String name;
    private List<Flight> flights;

    public AirCompany(){
        flights = new ArrayList<>();
    }
    
    public AirCompany(String id, String name){
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Flight> getFlights() {
        return flights;
    }

    public void addFlight(Flight flight){
        flights.add(flight);
    }

    public void removeFlight(Flight flight){
        flights.remove(flight);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AirCompany that = (AirCompany) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
