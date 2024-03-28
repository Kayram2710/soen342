package ca.concordia.airport;

import ca.concordia.FlightTracker;

public class Aircraft {

    private Airline operator;
    private int aircraftID;
    private Airport location;
    private boolean reserved;

    public Aircraft(Airline operator, int aircraftID, Airport location) {
        this.operator = operator;
        this.aircraftID = aircraftID;
        this.location = location;
        this.reserved = false;
        this.operator.getFleet().add(this);
        sendtoDB();
    }

    public Aircraft(Airline operator, int aircraftID, Airport location, boolean reserved) {
        this.operator = operator;
        this.aircraftID = aircraftID;
        this.location = location;
        this.reserved = reserved;
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

    public boolean getReserved() {
        return this.reserved;
    }

    public void setReserved(Boolean reserved) {
        this.reserved = reserved;
    }

    public String toSql(){
        String command = "Insert or replace into Aircraft (aircraftID, airportID, reserved) values ("+aircraftID+",'"+this.location.getLetterCode()+"', "+getReserved()+");";
        command = command + "Insert or replace into Fleet (aircraftID, airlineName) values ("+aircraftID+",'"+this.operator.getName()+"');";
        return command;
    }

}
