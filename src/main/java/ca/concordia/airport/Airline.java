package ca.concordia.airport;
import ca.concordia.FlightTracker;
import ca.concordia.flight.Flight;

import java.util.ArrayList;

public class Airline {
    private String name;
    private ArrayList<Aircraft> fleet;

    public Airline(String name) {
        this.name = name;
        fleet = new ArrayList<Aircraft>();
        setFleet();
    }

    public void setFleet(){
        String command = "SELECT A.aircraftID , A.reserved, A.airportID FROM Aircraft A JOIN Fleet F ON A.aircraftId = F.aircraftId WHERE F.airlineName = '"+this.name+"'";
        ArrayList<Object> result = FlightTracker.Tracker.accessDB().runQuery(command);
        int size = result.size() / 3;
        int index = 0;


        for(int i = 0; i < size ;i++){

            //System.out.println(result.get(index++));
            //System.out.println(result.get(index++));
            //System.out.println(result.get(index++));
            //System.out.println(result.get(index++));
            //fleet.add(new Aircraft(null, i, null));

        }
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
    public Aircraft reserveAircraft(Flight newFlight){

        //for all aircrafts in fleet
        for(Aircraft a: this.fleet){

            //if location match source and is available
            if(a.getLocation().equals(newFlight.getSource()) && !a.getReserved()){

                //complete resercation
                a.setReserved(true);
                newFlight.setPlane(a);
                
                return a;
            }
        }

        //else return false
        return null;
    }

    public String toSQL(){
        String command = "Insert into Airline values ('"+this.name+"');";
        return command;
    }

}
