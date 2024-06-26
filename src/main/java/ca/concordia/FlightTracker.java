package ca.concordia;

import java.util.ArrayList;

import ca.concordia.airport.Aircraft;
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

@SuppressWarnings("exports")
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
                newUser = new AirportAdmin(username, password, fetchAirport(result.get(3).toString()));
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

    public int registerFlight(Flight newFlight){
        String command;
        ArrayList<Object> result;

        System.out.print("Flight Register Result: ");

        //check for airport availabilities
        command = "SELECT destinationID, scheduledArriv FROM Flight where destinationID ='"+newFlight.getDestination().getLetterCode()+"' and scheduledArriv ='"+newFlight.getScheduledArriv().toString()+"';";
        result = Db.runQuery(command);

        if(result.size() > 0 ){
            System.out.println("Arrival time already taken at arriving Airport.");
            return 1;
        }

        command = "SELECT sourceID, scheduledDepart FROM Flight where sourceID ='"+newFlight.getSource().getLetterCode()+"' and scheduledDepart ='"+newFlight.getScheduledDepart().toString()+"';";
        result = Db.runQuery(command);

        if(result.size() > 0 ){
            System.out.println("Deperature time already taken at departure Airport.");
            return 2;
        }

        //if user an airline admin, call the reserve aircraft function
        if(this.loggedUser instanceof AirlineAdmin){
            Airline userAirline = ((AirlineAdmin)loggedUser).getAirline();

            //if reservation failed then method fails
            if(!userAirline.reserveAircraft(newFlight)){
                return 3;
            }

        }else if(this.loggedUser instanceof AirportAdmin){
            Airport userAirline = ((AirportAdmin)loggedUser).getAirport();

            //if reservation failed then method fails
            if(!userAirline.reserveAircraft(newFlight,fetchAllAircrafts())){
                return 3;
            }

        }

        //adds either a private or non private flight based on the user
        if(newFlight instanceof NonPrivateFlight){
            ((NonPrivateFlight)newFlight).setOperator(((AirlineAdmin)loggedUser).getAirline());
        }
        else if(loggedUser instanceof AirportAdmin){
            ((PrivateFlight)newFlight).setOperator(((AirportAdmin)loggedUser).getAirport());
        }

        command = newFlight.toSql()+loggedUser.getName()+"');";

        try {
            Db.unmannagedPass(command);
        } catch (Exception e) {
            System.out.println("Flight Number Already in use.");
            return 4;
        }

        //return 0 if nothing fails
        System.out.println("Succefully Added Flight.");
        return 0;
    }

    //get flights method
    public ObservableList<String[]> getFlights() {
        ObservableList<String[]> data = FXCollections.observableArrayList();
        
        String command = "SELECT flightNumber, sourceID, destinationID from Flight where flightDiscriminator <> 1";
        int quotient = 12;
        ArrayList<Object> result;

        if(loggedUser == null){
            quotient = 3;
        } 
        else if((loggedUser instanceof AirlineAdmin) || (loggedUser instanceof SysAdmin)){
            command = "SELECT * from Flight where flightDiscriminator <> 1";
        }
        else if(loggedUser instanceof AirportAdmin){
            String code = ((AirportAdmin)loggedUser).getAirport().getLetterCode();
            command = "SELECT * from Flight where flightDiscriminator <> 1 union Select * from Flight where flightDiscriminator == 1 and (sourceID == '"+code+"' or destinationID=='"+code+"') ";
        } else{
            command = "SELECT * from Flight where flightDiscriminator <> 1";
        }

        result = Db.runQuery(command);
        int res_size = result.size()/quotient;

        for (int i = 0; i < res_size; i++) {
            if(quotient == 3){
                data.add(new String[] { result.get(0+(i*3)).toString(), result.get(1+(i*3)).toString(), result.get(2+(i*3)).toString()});
            }else if(quotient == 12){
                data.add(new String[] { result.get(0+(i*12)).toString(), result.get(1+(i*12)).toString(), result.get(2+(i*12)).toString(), result.get(8+(i*12)).toString(), result.get(9+(i*12)).toString(), result.get(10+(i*12)).toString(), result.get(11+(i*12)).toString(), result.get(3+(i*12)).toString(),
                    result.get((result.get((7+(i*12))).toString().equals("1"))?(5+(i*12)):(4+(i*12))).toString()});
            }
        }

        return data;
    }

    public Airport fetchAirport(String letterCode){
        Airport airport;
        String command = "SELECT * From Airport A , City C WHERE A.locationID = C.name and A.letterCode = '"+letterCode+"'";
        ArrayList<Object>  result = Db.runQuery(command);

        Temperature temp = new Temperature(Double.parseDouble(result.get(5).toString()),result.get(6).toString());
        City city = new City(result.get(3).toString(), result.get(4).toString(), temp);
        airport = new Airport(result.get(2).toString(), result.get(0).toString(), city);
        
        return airport;

    }

    public ArrayList<Airport> fetchAllAirports(){

        ArrayList<Airport> airports = new ArrayList<Airport>();
        String command = "SELECT * From Airport A , City C WHERE A.locationID = C.name";
        ArrayList<Object>  result = Db.runQuery(command);

        int result_size = result.size()/7;

        for (int i = 0; i < result_size; i++) {
            Temperature temp = new Temperature(Double.parseDouble(result.get(5+(i*7)).toString()),result.get(6+(i*7)).toString());
            City city = new City(result.get(3+(i*7)).toString(), result.get(4+(i*7)).toString(), temp);
            airports.add(new Airport(result.get(2+(i*7)).toString(), result.get(0+(i*7)).toString(), city));
        }

        return airports;
    }

    public ArrayList<Airline> fetchAllAirlines(){
        ArrayList<Airline> airlines = new ArrayList<Airline>();
        String command = "SELECT * From Airline";
        ArrayList<Object>  result = Db.runQuery(command);

        for (Object o : result) {

            airlines.add(new Airline(o.toString()));
            
        }

        return airlines;
    }

    public ArrayList<Aircraft> fetchAllAircrafts(){

        ArrayList<Aircraft> aircrafts = new ArrayList<Aircraft>();
        ArrayList<Airline> airlines = fetchAllAirlines();

        for (Airline airline : airlines) {
            ArrayList<Aircraft> curr_fleet = airline.getFleet();
            for (Aircraft aircraft : curr_fleet) {
                aircrafts.add(aircraft);
            }
        }
        
        return aircrafts;

    }

    public ArrayList<City> fetchAllCities(){

        ArrayList<City> cities = new ArrayList<City>();
        String command = "SELECT * From City";
        ArrayList<Object>  result = Db.runQuery(command);

        int res_size = result.size()/4; 

        for (int i = 0; i < res_size; i++) {
            cities.add(new City(result.get(0+(i*4)).toString(), result.get(1+(i*4)).toString(), new Temperature(Double.parseDouble(result.get(2+(i*4)).toString()), result.get(3+(i*4)).toString())));
        }
        return cities;

    }
}
