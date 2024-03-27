package ca.concordia;

import java.util.ArrayList;

import ca.concordia.airport.Airline;
import ca.concordia.airport.Airport;
import ca.concordia.flight.Flight;
import ca.concordia.flight.NonPrivateFlight;
import ca.concordia.flight.PrivateFlight;
import ca.concordia.location.City;
import ca.concordia.location.Temperature;
import ca.concordia.user.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.PieChart.Data;

public class FlightTracker {
    private User loggedUser;

    public static FlightTracker Tracker = new FlightTracker(null);

    private DatabaseGateway Db;
    String path = "jdbc:sqlite:Database\\\\database.db";

    public FlightTracker(User loggedUser) {
        Db = new DatabaseGateway(path);
        this.loggedUser = loggedUser;
    }

    public DatabaseGateway accessDB(){
        return this.Db;
    }

    public User getLoggedUser() {
        return this.loggedUser;
    }

    public void setLoggedUser(User loggedUser) {
        this.loggedUser = loggedUser;
    }

    public void loggout(){
        this.loggedUser = null;
    }

    public boolean validateLogin(String username, String password) {

        //verify login info

        String command = "SELECT * FROM User U where login=='"+username+"'";
        ArrayList<Object> result = Db.runQuery(command);

        System.out.println(result.toString());

        if(result.size()==0){
            return false; //Invalid Username
        }

        if(!result.get(1).equals(password)){
            return false; //Invalid Password
        }

        //log in user
        int discriminator = Integer.parseInt(result.get(2).toString());

        User newUser;

        switch (discriminator) {
            case 1:
                newUser = new User(username, password);
                break;
            case 2:
                newUser = new SysAdmin(username, password);
                break;
                
            case 3:
                command = "SELECT * From Airport A , City C WHERE A.letterCode = '"+result.get(3)+"' and A.locationID = C.name";
                result = Db.runQuery(command);
                Temperature temp = new Temperature(Double.parseDouble(result.get(5).toString()),result.get(6).toString());
                City city = new City(result.get(3).toString(), result.get(4).toString(), temp);
                newUser = new AirportAdmin(username, password, new Airport(result.get(1).toString(), result.get(0).toString(), city));
                System.out.println(((AirportAdmin)newUser).getAirport().toString());
                break;
            case 4:
                command = "SELECT * From Airline A WHERE A.name = '"+ result.get(4)+"'";
                result = Db.runQuery(command);
                newUser = new AirlineAdmin(username, password,new Airline(result.get(0).toString()));
                break;
            default:
                newUser = new User(username, password);
                break;
        }

        loggedUser = newUser;

        System.out.println("Succefully logged in as:"+loggedUser.getClass().toString());

        return true;
    }

    public boolean registerFlight(Flight newFlight){
        String command;
        ArrayList<Object> result;

        //if user an airline admin, call the reserve aircraft function
        if(this.loggedUser instanceof AirlineAdmin){

            Airline userAirline = ((AirlineAdmin)loggedUser).getAirline();

            //if reservation failed then method fails
            if(userAirline.reserveAircraft(newFlight)==null){
                return false;
            }
        }

        //check for airport availabilities
        command = "SELECT destinationID, scheduledArriv FROM Flight where destinationID ='"+newFlight.getDestination().getLetterCode()+"' and scheduledArriv ='"+newFlight.getScheduledArriv().toString()+"';";
        result = Db.runQuery(command);

        if(result.size() > 0 ){
            return false;
        }

        command = "SELECT sourceID, scheduledDepart FROM Flight where sourceID ='"+newFlight.getSource().getLetterCode()+"' and scheduledDepart ='"+newFlight.getScheduledDepart().toString()+"';";
        result = Db.runQuery(command);

        if(result.size() > 0 ){
            return false;
        }

        //adds either a private or non private flight based on the user
        if(newFlight instanceof NonPrivateFlight){
            ((NonPrivateFlight)newFlight).setOperator(((AirlineAdmin)loggedUser).getAirline());
            //command += ((AirlineAdmin)loggedUser).getAirline().getName()+"','"+loggedUser.getName()+"');";
        }
        else if(loggedUser instanceof AirportAdmin){
            ((PrivateFlight)newFlight).setOperator(((AirportAdmin)loggedUser).getAirport());
            //command += ((AirportAdmin)loggedUser).getAirport().getLetterCode()+"','"+loggedUser.getName()+"');";
        }

        command = newFlight.toSql()+loggedUser.getName()+"');";

        Db.passStatement(command);

        //return true if nothing fails
        return true;
    }

    //get flights method
    public ObservableList<String[]> getFlights() {
        ObservableList<String[]> data = FXCollections.observableArrayList();
        
        /*
        String command;


        if(this.loggedUser == null){
            command = "SELECT *  ";
        } 

        if(this.loggedUser == null){
            for (Flight f : registry.getFlights()) {
                if (f instanceof NonPrivateFlight) {
                    data.add(new String[] { f.getFlightNumber(), f.getSource().getLetterCode(),
                            f.getDestination().getLetterCode() });
                }
            }
        }else{
            if(loggedUser instanceof AirportAdmin){
                for (Flight f : registry.getFlights()) {
                    if (f instanceof PrivateFlight && ((PrivateFlight)f).getSource().getName().equals(((AirportAdmin)loggedUser).getAirport().getName())) {
                        data.add(f.toString().split(","));
                    }
                }    
            }
            for (Flight f : registry.getFlights()) {
                if (f instanceof NonPrivateFlight) {
                    data.add(f.toString().split(","));
                }
            }    
        } */
        return data;
    }
}
