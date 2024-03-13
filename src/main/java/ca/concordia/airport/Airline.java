package ca.concordia.airport;
import ca.concordia.flight.Flight;

import java.util.ArrayList;

public class Airline {
    private String name;
    private ArrayList<Aircraft> fleet;

    public Airline(String name) {
        this.name = name;
        fleet = new ArrayList<Aircraft>();
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Aircraft> getFleet() {
        return this.fleet;
    }

    public void setFleet(ArrayList<Aircraft> fleet) {
        this.fleet = fleet;
    }
    
    public void addAircraft(Aircraft aircraft){
        this.fleet.add(aircraft);
    }

    public void removeAircraft(Aircraft aircraft){
        this.fleet.remove(aircraft);
    }

    //reserve aircraft in fleet
    public boolean reserveAircraft(Flight newFlight){

        //for all aircrafts in fleet
        for(Aircraft a: this.fleet){

            //if location match source and is available
            if(a.getLocation().equals(newFlight.getSource()) && !a.getReserved()){

                //complete resercation
                a.setReserved(true);
                newFlight.setPlane(a);
                
                return true;
            }
        }

        //else return false
        return false;
    }

}
