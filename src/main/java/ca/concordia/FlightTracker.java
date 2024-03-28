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
                //System.out.println(((AirportAdmin)newUser).getAirport().toString());
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

        //if user an airline admin, call the reserve aircraft function
        if(this.loggedUser instanceof AirlineAdmin){

            Airline userAirline = ((AirlineAdmin)loggedUser).getAirline();

            //if reservation failed then method fails
            if(userAirline.reserveAircraft(newFlight)==null){
                return false;
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

        Db.passStatement(command);

        //return true if nothing fails
        return true;
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
}
