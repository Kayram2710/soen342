package ca.concordia.airport;

import ca.concordia.FlightTracker;
import ca.concordia.location.City;

public class Airport {
    private String name;
    private String letterCode;
    private City location;
    
    public Airport(String name, String letterCode, City location) {
        this.name = name;
        this.letterCode = letterCode;
        this.location = location;
        sendtoDB();
    }

    private void sendtoDB(){
        String command = this.toSQL();
        FlightTracker.Tracker.accessDB().passStatement(command);
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
        String command = "INSERT OR IGNORE into Airport (letterCode, locationID, name) values ('"+this.letterCode+"', '"+this.location.getName()+"', '"+this.name+"');";
        return command;
    }
}
