package ca.concordia.airport;

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
}
