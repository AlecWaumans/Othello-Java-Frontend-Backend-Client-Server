package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 * @author: Alec Waumans (2025)
 * Main entry point for the Othello client application.
 * Loads the login view (FXML) and initializes the primary stage.
 */

public class MainApp extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Load the FXML file for the login screen
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/othello/view/LoginView.fxml"));
        
        // Create the root layout from the loaded FXML
        Pane root = loader.load();
        
        primaryStage.setTitle("Connexion Othello");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
