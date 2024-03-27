package ca.concordia.UI;

import java.io.IOException;
import java.time.*;
import java.util.ArrayList;

import ca.concordia.App;
import ca.concordia.FlightTracker;
import ca.concordia.airport.Aircraft;
import ca.concordia.airport.Airline;
import ca.concordia.airport.Airport;
import ca.concordia.flight.CargoFlight;
import ca.concordia.flight.CommercialFlight;
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
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

//UI Class
public class UIController {

    //FXML Setup
    @FXML
    private TableView<String[]> table;
    @FXML
    private Label title;
    @FXML
    private TextField usernameLoginField;
    @FXML
    private PasswordField passwordLoginField;
    @FXML
    private Label statusLabel;

    //private variables
    private FlightTracker flightTracker = FlightTracker.Tracker;


    //FXML function to switch pages
    @FXML
    private void switchPage(String fxml) throws IOException {
        App.setRoot(fxml);
    }

    @FXML
    private void loginButtonClicked() {
        String username = usernameLoginField.getText();
        String password = passwordLoginField.getText();

        if (flightTracker.validateLogin(username, password)) {
            // Successful login
            statusLabel.setText("Login successful");
        } else {
            statusLabel.setText("Invalid username or password");
        }
    }

    @FXML
    public void initialize() {

        hardCodeReg();

    }

    private void buildRegisteredTable() {

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

    private void buildGuestTable() {

        table.getColumns().clear();
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
    public void onGuestLoginButton(ActionEvent event) throws IOException{

        switchPage("index");

        onGuestButton(event);
    }

        //Temp event handlers for testing purposes
    public void onGuestButton(ActionEvent event) throws IOException{


        buildGuestTable();
        title.setText("Guest User");
    }

    public void onRegistedButton(ActionEvent event){

        flightTracker.setLoggedUser(new User("test", "test"));
        this.buildRegisteredTable();
        title.setText("Registered User");

    }

    public void onAirportButton(ActionEvent event){

        Temperature temp1 = new Temperature(25, "Celcius");
        City antigua = new City("Antigua", "Guatemala", temp1);
        Airport airport1 = new Airport("Antigua", "ANU", antigua);

        flightTracker.setLoggedUser(new AirportAdmin("test", "test",airport1));
        this.buildRegisteredTable();
        title.setText("Airport User");
    }
    
    
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

        Airport airport2 = new Airport("Antigua", "ANU", antigua);
        Airport airport1 = new Airport("Toronto", "YYZ", toronto);

        Airline airline1 = new Airline("Sunwing");

        Aircraft aircraft1 = new Aircraft(airline1, 0, airport2);
        Aircraft aircraft2 = new Aircraft(airline1, 0, airport2);

        //NOTE: WE NEED TO USE A DIFFERANT DATE PACKAGE
        LocalDateTime date1 = LocalDateTime.of(2024, 3, 30, 12, 30);
        LocalDateTime date2 = LocalDateTime.of(2024, 3, 30, 18, 30);
        LocalDateTime date3 = LocalDateTime.of(2024, 4, 30, 12, 30);
        LocalDateTime date4 = LocalDateTime.of(2024, 4, 30, 18, 30);
        LocalDateTime date5 = LocalDateTime.of(2024, 1, 30, 12, 30);
        LocalDateTime date6 = LocalDateTime.of(2024, 1, 30, 18, 30);

        Flight flight1 = new PrivateFlight("WG117", airport1, airport2, date1, date2, date1, date2, aircraft1);
        Flight flight2 = new CommercialFlight("WG120", airport2, airport1, date3, date4, date3, date4, airline1);
        Flight flight3 = new CargoFlight("WG118", airport2, airport1, date5, date6, date5, date6, airline1);

        User user = new User("Regular", "Test");
        User user1 = new AirportAdmin("AirportAdmin", "Test", airport1);
        User user2 = new AirlineAdmin("AirlineAdmin", "Test", airline1);

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

        //testing
        flightTracker.setLoggedUser(user1);
        System.out.println(flightTracker.registerFlight(flight1));

        flightTracker.setLoggedUser(user2);
        System.out.println(flightTracker.registerFlight(flight2));
        System.out.println(flightTracker.registerFlight(flight3));
    }
    
}
