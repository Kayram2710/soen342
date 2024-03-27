package ca.concordia.flight;

import java.time.LocalDateTime;
import ca.concordia.airport.Aircraft;
import ca.concordia.airport.Airport;

public abstract class Flight {
    private String flightNumber;
    private Airport source;
    private Airport destination;
    private LocalDateTime scheduledDepart;
    private LocalDateTime scheduledArriv;
    private LocalDateTime actualDepart;
    private LocalDateTime actualArriv;
    private Aircraft plane;

    public Flight(String flightNumber, Airport source, Airport destination, LocalDateTime scheduledDepart, LocalDateTime scheduledArriv, LocalDateTime actualDepart, LocalDateTime actualArriv) {
        this.flightNumber = flightNumber;
        this.source = source;
        this.destination = destination;
        this.scheduledDepart = scheduledDepart;
        this.scheduledArriv = scheduledArriv;
        this.actualDepart = actualDepart;
        this.actualArriv = actualArriv;
    }

    public String getFlightNumber() {
        return this.flightNumber;
    }

    public void setFlightNumber(String flightNumber) {
        this.flightNumber = flightNumber;
    }

    public Airport getSource() {
        return this.source;
    }

    public void setSource(Airport source) {
        this.source = source;
    }

    public Airport getDestination() {
        return this.destination;
    }

    public void setDestination(Airport destination) {
        this.destination = destination;
    }

    public LocalDateTime getScheduledDepart() {
        return this.scheduledDepart;
    }

    public void setScheduledDepart(LocalDateTime scheduledDepart) {
        this.scheduledDepart = scheduledDepart;
    }

    public LocalDateTime getScheduledArriv() {
        return this.scheduledArriv;
    }

    public void setScheduledArriv(LocalDateTime scheduledArriv) {
        this.scheduledArriv = scheduledArriv;
    }

    public LocalDateTime getActualDepart() {
        return this.actualDepart;
    }

    public void setActualDepart(LocalDateTime actualDepart) {
        this.actualDepart = actualDepart;
    }

    public LocalDateTime getActualArriv() {
        return this.actualArriv;
    }

    public void setActualArriv(LocalDateTime actualArriv) {
        this.actualArriv = actualArriv;
    }

    public Aircraft getPlane() {
        return this.plane;
    }

    public void setPlane(Aircraft plane) {
        this.plane = plane;
    }

    @Override
    public String toString() {
        return getFlightNumber() + "," +
                getSource().getLetterCode()+ "," +
                getDestination().getLetterCode() + "," +
                getScheduledDepart() + "," +
                getScheduledArriv() + "," +
                getActualDepart() + "," +
                getActualArriv() + "," + 
                ((getPlane() != null)? "N/A":getPlane().getAircraftID());
    }

    public String toSql(){
        String command = "INSERT INTO Flight (actualArriv, actualDepart, scheduledArriv, scheduledDepart, sourceID, destinationID, flightNumber, airlineOperating, aircraftID , airportOperating, flightDiscriminator, recordWritter) VALUES(";
        command = command +"'"+actualArriv+"','"+actualDepart+"','"+scheduledArriv+"','"+scheduledDepart+"','"+source.getLetterCode()+"','"+destination.getLetterCode()+"','"+flightNumber+"',";
        return command;
    }
}
