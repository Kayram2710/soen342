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
    
}
