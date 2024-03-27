package ca.concordia.airport;

import ca.concordia.location.City;

public class Airport {
    private String name;
    private String letterCode;
    private City location;
    
    public Airport(String name, String letterCode, City location) {
        this.name = name;
        this.letterCode = letterCode;
        this.location = location;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLetterCode() {
        return this.letterCode;
    }

    public void setLetterCode(String letteCode) {
        this.letterCode = letteCode;
    }

    public City getLocation() {
        return this.location;
    }

    public void getLocation(City location) {
        this.location = location;
    }

    public String toSQL(){
        String command = "Insert into Airport (letterCode, locationID, name) values ('"+this.letterCode+"', '"+this.location.getName()+"', '"+this.name+"');";
        return command;
    }
}
