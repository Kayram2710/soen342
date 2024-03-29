package ca.concordia.airport;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import ca.concordia.FlightTracker;
import ca.concordia.flight.Flight;

public class Aircraft {

    private Airline operator;
    private int aircraftID;
    private Airport location;

    public Aircraft(Airline operator, int aircraftID, Airport location) {
        this.operator = operator;
        this.aircraftID = aircraftID;
        this.location = location;
        this.operator.getFleet().add(this);
        sendtoDB();
    }

    private void sendtoDB(){
        String command = this.toSql();
        FlightTracker.Tracker.accessDB().passStatement(command);
    }

    public Airline getOperator() {
        return this.operator;
    }

    public void setOperator(Airline operator) {
        this.operator = operator;
    }

    public int getAircraftID() {
        return this.aircraftID;
    }

    public void setAircraftID(int aircraftID) {
        this.aircraftID = aircraftID;
    }

    public Airport getLocation() {
        return this.location;
    }

    public void setLocation(Airport location) {
        this.location = location;
    }

    public String toSql(){
        String command = "Insert or replace into Aircraft (aircraftID, airportID) values ("+aircraftID+",'"+this.location.getLetterCode()+"');";
        command = command + "Insert or replace into Fleet (aircraftID, airlineName) values ("+aircraftID+",'"+this.operator.getName()+"');";
        return command;
    }

    public boolean checkAvailability(Flight newFlight){
        String command;
        ArrayList<Object> result;

        LocalDateTime scheduledDepart = newFlight.getScheduledDepart();
        LocalDateTime scheduledArriv = newFlight.getScheduledArriv();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");

        //query for all fligths scheduled on aircraft a then return their departure and arrival
        command = "Select scheduledDepart , scheduledArriv  from Flight where aircraftID = "+aircraftID+";";
        result = FlightTracker.Tracker.accessDB().runQuery(command);
        int res_size = result.size()/2;

        //loop through returned querries
        for (int i = 0; i < res_size; i++) {

            LocalDateTime depart =  LocalDateTime.parse(result.get(0+(i*2)).toString(),formatter);
            LocalDateTime arriv =  LocalDateTime.parse(result.get(1+(i*2)).toString(),formatter);

            boolean isDepartInInterval = (scheduledDepart.compareTo(depart) >= 0 && scheduledDepart.compareTo(arriv) <= 0)?true:false;
            boolean isArrivInInterval = (scheduledArriv.compareTo(depart) >= 0 && scheduledArriv.compareTo(arriv) <= 0)?true:false;
            boolean isDepartInIntervalReverse = (depart.compareTo(scheduledDepart) >= 0 && depart.compareTo(scheduledArriv) <= 0)?true:false;
            boolean isArrivInIntervalReverse = (arriv.compareTo(scheduledDepart) >= 0 && arriv.compareTo(scheduledArriv) <= 0)?true:false;

            if( isDepartInInterval || isArrivInInterval || isDepartInIntervalReverse || isArrivInIntervalReverse ){
                //System.out.println("Aircraft is not Available");
                return false;
            }
        }       
        
        newFlight.setPlane(this);
        //System.out.println("Aircraft is Available");
        return true;
    }

}
