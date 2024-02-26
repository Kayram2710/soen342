package ca.concordia.user;

import ca.concordia.airport.Airport;

public class AirportAdmin extends User{
    public AirportAdmin(String name, String password, Airport airport) {
        super(name, password);
        this.airport = airport;
    }

    private Airport airport;
    
    public void setAirport(Airport airport){
        this.airport = airport;
    }
    
    public Airport getAirport(){
        return this.airport;
    }
}
