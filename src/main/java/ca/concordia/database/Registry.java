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
import ca.concordia.user.User;

public class Registry {
    
    private ArrayList<Flight> flights;
    private ArrayList<Airport> airports;
    private ArrayList<Aircraft> aircrafts;
    private ArrayList<Airline> airlines;
    private ArrayList<User> users;

    public Registry(){}

    //Todo
    /*
    public Registry(Path){
        this.loadRegistry(Path);
    }
    */

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

    public void saveRegistry(){
        Gson gson = new Gson();
        try {
            gson.toJson(this, new  FileWriter("registry.txt"));
        } catch (JsonIOException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadRegistry(){
        //TODO
    }

    
}
