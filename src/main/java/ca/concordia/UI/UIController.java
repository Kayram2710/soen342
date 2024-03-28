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
import ca.concordia.user.SysAdmin;
import ca.concordia.user.User;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
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
    @FXML
    private ComboBox<String> userTypesBox;

    private final ObservableList<String> options = 
    FXCollections.observableArrayList(
        "User",
        "Airline Admin",
        "Airport Admin",
        "System Admin"
    );

    private static String view ="";
    //private variables
    private FlightTracker flightTracker = FlightTracker.Tracker;


    //FXML function to switch pages
    @FXML
    private void switchPage(String fxml) throws IOException {
        App.setRoot(fxml);
    }

    @FXML
    public void initialize() {

        // Airline air = new Airline("Sunwing");
        // Temperature temp = new Temperature(25,"Celcius");
        // Temperature temp2 = new Temperature(33,"Celcius");
        // City city = new City("Veraguas", "Panama", temp);
        // City city2 = new City("Panama City", "Panama", temp2);
        // Airport airp = new Airport("Veraguas Aiport", "VEA", city);
        // Airport airp2 = new Airport("Panama Aiport", "PTY", city2);

        //  Aircraft cra1 = new Aircraft(air, 8, airp);
        //  Aircraft cra2 = new Aircraft(air, 9, airp);
        //  Aircraft cra3 = new Aircraft(air, 10, airp2);

        // LocalDateTime date1 = LocalDateTime.of(2024, 5, 8, 18, 10, 0);
        // LocalDateTime date2 = LocalDateTime.of(2024, 5, 8, 21, 25, 0);
        // Flight newf = new CommercialFlight("WW390", airp, airp2, date1, date2, date1, date2, air);

        // flightTracker.setLoggedUser(new AirlineAdmin("AirlineAdmin", "pass", air));
        // flightTracker.registerFlight(newf);

        // System.out.println(air.getFleet().size());
        // for(Aircraft f : air.getFleet()){
        //     System.out.println(f.getAircraftID() + "  " + f.getReserved());
        // }


        ArrayList<Aircraft> a = flightTracker.fetchAllAvailableAircrafts();
        for (Aircraft aircraft : a) {
            System.out.println(aircraft.toSql());
        }
        

        System.out.println(view);
        switch(view){
            case "guest":
                buildGuestTable();
                break;
            case "registered":
                buildRegisteredTable();
                break;
            case "register":
                userTypesBox.setItems(options);
                userTypesBox.setValue("User");
                break;
        }
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
        System.out.println(flightTracker.getFlights().size());
        System.out.println("AA" + table.getColumns().size());
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

    @FXML
    private void loginButtonClicked() throws IOException {
        String username = usernameLoginField.getText();
        String password = passwordLoginField.getText();

        if (flightTracker.validateLogin(username, password)) {
            // Successful login
            statusLabel.setText("Login successful");
            view = "registered";
            switchPage("index");
            title.setText("Registered User");
            if (flightTracker.getLoggedUser() instanceof AirlineAdmin) {

            }else if(flightTracker.getLoggedUser() instanceof AirportAdmin){

            }else if(flightTracker.getLoggedUser() instanceof SysAdmin){

            }else{

            }

        } else {
            statusLabel.setText("Invalid username or password");
        }
    }

    @FXML
    private void registerButtonClicked () throws IOException{
        view = "register";
        switchPage("register");
    }

    public void onGuestLoginButton(ActionEvent event) throws IOException{
        view = "guest";
        switchPage("index-guest");
    }

    public void onBack(ActionEvent event) throws IOException{
        view = "";
        switchPage("login");
    }

}
