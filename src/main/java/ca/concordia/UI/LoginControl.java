package ca.concordia.UI;

import ca.concordia.App;
import ca.concordia.FlightTracker;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginControl {
    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label statusLabel;

    @FXML
    private void loginButtonClicked() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (validateLogin(username, password)) {
            // Successful login
            statusLabel.setText("Login successful");
        } else {
            statusLabel.setText("Invalid username or password");
        }
    }

    private boolean validateLogin(String username, String password) {
        return username.equals("admin") && password.equals("password");
    }
}
