module ca.concordia {
    requires java.sql;
    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.gson;
    opens ca.concordia.UI to javafx.fxml;
    exports ca.concordia;
}
