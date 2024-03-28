package ca.concordia;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class App extends Application {

    private static Scene scene;
   
    //To start the fxml at index
    @SuppressWarnings("exports")
    @Override
    public void start(Stage stage) throws IOException {
        //Change guest to registered to switch between views
        scene = new Scene(loadFXML("login"), 1140, 740);
        stage.setScene(scene);
        stage.show();  
    }

    public static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }
   
    public static void main(String[] args) {
        launch();
    }
}