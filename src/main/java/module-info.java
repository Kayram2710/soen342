module ca.concordia {
    requires java.sql;
    requires javafx.graphics;
    requires javafx.fxml;
    requires javafx.controls;
    requires com.google.gson;
    requires jfxtras.common;
    requires jfxtras.fxml;
    requires jfxtras.controls;
    opens ca.concordia.UI to javafx.fxml;
    exports ca.concordia;
}
