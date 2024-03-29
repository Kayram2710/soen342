package ca.concordia.airport;
import java.util.ArrayList;

import ca.concordia.FlightTracker;
import ca.concordia.flight.Flight;
import ca.concordia.location.City;
import ca.concordia.location.Temperature;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

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

    private void importFleet(){
        fleet.clear();
        String command = "SELECT A.aircraftID, A.airportID FROM Aircraft A JOIN Fleet F ON A.aircraftId = F.aircraftId WHERE F.airlineName = '"+this.name+"'";
        ArrayList<Object> result = FlightTracker.Tracker.accessDB().runQuery(command);
        
        int size = result.size() / 2;
        int index = 0;

        for(int i = 0; i < size ;i++){
            int aircraftID = Integer.parseInt(result.get(index++).toString());
            String letterCode = result.get(index++).toString();

            String command2 = "SELECT * From Airport A , City C WHERE A.letterCode = '"+letterCode+"' and A.locationID = C.name";

            ArrayList<Object> result2 = FlightTracker.Tracker.accessDB().runQuery(command2);
            Temperature temp = new Temperature(Double.parseDouble(result2.get(5).toString()),result2.get(6).toString());
            City city = new City(result2.get(3).toString(), result2.get(4).toString(), temp);
            Airport air = new Airport(result2.get(1).toString(), result2.get(0).toString(), city);

            new Aircraft(this, aircraftID , air);
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

    public ObservableList<String[]> fleetTObservableList(){
        ObservableList<String[]> data = FXCollections.observableArrayList();

        for (Aircraft a : fleet) {
            data.add(new String[] {String.valueOf(a.getAircraftID()),a.getLocation().getLetterCode()});
        }

        return data;

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
            //find if aircraft a is at new flight location
            if(a.getLocation().getLetterCode().equals(newFlight.getSource().getLetterCode())){

                if(a.checkAvailability(newFlight)){
                    //confirm reservation
                    System.out.println("Found Free Aircraft");
                    return true;    
                }
            }
        }

        System.out.println("No Free Aircrafts");
        return false;
    }

    public String toSQL(){
        String command = "Insert or ignore into Airline values ('"+this.name+"');";
        return command;
    }

}
