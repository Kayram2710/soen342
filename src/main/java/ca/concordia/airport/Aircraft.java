package ca.concordia.airport;

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

}
