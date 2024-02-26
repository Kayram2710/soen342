package ca.concordia.flight;

import java.util.Date;

import ca.concordia.airport.Aircraft;
import ca.concordia.airport.Airport;

public class PrivateFlight extends Flight{

    public PrivateFlight(String flightNumber, Airport source, Airport destination, Date scheduledDepart,
            Date scheduledArriv, Date actualDepart, Date actualArriv, Aircraft plane, Airport operator) {
        super(flightNumber, source, destination, scheduledDepart, scheduledArriv, actualDepart, actualArriv, plane);
        this.operator = operator;
    }

    private Airport operator;

    public Airport getOperator() {
        return this.operator;
    }

    public void setOperator(Airport operator) {
        this.operator = operator;
    }

    @Override
    public String toString(){
        return super.toString()  +
        "," + this.getOperator().getLetteCode();
    }
}
