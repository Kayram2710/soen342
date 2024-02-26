package ca.concordia.user;

import ca.concordia.airport.Airline;

public class AirlineAdmin extends User{
    public AirlineAdmin(String name, String password, Airline airline) {
        super(name, password);
        this.airline = airline;
    }

    private Airline airline;
    
    public void setAirline(Airline airline){
        this.airline = airline;
    }
    
    public Airline getAirline(){
        return this.airline;
    }
}
