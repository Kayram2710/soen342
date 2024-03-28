package ca.concordia.airport;
import ca.concordia.FlightTracker;
import ca.concordia.flight.Flight;
import ca.concordia.location.City;
import ca.concordia.location.Temperature;

import java.util.ArrayList;

public class Airline {
    private String name;
    private ArrayList<Aircraft> fleet;

    public Airline(String name) {
        this.name = name;
        fleet = new ArrayList<Aircraft>();
        importFleet();
        sendtoDB();
    }

    private void sendtoDB(){
        String command = this.toSQL();
        FlightTracker.Tracker.accessDB().passStatement(command);
    }

    public void importFleet(){
        fleet.clear();
        String command = "SELECT A.aircraftID, A.airportID, A.reserved FROM Aircraft A JOIN Fleet F ON A.aircraftId = F.aircraftId WHERE F.airlineName = '"+this.name+"'";
        ArrayList<Object> result = FlightTracker.Tracker.accessDB().runQuery(command);
        
        int size = result.size() / 3;
        int index = 0;

        for(int i = 0; i < size ;i++){
            System.out.println("AA");
            int aircraftID = Integer.parseInt(result.get(index++).toString());
            String letterCode = result.get(index++).toString();
            boolean reserved = ((result.get(index++).toString()).equals("1"))?true:false;

            String command2 = "SELECT * From Airport A , City C WHERE A.letterCode = '"+letterCode+"' and A.locationID = C.name";

            ArrayList<Object> result2 = FlightTracker.Tracker.accessDB().runQuery(command2);
            Temperature temp = new Temperature(Double.parseDouble(result2.get(5).toString()),result2.get(6).toString());
            City city = new City(result2.get(3).toString(), result2.get(4).toString(), temp);
            Airport air = new Airport(result2.get(1).toString(), result2.get(0).toString(), city);

            new Aircraft(this, aircraftID , air, reserved);
            //System.out.println(aircraftID+" "+reserved);
        }

        System.out.println("FLEET SIZE: " + fleet.size());
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

        String command;

        // String command = "SELECT * From Aircraft where (airportID == '"+newFlight.getSource().getLetterCode()+"' and reserved == false)";
        // ArrayList<Object> result = FlightTracker.Tracker.accessDB().runQuery(command);


       // System.out.println(result.toString());


        // if(result.size() > 1){

        //     //

        // }

        //for all aircrafts in fleet
        for(Aircraft a: this.fleet){

            //System.out.println(a.getAircraftID());
           // System.out.println(a.getAircraftID()+" "+a.getReserved());

            //if location match source and is available
            if(a.getLocation().equals(newFlight.getSource()) && !a.getReserved()){


                //complete reservation
                a.setReserved(true);

                command = "UPDATE Aircraft SET reserved = true where (airportID == '"+a.getLocation().getLetterCode()+"' and reserved == false)";
                FlightTracker.Tracker.accessDB().passStatement(command);

                newFlight.setPlane(a);
                
                return a;
            }
        }

        //else return false
        return null;
    }

    public String toSQL(){
        String command = "Insert or ignore into Airline values ('"+this.name+"');";
        return command;
    }

}
