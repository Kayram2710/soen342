package ca.concordia.UI;

import java.io.IOException;
import java.time.*;
import java.util.ArrayList;
import java.util.List;

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
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

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
    private TextField flightNumber;
    @FXML
    private DatePicker scheduledDepart;
    @FXML
    private DatePicker scheduledArrival;
    @FXML
    private PasswordField passwordLoginField;
    @FXML
    private Label statusLabel;
    @FXML
    private ComboBox<String> userTypesBox;
    @FXML
    private ComboBox<String> airlineInput;
    @FXML
    private ComboBox<String> airportInput;
    @FXML
    private ComboBox<String> destinationInput;
    @FXML
    private ComboBox<String> sourceInput;
    @FXML
    private HBox airportBox;
    @FXML
    private HBox airlineBox;
    @FXML
    private ComboBox<String> flightType;
    @FXML
    private Label addStatus;

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

    @FXML
    private void onUserTypeChange(ActionEvent event){
        if(userTypesBox.getValue().equals("Airline Admin")){
            airlineBox.setDisable(false);
            airportBox.setDisable(true);
        } else if(userTypesBox.getValue().equals("Airport Admin")){
            airlineBox.setDisable(true);
            airportBox.setDisable(false);
        } else {
            airlineBox.setDisable(true);
            airportBox.setDisable(true);
        }
    }
    private void populateLists(){
        ObservableList<String> airlines = FXCollections.observableArrayList();
        ObservableList<String> airports = FXCollections.observableArrayList();

        for(Airline airline: this.flightTracker.fetchAllAirlines()){
            airlines.add(airline.getName());
        }
        for(Airport airport: this.flightTracker.fetchAllAirports()){
            airports.add(airport.getLetterCode());
        }

        
        if(sourceInput != null){
            sourceInput.setItems(airports);
            destinationInput.setItems(airports);
        }else{
            airportInput.setItems(airports);
            airlineInput.setItems(airlines);
        }
    }

    //FXML function to switch pages
    @FXML
    private void switchPage(String fxml) throws IOException {
        App.setRoot(fxml);
    }

    @FXML
    private void addFlight(){
        Airport source = null;
        Airport destination = null;
        boolean registeredFlight = false;
        for(Airport a: flightTracker.fetchAllAirports()){
            if(a.getLetterCode().equals(sourceInput.getValue())){
                source = a;
            }
            if(a.getLetterCode().equals(destinationInput.getValue())){
                destination = a;
            }
        }

        if(flightTracker.getLoggedUser() instanceof AirlineAdmin){
            if(flightType.getValue().equals("Cargo Flight")){
                System.out.println("AAA");
                CargoFlight flight = new CargoFlight(flightNumber.getText(), source, destination, scheduledDepart.getValue().atStartOfDay(), scheduledArrival.getValue().atStartOfDay(), null, null, null);
                registeredFlight = flightTracker.registerFlight(flight);
            }else if(flightType.getValue().equals("Commercial Flight")){
                CommercialFlight flight = new CommercialFlight(flightNumber.getText(), source, destination, scheduledDepart.getValue().atStartOfDay(), scheduledArrival.getValue().atStartOfDay(), null, null, null);
                registeredFlight = flightTracker.registerFlight(flight);
            }
        }

        if(registeredFlight){
            addStatus.setText("Flight added successfully!");
        }else{
            addStatus.setText("Error adding flight.");
        }

        buildRegisteredTable();
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
        // System.out.println(f.getAircraftID() + " " + f.getReserved());
        // }

        ArrayList<Airline> a = flightTracker.fetchAllAirlines();
        for (Airline aircraft : a) {
            System.out.println(aircraft.toSQL());
        }

        System.out.println(view);
        switch (view) {
            case "guest":
                buildGuestTable();
                break;
            case "registered":
                buildRegisteredTable();
                if (flightTracker.getLoggedUser() instanceof AirlineAdmin) {
                    ObservableList<String> flightOptions = FXCollections.observableArrayList(
                            "Cargo Flight",
                            "Commercial Flight");
                    flightType.setItems(flightOptions);
                    flightType.setValue("Cargo Flight");
                } else if (flightTracker.getLoggedUser() instanceof AirportAdmin) {
                    ObservableList<String> flightOptions = FXCollections.observableArrayList(
                            "Private Flight");
                    flightType.setItems(flightOptions);
                    flightType.setValue("Private Flight");
                }
                populateLists();
                break;
            case "register":
                userTypesBox.setItems(options);
                userTypesBox.setValue("User");
                populateLists();
                break;
        }
    }
    @FXML
    private void registerUser() throws IOException{
        if(usernameLoginField.getText().isEmpty() || passwordLoginField.getText().isEmpty()){
            statusLabel.setText("Missing username or password");
        }else{
            User user;
            if(userTypesBox.getValue().equals("Airline Admin")){
                Airline airline = null;
                for(Airline p : flightTracker.fetchAllAirlines()){
                    if(p.getName().equalsIgnoreCase(airlineInput.getValue())){
                        airline = p;
                        break;
                    }
                }
                user = new AirlineAdmin(usernameLoginField.getText(), passwordLoginField.getText(),airline);
            } else if(userTypesBox.getValue().equals("Airport Admin")){
                Airport airport = null;
                for(Airport p : flightTracker.fetchAllAirports()){
                    if(p.getLetterCode().equalsIgnoreCase(airportInput.getValue())){
                        airport = p;
                        break;
                    }
                }
                user = new AirportAdmin(usernameLoginField.getText(), passwordLoginField.getText(),airport);
            } else if(userTypesBox.getValue().equals("System Admin")){
                user = new SysAdmin(usernameLoginField.getText(), passwordLoginField.getText());
            }else{
                user = new User(usernameLoginField.getText(), passwordLoginField.getText());
            }
            user.save();
            switchPage("index");
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
