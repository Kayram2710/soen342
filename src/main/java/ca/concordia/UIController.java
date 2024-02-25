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
import ca.concordia.user.AirportAdmin;
import ca.concordia.user.User;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.util.Callback;

public class UIController {

    @FXML
    private TableView<String[]> table;
    @FXML
    private Label title;

    @FXML
    private void switchToSecondary() throws IOException {
        App.setRoot("secondary");
    }
    //Hardcoded registry TODO Remove
    private Registry registry;

    private FlightTracker flightTracker;
    @FXML
    public void initialize() {
        // TODO: Remove Hardcoded Data
        ArrayList<Flight> flights = new ArrayList<>();
        Temperature temp1 = new Temperature(25, "Celcius");
        Temperature temp2 = new Temperature(-5, "Celcius");
        City antigua = new City("Antigua", "Guatemala", temp1);
        City toronto = new City("Toronto", "Canada", temp2);
        Airport airport1 = new Airport("Antigua", "ANU", antigua);
        Airport airport2 = new Airport("Toronto", "YYZ", toronto);
        Airline airline1 = new Airline("Sunwing");
        Aircraft aircraft1 = new Aircraft(airline1, 0);
        airline1.addAircraft(aircraft1);
        Date date1 = new Date();
        date1.setTime(1232133);
        Date date2 = new Date();
        date2.setTime(99999999);
        Flight flight1 = new PrivateFlight("WG117", airport1, airport2, date1, date2, date1, date2, aircraft1,
                airport1);
        date1.setTime(999999999);
        Flight flight2 = new NonPrivateFlight("WG118", airport2, airport1, date2, date1, date2, date1, aircraft1,
                airline1);
        flights.add(flight1);
        flights.add(flight2);
        ArrayList<Airport> airports = new ArrayList<>();
        airports.add(airport1);
        airports.add(airport2);
        ArrayList<Aircraft> aircrafts = new ArrayList<>();
        aircrafts.add(aircraft1);
        ArrayList<Airline> airlines = new ArrayList<>();
        airlines.add(airline1);
        User user = new User("Jean", "Test");
        ArrayList<User> users = new ArrayList<>();
        users.add(user);

        registry = new Registry(flights, airports, aircrafts, airlines, users);
        flightTracker = new FlightTracker(null ,registry);
        //Build guest table
         buildGuestTable(registry);
        //Build RegisteredTable
       // buildRegisteredTable(registry);
    }

    private void buildRegisteredTable(Registry reg) {
        table.getColumns().clear();
        if(table.getColumns().isEmpty()){
            // Initialize Columns
        TableColumn<String[], String> numberColumn = new TableColumn<String[], String>();
        numberColumn.setText("Flight Number");
        TableColumn<String[], String> sourceColumn = new TableColumn<String[], String>();
        sourceColumn.setText("Source");
        TableColumn<String[], String> destinationColumn = new TableColumn<String[], String>();
        destinationColumn.setText("Destination");
        TableColumn<String[], String> scheduledDepartColumn = new TableColumn<String[], String>();
        scheduledDepartColumn.setText("Scheduled Depart");
        TableColumn<String[], String> scheduledArrivColumn = new TableColumn<String[], String>();
        scheduledArrivColumn.setText("Scheduled Arrival");
        TableColumn<String[], String> actualDepartColumn = new TableColumn<String[], String>();
        actualDepartColumn.setText("Actual Depart");
        TableColumn<String[], String> actualArrivColumn = new TableColumn<String[], String>();
        actualArrivColumn.setText("Actual Arrival");
        TableColumn<String[], String> planeColumn = new TableColumn<String[], String>();
        planeColumn.setText("Plane");
        TableColumn<String[], String> operatorColumn = new TableColumn<String[], String>();
        operatorColumn.setText("Operator");
        table.getColumns().addAll(numberColumn, sourceColumn, destinationColumn, scheduledDepartColumn,
                scheduledArrivColumn, actualDepartColumn, actualArrivColumn, planeColumn, operatorColumn);
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
        numberColumn.setText("Flight Number");
        TableColumn<String[], String> sourceColumn = new TableColumn<String[], String>();
        sourceColumn.setText("Source");
        TableColumn<String[], String> destinationColumn = new TableColumn<String[], String>();
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
            TableColumn<String[], String> column = (TableColumn<String[], String>) table.getColumns()
                    .get(finalColumnIndex);
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
