package ca.concordia;

import ca.concordia.airport.Airport;
import ca.concordia.database.Registry;
import ca.concordia.flight.Flight;
import ca.concordia.flight.NonPrivateFlight;
import ca.concordia.flight.PrivateFlight;
import ca.concordia.user.AirportAdmin;
import ca.concordia.user.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class FlightTracker {
    private User loggedUser;
    private Registry registry;

    public FlightTracker(User loggedUser) {
        this.loggedUser = loggedUser;
        this.registry = new Registry();
    }

    public FlightTracker(User loggedUser, Registry registry) {
        this.loggedUser = loggedUser;
        this.registry = registry;
    }

    public User getLoggedUser() {
        return this.loggedUser;
    }

    public void setLoggedUser(User loggedUser) {
        this.loggedUser = loggedUser;
    }

    public Registry getRegistry() {
        return this.registry;
    }

    public void setRegistry(Registry registry) {
        this.registry = registry;
    }

    public ObservableList<String[]> getFlights() {
        ObservableList<String[]> data = FXCollections.observableArrayList();
        if(this.loggedUser == null){
            for (Flight f : registry.getFlights()) {
                if (f instanceof NonPrivateFlight) {
                    data.add(new String[] { f.getFlightNumber(), f.getSource().getLetteCode(),
                            f.getDestination().getLetteCode() });
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
        } 
        return data;
    }
}
