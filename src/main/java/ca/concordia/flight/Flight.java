package ca.concordia.flight;

import java.util.Date;
import ca.concordia.airport.Aircraft;
import ca.concordia.airport.Airport;

public abstract class Flight {
    private String flightNumber;
    private Airport source;
    private Airport destination;
    private Date scheduledDepart;
    private Date scheduledArriv;
    private Date actualDepart;
    private Date actualArriv;
    private Aircraft plane;

    public Flight(String flightNumber, Airport source, Airport destination, Date scheduledDepart, Date scheduledArriv, Date actualDepart, Date actualArriv) {
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

    public Date getScheduledDepart() {
        return this.scheduledDepart;
    }

    public void setScheduledDepart(Date scheduledDepart) {
        this.scheduledDepart = scheduledDepart;
    }

    public Date getScheduledArriv() {
        return this.scheduledArriv;
    }

    public void setScheduledArriv(Date scheduledArriv) {
        this.scheduledArriv = scheduledArriv;
    }

    public Date getActualDepart() {
        return this.actualDepart;
    }

    public void setActualDepart(Date actualDepart) {
        this.actualDepart = actualDepart;
    }

    public Date getActualArriv() {
        return this.actualArriv;
    }

    public void setActualArriv(Date actualArriv) {
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
                getSource().getLetteCode()+ "," +
                getDestination().getLetteCode() + "," +
                getScheduledDepart() + "," +
                getScheduledArriv() + "," +
                getActualDepart() + "," +
                getActualArriv() + "," + 
                ((getPlane() != null)? "N/A":getPlane().getAircraftID());
    }
}
