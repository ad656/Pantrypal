package PantryPal.client;

import PantryPal.client.controllers.SceneController;

import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {
    
    @Override
    public void start(Stage primaryStage) throws Exception {

        SceneController sceneManager = new SceneController(primaryStage, "file.json");
        sceneManager.setPrimaryStage(primaryStage);
        
        primaryStage.setTitle("PantryPal");
        
        // Show the app
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}