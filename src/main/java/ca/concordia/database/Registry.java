package ca.concordia.database;

import java.util.ArrayList;

import ca.concordia.*;
import ca.concordia.airport.*;
import ca.concordia.flight.*;
import ca.concordia.user.*;
import ca.concordia.location.*;

public class Registry {

    private ArrayList<Flight> flights;
    private ArrayList<Airport> airports;
    private ArrayList<Aircraft> aircrafts;
    private ArrayList<Airline> airlines;

    private DatabaseGateway Db;
    String path = "jdbc:sqlite:Database\\\\database.db";

    public Registry(){
        Db = new DatabaseGateway(path);
    }

    public Registry(ArrayList<Flight> flights, ArrayList<Airport> airports, ArrayList<Aircraft> aircrafts, ArrayList<Airline> airlines, ArrayList<User> users) {
        this.flights = flights;
        this.airports = airports;
        this.aircrafts = aircrafts;
        this.airlines = airlines;
        Db = new DatabaseGateway(path);
    }

    public DatabaseGateway accessDB(){
        return Db;
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

    public void addFlight(String command){
        Db.passStatement(command);
    }

    
}
