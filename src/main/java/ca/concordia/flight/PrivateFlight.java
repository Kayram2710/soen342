package ca.concordia.flight;

import java.time.LocalDateTime;

import ca.concordia.airport.Airport;

public class PrivateFlight extends Flight{

    public PrivateFlight(String flightNumber, Airport source, Airport destination, LocalDateTime scheduledDepart, LocalDateTime scheduledArriv, LocalDateTime actualDepart, LocalDateTime actualArriv) {
        super(flightNumber, source, destination, scheduledDepart, scheduledArriv, actualDepart, actualArriv);
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
        "," + this.getOperator().getLetterCode();
    }

    @Override
    public String toSql(){
        String command = super.toSql()+"null,"+super.getPlane().getAircraftID()+",'"+this.operator.getLetterCode()+"', 1 ,'";
        return command;
    }
}
