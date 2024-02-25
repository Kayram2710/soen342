module ca.concordia {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.gson;
    opens ca.concordia to javafx.fxml;
    exports ca.concordia;
}
