package ca.concordia.flight;

import java.util.Date;

import ca.concordia.airport.Aircraft;
import ca.concordia.airport.Airline;
import ca.concordia.airport.Airport;

public class CargoFlight extends NonPrivateFlight {

    public CargoFlight(String flightNumber, Airport source, Airport destination, Date scheduledDepart, Date scheduledArriv, Date actualDepart, Date actualArriv, Aircraft plane, Airline operator) {
        super(flightNumber, source, destination, scheduledDepart, scheduledArriv, actualDepart, actualArriv, plane, operator);
    }

    @Override
    public String toString(){
        return super.toString()  +
        ", Cargo";
    }

}
