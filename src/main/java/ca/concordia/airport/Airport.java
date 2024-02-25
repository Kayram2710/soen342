package ca.concordia.airport;

import ca.concordia.location.City;

public class Airport {
    private String name;
    private String letteCode;
    private City location;
    
    public Airport(String name, String letteCode, City location) {
        this.name = name;
        this.letteCode = letteCode;
        this.location = location;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLetteCode() {
        return this.letteCode;
    }

    public void setLetteCode(String letteCode) {
        this.letteCode = letteCode;
    }

    public City getLocation() {
        return this.location;
    }

    public void getLocation(City location) {
        this.location = location;
    }
}
