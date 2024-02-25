package ca.concordia.airport;

public class Aircraft {
    private Airline operator;
    private int aircraftID;

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

    public Aircraft(Airline operator, int aircraftID) {
        this.operator = operator;
        this.aircraftID = aircraftID;
    }

}
