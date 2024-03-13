package ca.concordia.flight;

import java.util.Date;

import ca.concordia.airport.Aircraft;
import ca.concordia.airport.Airport;

public class CargoFlight extends NonPrivateFlight {

    public CargoFlight(String flightNumber, Airport source, Airport destination, Date scheduledDepart, Date scheduledArriv, Date actualDepart, Date actualArriv, Aircraft plane) {
        super(flightNumber, source, destination, scheduledDepart, scheduledArriv, actualDepart, actualArriv);
    }

    @Override
    public String toString(){
        return super.toString()  +
        ", Cargo";
    }

}
