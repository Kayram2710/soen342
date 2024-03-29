package ca.concordia.airport;

import java.util.ArrayList;

import ca.concordia.FlightTracker;
import ca.concordia.flight.Flight;
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

    //reserve aircraft in fleet
    public boolean reserveAircraft(Flight newFlight,ArrayList<Aircraft> aircrafts){

        //for all aircrafts in fleet
        for(Aircraft a: aircrafts){
            //find if aircraft a is at new flight location
            if(a.getLocation().getLetterCode().equals(newFlight.getSource().getLetterCode())){

                if(a.checkAvailability(newFlight)){
                    //confirm reservation
                    System.out.println("Found Free Aircraft");
                    return true;    
                }
            }
        }

        System.out.println("No Free Aircrafts");
        return false;
    }

    public String toSQL(){
        String command = "INSERT OR Replace into Airport (letterCode, locationID, name) values ('"+this.letterCode+"', '"+this.location.getName()+"', '"+this.name+"');";
        return command;
    }
}
