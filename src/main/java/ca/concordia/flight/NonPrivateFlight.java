package ca.concordia.flight;

import java.util.Date;

import ca.concordia.airport.Aircraft;
import ca.concordia.airport.Airline;
import ca.concordia.airport.Airport;

public abstract class NonPrivateFlight extends Flight{

    public NonPrivateFlight(String flightNumber, Airport source, Airport destination, Date scheduledDepart,
            Date scheduledArriv, Date actualDepart, Date actualArriv, Aircraft plane, Airline operator) {
        super(flightNumber, source, destination, scheduledDepart, scheduledArriv, actualDepart, actualArriv, plane);
        this.operator = operator;
    }

    private Airline operator;

    public Airline getOperator() {
        return this.operator;
    }

    public void setOperator(Airline operator) {
        this.operator = operator;
    }

    @Override
    public String toString(){
        return super.toString()  +
        "," + this.getOperator().getName();
    }

}
