package ca.concordia.UI;

import java.io.IOException;
import java.util.ArrayList;

import ca.concordia.App;
import ca.concordia.FlightTracker;
import ca.concordia.airport.Aircraft;
import ca.concordia.airport.Airline;
import ca.concordia.airport.Airport;
import ca.concordia.flight.CargoFlight;
import ca.concordia.flight.CommercialFlight;
import ca.concordia.flight.PrivateFlight;
import ca.concordia.user.AirlineAdmin;
import ca.concordia.user.AirportAdmin;
import ca.concordia.user.SysAdmin;
import ca.concordia.user.User;
import ca.concordia.location.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import jfxtras.scene.control.LocalDateTimeTextField;

@SuppressWarnings("unchecked")
//UI Class
public class UIController {

    //FXML Setup
    @FXML
    private TableView<String[]> table;
    @FXML
    private TableView<String[]> fleetTable;
    @FXML
    private Label title;
    @FXML
    private TextField usernameLoginField;
    @FXML
    private TextField flightNumber;
    @FXML
    private ComboBox<String> city;
    @FXML
    private TextField airpName;
    @FXML
    private TextField ltrCde;
    @FXML
    private TextField cityname;
    @FXML
    private TextField countryname;
    @FXML
    private TextField temp;
    @FXML
    private TextField airlName;
    @FXML
    private LocalDateTimeTextField scheduledDepart;
    @FXML
    private LocalDateTimeTextField scheduledArrival;
    @FXML
    private PasswordField passwordLoginField;
    @FXML
    private Label statusLabel;
    @FXML
    private ComboBox<String> userTypesBox;
    @FXML
    private ComboBox<String> fplaneLoc;
    @FXML
    private ComboBox<String> airlineInput;
    @FXML
    private ComboBox<String> airportInput;
    @FXML
    private ComboBox<String> destinationInput;
    @FXML
    private ComboBox<String> sourceInput;
    @FXML
    private ComboBox<String> airLoc;
    @FXML
    private ComboBox<String> metric;
    @FXML
    private HBox airportBox;
    @FXML
    private HBox airlineBox;
    @FXML
    private HBox sysAdminBox;
    @FXML
    private VBox addflightform;
    @FXML
    private ComboBox<String> flightType;
    @FXML
    private Label addStatus;

    private final ObservableList<String> options = FXCollections.observableArrayList(
        "User",
        "Airline Admin",
        "Airport Admin",
        "System Admin"
    );

    private final ObservableList<String> metrics = FXCollections.observableArrayList(
        "Celsius",
        "Kelvin",
        "Fahrenheit"
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

        if(flightTracker.getLoggedUser() instanceof AirlineAdmin){
            airLoc.setItems(airports);
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

        if(flightNumber.getText().isEmpty()){
            addStatus.setText("Invalid Input: Enter flight Number.");
            return;
        }

        if(sourceInput.getValue() == null || destinationInput.getValue() == null){
            addStatus.setText("Invalid Input: Enter flight source and destination.");
            return;
        }

        if(flightTracker.getLoggedUser() instanceof AirportAdmin){
            String code = ((AirportAdmin)flightTracker.getLoggedUser()).getAirport().getLetterCode();
            if(!(sourceInput.getValue().equals(code)||destinationInput.getValue().equals(code))){
                addStatus.setText("Invalid Input: Private flight must land or depart from current Airport access.");
                return;
            }
        }

        if(sourceInput.getValue().equals(destinationInput.getValue())){
            addStatus.setText("Invalid Input: Flight cannot have the same source and destination.");
            return;
        }

        if(scheduledDepart.getText().isEmpty() || scheduledArrival.getText().isEmpty()){
            addStatus.setText("Invalid Input: Enter flight dates.");
            return;
        }    

        if(scheduledDepart.getLocalDateTime().compareTo(scheduledArrival.getLocalDateTime()) >= 0){
            addStatus.setText("Invalid Input: Arrival cannot be shceduled before departure.");
            return;
        }

        Airport source = flightTracker.fetchAirport(sourceInput.getValue());
        Airport destination = flightTracker.fetchAirport(destinationInput.getValue());;
        int registeredFlight = 0;

        if(flightTracker.getLoggedUser() instanceof AirlineAdmin){
            if(flightType.getValue().equals("Cargo Flight")){
                CargoFlight flight = new CargoFlight(flightNumber.getText(), source, destination, scheduledDepart.getLocalDateTime(), scheduledArrival.getLocalDateTime(), null, null);
                registeredFlight = flightTracker.registerFlight(flight);
            }else if(flightType.getValue().equals("Commercial Flight")){
                CommercialFlight flight = new CommercialFlight(flightNumber.getText(), source, destination, scheduledDepart.getLocalDateTime(), scheduledArrival.getLocalDateTime(), null, null);
                registeredFlight = flightTracker.registerFlight(flight);
            }
        }else if(flightTracker.getLoggedUser() instanceof AirportAdmin){
            PrivateFlight flight = new PrivateFlight(flightNumber.getText(), source, destination, scheduledDepart.getLocalDateTime(), scheduledArrival.getLocalDateTime(), null, null);
            registeredFlight = flightTracker.registerFlight(flight);
        } 

        switch (registeredFlight) {
            case 0:
                addStatus.setText("Flight added successfully!");
                break;
            case 1:
                addStatus.setText("Error: Arrival Airport already has a flight scheduled.");
                break;
            case 2:
                addStatus.setText("Error: Departure Airport already has a flight scheduled.");
                break;
            case 3:
                addStatus.setText("Error: No available aircrafts for this time.");
                break;
            case 4:
                addStatus.setText("Error: Flight number already taken.");
                break;
            default:
                break;
        }

        buildRegisteredTable();
    }

    @FXML
    public void initialize() {

        switch (view) {
            case "guest":
                buildGuestTable();
                break;
            case "registered":
                airlineBox.setVisible(false);
                sysAdminBox.setVisible(false);
                addflightform.setVisible(false);
                buildRegisteredTable();
                if (flightTracker.getLoggedUser() instanceof AirlineAdmin) {
                    title.setText("Airline Adminsitration Page for: "+((AirlineAdmin)flightTracker.getLoggedUser()).getAirline().getName());
                    //addStatus.setText("Enter flight source and destination.");
                    addflightform.setVisible(true);
                    airlineBox.setVisible(true);
                    buildFleetTable();
                    ObservableList<String> flightOptions = FXCollections.observableArrayList("Cargo Flight","Commercial Flight");
                    flightType.setItems(flightOptions);
                    flightType.setValue("Cargo Flight");
                    populateLists();
                } else if (flightTracker.getLoggedUser() instanceof AirportAdmin) {
                    title.setText("Airport Adminsitration Page for: "+((AirportAdmin)flightTracker.getLoggedUser()).getAirport().getLetterCode());
                    addflightform.setVisible(true);
                    ObservableList<String> flightOptions = FXCollections.observableArrayList("Private Flight");
                    flightType.setItems(flightOptions);
                    flightType.setValue("Private Flight");
                    populateLists();
                } else if (flightTracker.getLoggedUser() instanceof SysAdmin){
                    title.setText("System Adminsitration Page"); 
                    sysAdminBox.setVisible(true);
                    setCities();
                    metric.setItems(metrics);
                    setPlanesLoc();
                }
                break;
            case "register":
                userTypesBox.setItems(options);
                userTypesBox.setValue("User");
                populateLists();
                break;
        }
    }
 
    private void setPlanesLoc(){
        ObservableList<String> airports = FXCollections.observableArrayList();

        for(Airport airport: this.flightTracker.fetchAllAirports()){
            airports.add(airport.getLetterCode());
        }

        fplaneLoc.setItems(airports);
        
    }

    @FXML
    private void setCities(){
        ObservableList<String> cities = FXCollections.observableArrayList();
        for(City c: this.flightTracker.fetchAllCities()){
            cities.add(c.getName());
        }
        city.setItems(cities);
    }

    @FXML
    private void registerUser() throws IOException{
        
        if(usernameLoginField.getText().isEmpty() || passwordLoginField.getText().isEmpty()){

            statusLabel.setText("Missing username or password");

        }else{
            User user;
            
            if(userTypesBox.getValue().equals("Airline Admin")){
                Airline airline = null;
                
                if(airlineInput.getValue() == null){
                    statusLabel.setText("Missing Airline Selection");
                    return;
                }

                for(Airline p : flightTracker.fetchAllAirlines()){
                    if(p.getName().equalsIgnoreCase(airlineInput.getValue())){
                        airline = p;
                        break;
                    }
                }
                user = new AirlineAdmin(usernameLoginField.getText(), passwordLoginField.getText(),airline);

            } else if(userTypesBox.getValue().equals("Airport Admin")){
                Airport airport = null;

                if(airportInput.getValue() == null){
                    statusLabel.setText("Missing Airport Selection");
                    return;
                }

                airport = flightTracker.fetchAirport(airportInput.getValue());

                user = new AirportAdmin(usernameLoginField.getText(), passwordLoginField.getText(),airport);
                
            } else if(userTypesBox.getValue().equals("System Admin")){
                user = new SysAdmin(usernameLoginField.getText(), passwordLoginField.getText());
            }else{
                user = new User(usernameLoginField.getText(), passwordLoginField.getText());
            }

            user.save();
            flightTracker.setLoggedUser(user);
            view = "registered";
            switchPage("index");

        }
       
    }

    private void buildFleetTable(){

        fleetTable.getColumns().clear();

        // Initialize Columns
        TableColumn<String[], String> IDColumn = new TableColumn<String[], String>();
        TableColumn<String[], String> locationColumn = new TableColumn<String[], String>();

        IDColumn.setText("Aircraft ID");
        locationColumn.setText("Location");


        fleetTable.getColumns().addAll(IDColumn, locationColumn);
        
        // Setting up collumns to read values
        setUpFleetTableCells();
        
        ObservableList<String[]> data = ((AirlineAdmin)flightTracker.getLoggedUser()).getAirline().fleetTObservableList();
        fleetTable.setItems(data);
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

    private void setUpFleetTableCells() {

        for (int columnIndex = 0; columnIndex < fleetTable.getColumns().size(); columnIndex++) {
            
            final int finalColumnIndex = columnIndex;
            TableColumn<String[], String> column = (TableColumn<String[], String>) fleetTable.getColumns().get(finalColumnIndex);
            column.setCellValueFactory(features -> {
                String[] row = features.getValue();

                if (row != null && row.length > finalColumnIndex) {
                    return new SimpleStringProperty(row[finalColumnIndex]);
                } else {
                    return new SimpleStringProperty("");
                }
            });
        }
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
                    return new SimpleStringProperty("");
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
            statusLabel.setText("Login successful!");
            view = "registered";
            switchPage("index");
            title.setText("Registered User");
        } else {
            statusLabel.setText("Invalid username or password.");
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

    public void loggout(ActionEvent event) throws IOException{
        flightTracker.setLoggedUser(null);

        view = "";
        switchPage("login");
    }

    
    public void createAircraft(ActionEvent event) throws IOException{

        if(airLoc.getValue()==null){
            addStatus.setText("Error adding aircraft: No location selected.");
            return;
        }

        String command = "Select aircraftID From Aircraft";
        ArrayList<Object> result = flightTracker.accessDB().runQuery(command);
        
        new Aircraft(((AirlineAdmin)flightTracker.getLoggedUser()).getAirline(), result.size(), flightTracker.fetchAirport(airLoc.getValue()));

        buildFleetTable();
        addStatus.setText("Aircraft Added!");
    }

    public void createAirport(ActionEvent event) throws IOException{

        if(ltrCde.getText().isEmpty()){
            addStatus.setText("Error adding airport: No code entered.");
            return;
        }else if(city.getValue()==null){
            addStatus.setText("Error adding airport: No city selected.");
            return;
        }else if(airpName.getText().isEmpty()){
            addStatus.setText("Error adding airport: No name entered.");
            return;
        }

        String command = "Select * From City where name='"+city.getValue()+"';";
        ArrayList<Object> result = flightTracker.accessDB().runQuery(command);

        Temperature temp = new Temperature(Double.parseDouble(result.get(2).toString()),result.get(3).toString());
        City city = new City(result.get(0).toString(), result.get(1).toString(), temp);
        new Airport(airpName.getText(),ltrCde.getText(), city);
        
        addStatus.setText("Airport Added!");
        setPlanesLoc();
    }

    public void createAirline(ActionEvent event) throws IOException{

        if(airlName.getText().isEmpty()){
            addStatus.setText("Error adding airline: No name entered.");
            return;
        }
        if(fplaneLoc.getValue()==null){
            addStatus.setText("Error adding airline: No location selected for first aircraft.");
            return;
        }

        String command = "Select aircraftID From Aircraft";
        ArrayList<Object> result = flightTracker.accessDB().runQuery(command);
        
        new Aircraft(new Airline(airlName.getText()), result.size(), flightTracker.fetchAirport(fplaneLoc.getValue()));

        addStatus.setText("Airline Added!");
    }

    public void createCity(ActionEvent event) throws IOException{

        if(cityname.getText().isEmpty()){
            addStatus.setText("Error adding city: No name entered.");
            return;
        }else if(countryname.getText().isEmpty()){
            addStatus.setText("Error adding city: No country entered.");
            return;
        }else if(temp.getText().isEmpty()){
            addStatus.setText("Error adding city: No temperature entered.");
            return;
        }else if(metric.getValue()==null){
            addStatus.setText("Error adding city: No metric selected.");
            return;
        }

        try {
            Double.parseDouble(temp.getText());
        } catch (Exception e) {
            addStatus.setText("Error adding city: Enter numerical value for temperature.");
            return;
        }

        new City(cityname.getText(), countryname.getText(), new Temperature(Double.parseDouble(temp.getText()), metric.getValue()));
        addStatus.setText("City Added!");
        setCities();
    }

}
