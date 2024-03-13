package ca.concordia;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import ca.concordia.airport.Aircraft;
import ca.concordia.airport.Airline;
import ca.concordia.airport.Airport;
import ca.concordia.database.Registry;
import ca.concordia.flight.Flight;
import ca.concordia.flight.NonPrivateFlight;
import ca.concordia.flight.PrivateFlight;
import ca.concordia.location.City;
import ca.concordia.location.Temperature;
import ca.concordia.user.AirlineAdmin;
import ca.concordia.user.AirportAdmin;
import ca.concordia.user.User;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

//UI Class
public class UIController {

    //FXML Setup
    @FXML
    private TableView<String[]> table;
    @FXML
    private Label title;

    @FXML
    private void switchToSecondary() throws IOException {
        App.setRoot("secondary");
    }

    //private variables
    private Registry registry;
    private FlightTracker flightTracker;

    //Hardcoded Data
    public void hardCodeReg(){

        //create containers
        ArrayList<Flight> flights = new ArrayList<>();
        ArrayList<Airport> airports = new ArrayList<>();
        ArrayList<Aircraft> aircrafts = new ArrayList<>();
        ArrayList<Airline> airlines = new ArrayList<>();
        ArrayList<User> users = new ArrayList<>();

        //instantiate objects
        Temperature temp1 = new Temperature(25, "Celcius");
        Temperature temp2 = new Temperature(-5, "Celcius");

        City antigua = new City("Antigua", "Guatemala", temp1);
        City toronto = new City("Toronto", "Canada", temp2);

        Airport airport1 = new Airport("Antigua", "ANU", antigua);
        Airport airport2 = new Airport("Toronto", "YYZ", toronto);

        Airline airline1 = new Airline("Sunwing");

        Aircraft aircraft1 = new Aircraft(airline1, 0, airport2);
        Aircraft aircraft2 = new Aircraft(airline1, 0, airport2);

        //NOTE: WE NEED TO USE A DIFFERANT DATE PACKAGE
        Date date1 = new Date(100000000);
        Date date2 = new Date(200000000);
        Date date3 = new Date(300000000);
        Date date4 = new Date(400000000);
        Date date5 = new Date(500000000);
        Date date6 = new Date(600000000);

        Flight flight1 = new PrivateFlight("WG117", airport1, airport2, date1, date2, date1, date2, aircraft1);
        Flight flight2 = new NonPrivateFlight("WG120", airport2, airport1, date3, date4, date3, date4);
        Flight flight3 = new NonPrivateFlight("WG118", airport2, airport1, date5, date6, date5, date6);

        User user = new User("Regular", "Test");
        User user1 = new AirportAdmin("PortGuy", "Test", airport1);
        User user2 = new AirlineAdmin("LineGuy", "Test", airline1);
        
        /* Date Testing
        System.out.println(date1);
        System.out.println(date2);
        System.out.println(date3);
        System.out.println(date4);
        System.out.println(date5);
        System.out.println(date6);
        */

        //fill containers
        airports.add(airport1);
        airports.add(airport2);

        aircrafts.add(aircraft1);
        aircrafts.add(aircraft2);

        airlines.add(airline1);
        
        airline1.addAircraft(aircraft1);
        airline1.addAircraft(aircraft2);

        users.add(user1);
        users.add(user2);
        users.add(user);

        registry = new Registry(flights, airports, aircrafts, airlines, users);
        flightTracker = new FlightTracker(user ,registry);

        //testing
        flightTracker.setLoggedUser(user1);
        System.out.println(flightTracker.registerFlight(flight1));

        flightTracker.setLoggedUser(user2);
        System.out.println(flightTracker.registerFlight(flight2));
        System.out.println(flightTracker.registerFlight(flight3));
    }

    @FXML
    public void initialize() {
        
        hardCodeReg();
        buildGuestTable(registry);

    }

    private void buildRegisteredTable(Registry reg) {

        table.getColumns().clear();
    
        if(table.getColumns().isEmpty()){

            // Initialize Columns
            TableColumn<String[], String> numberColumn = new TableColumn<String[], String>();
            TableColumn<String[], String> sourceColumn = new TableColumn<String[], String>();
            TableColumn<String[], String> destinationColumn = new TableColumn<String[], String>();
            TableColumn<String[], String> scheduledDepartColumn = new TableColumn<String[], String>();
            TableColumn<String[], String> scheduledArrivColumn = new TableColumn<String[], String>();
            TableColumn<String[], String> actualDepartColumn = new TableColumn<String[], String>();
            TableColumn<String[], String> actualArrivColumn = new TableColumn<String[], String>();
            TableColumn<String[], String> planeColumn = new TableColumn<String[], String>();
            TableColumn<String[], String> operatorColumn = new TableColumn<String[], String>();

            numberColumn.setText("Flight Number");
            sourceColumn.setText("Source");
            destinationColumn.setText("Destination");
            scheduledDepartColumn.setText("Scheduled Depart");
            scheduledArrivColumn.setText("Scheduled Arrival");
            actualDepartColumn.setText("Actual Depart");
            actualArrivColumn.setText("Actual Arrival");
            planeColumn.setText("Plane");
            operatorColumn.setText("Operator");

            table.getColumns().addAll(numberColumn, sourceColumn, destinationColumn, scheduledDepartColumn, scheduledArrivColumn, actualDepartColumn, actualArrivColumn, planeColumn, operatorColumn);
            
            // Setting up collumns to read values
            setUpTableCells();
        }
        
        // Add the flights to the table
        ObservableList<String[]> data = flightTracker.getFlights();

        table.setItems(data);
    }

    private void buildGuestTable(Registry reg) {
        table.getColumns().clear();;
        // Initialize Columns
        TableColumn<String[], String> numberColumn = new TableColumn<String[], String>();
        TableColumn<String[], String> sourceColumn = new TableColumn<String[], String>();
        TableColumn<String[], String> destinationColumn = new TableColumn<String[], String>();

        sourceColumn.setText("Source");
        numberColumn.setText("Flight Number");
        destinationColumn.setText("Destination");

        table.getColumns().addAll(numberColumn, sourceColumn, destinationColumn);

        // Setting up collumns to read values
        setUpTableCells();
        
        // Add the flights to the table
        ObservableList<String[]> data = flightTracker.getFlights();

        table.setItems(data);
    }

    private void setUpTableCells() {

        for (int columnIndex = 0; columnIndex < table.getColumns().size(); columnIndex++) {
            
            final int finalColumnIndex = columnIndex;
            TableColumn<String[], String> column = (TableColumn<String[], String>) table.getColumns().get(finalColumnIndex);
            column.setCellValueFactory(features -> {
                String[] row = features.getValue();

                if (row != null && row.length > finalColumnIndex) {
                    return new SimpleStringProperty(row[finalColumnIndex]);
                } else {
                    return new SimpleStringProperty("<no data>");
                }
            });
        }
    }
    //Temp event handlers for testing purposes
    public void onGuestButton(ActionEvent event){

        flightTracker.setLoggedUser(null);
        this.buildGuestTable(registry);
        title.setText("Guest User");
    }

    public void onRegistedButton(ActionEvent event){

        flightTracker.setLoggedUser(new User("test", "test"));
        this.buildRegisteredTable(registry);
        title.setText("Registered User");

    }

    public void onAirportButton(ActionEvent event){

        Temperature temp1 = new Temperature(25, "Celcius");
        City antigua = new City("Antigua", "Guatemala", temp1);
        Airport airport1 = new Airport("Antigua", "ANU", antigua);

        flightTracker.setLoggedUser(new AirportAdmin("test", "test",airport1));
        this.buildRegisteredTable(registry);
        title.setText("Airport User");
    }

}
