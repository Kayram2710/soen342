package ca.concordia.flight;

import java.time.LocalDateTime;

import ca.concordia.airport.Airline;
import ca.concordia.airport.Airport;

public abstract class NonPrivateFlight extends Flight{

    private Airline operator;

    public NonPrivateFlight(String flightNumber, Airport source, Airport destination, LocalDateTime scheduledDepart, LocalDateTime scheduledArriv, LocalDateTime actualDepart, LocalDateTime actualArriv, Airline operator) {
        super(flightNumber, source, destination, scheduledDepart, scheduledArriv, actualDepart, actualArriv);
        setPlane(null);
    }

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

    @Override
    public String toSql(){
        String command = super.toSql()+"'"+operator.getName()+"',"+super.getPlane().getAircraftID()+", null, ";
        return command;
    }

}
