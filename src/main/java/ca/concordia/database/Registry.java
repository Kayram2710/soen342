package ca.concordia.database;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;

import ca.concordia.airport.Aircraft;
import ca.concordia.airport.Airline;
import ca.concordia.airport.Airport;
import ca.concordia.flight.Flight;
import ca.concordia.flight.NonPrivateFlight;
import ca.concordia.flight.PrivateFlight;
import ca.concordia.user.AirlineAdmin;
import ca.concordia.user.AirportAdmin;
import ca.concordia.user.User;

public class Registry {

    ArrayList<Flight> flights;
    ArrayList<Airport> airports;
    ArrayList<Aircraft> aircrafts;
    ArrayList<Airline> airlines;
    ArrayList<User> users;

    public Registry(){
        this.getRegistry();
    }

    public ArrayList<Flight> getFlights() {
        return flights;
    }

    public void setFlights(ArrayList<Flight> flights) {
        this.flights = flights;
    }

    public ArrayList<Airport> getAirports() {
        return airports;
    }

    public void setAirports(ArrayList<Airport> airports) {
        this.airports = airports;
    }

    public ArrayList<Aircraft> getAircrafts() {
        return aircrafts;
    }

    public void setAircrafts(ArrayList<Aircraft> aircrafts) {
        this.aircrafts = aircrafts;
    }

    public ArrayList<Airline> getAirlines() {
        return airlines;
    }

    public void setAirlines(ArrayList<Airline> airlines) {
        this.airlines = airlines;
    }

    public ArrayList<User> getUsers() {
        return users;
    }

    public void setUsers(ArrayList<User> users) {
        this.users = users;
    }

    public Registry(ArrayList<Flight> flights, ArrayList<Airport> airports, ArrayList<Aircraft> aircrafts,
            ArrayList<Airline> airlines, ArrayList<User> users) {
        this.flights = flights;
        this.airports = airports;
        this.aircrafts = aircrafts;
        this.airlines = airlines;
        this.users = users;
    }

    //register flight method
    public boolean registerFlight(Flight newFlight, User loggedInUser){

        //if user an airline admin, call the reserve aircraft function
        if(loggedInUser instanceof AirlineAdmin){

            Airline userAirline = ((AirlineAdmin)loggedInUser).getAirline();

            //if reservation failed then method fails
            if(!userAirline.reserveAircraft(newFlight)){
                return false;
            }
        }

        //check for aiport availabilities
        for(Flight f : this.flights){
            //Same Arrival, Same destination
            if((f.getDestination().getLetteCode().equals(newFlight.getDestination().getLetteCode()) && f.getScheduledArriv() == newFlight.getScheduledArriv())){
                return false;
            }
            //Same Depart, same source
            else if (f.getSource().getLetteCode().equals(newFlight.getSource().getLetteCode()) && f.getScheduledDepart() == newFlight.getScheduledDepart()){
                return false;
            }
        }

        //adds either a private or non private flight based on the user
        if(loggedInUser instanceof AirlineAdmin){
            NonPrivateFlight npFlight = (NonPrivateFlight) newFlight;
            npFlight.setOperator(((AirlineAdmin)loggedInUser).getAirline());
            this.flights.add(npFlight);
        }

        else if(loggedInUser instanceof AirportAdmin){
            PrivateFlight pFlight = (PrivateFlight) newFlight;
            pFlight.setOperator(((AirportAdmin)loggedInUser).getAirport());
            this.flights.add(pFlight);
        }

        //return true if nothing fails
        return true;
    } 

    public void saveRegistry() {
        Gson gson = new Gson();
        try (FileWriter writer = new FileWriter("registry.txt")) {
            gson.toJson(this, writer);
        } catch (JsonIOException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void getRegistry(){
        //TODO
    }

    
}
