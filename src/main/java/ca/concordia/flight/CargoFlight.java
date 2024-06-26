package ca.concordia.flight;

import java.time.LocalDateTime;

import ca.concordia.airport.Airport;

public class CargoFlight extends NonPrivateFlight {

    public CargoFlight(String flightNumber, Airport source, Airport destination, LocalDateTime scheduledDepart, LocalDateTime scheduledArriv, LocalDateTime actualDepart, LocalDateTime actualArriv) {
        super(flightNumber, source, destination, scheduledDepart, scheduledArriv, actualDepart, actualArriv);
    }

    @Override
    public String toString(){
        return super.toString()  +
        ", Cargo";
    }

    @Override
    public String toSql(){
        String command = super.toSql()+" 2 ,'";
        return command;
    }

}
