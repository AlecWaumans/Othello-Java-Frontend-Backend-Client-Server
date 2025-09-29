package client.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import client.model.OthelloClient;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.stage.Stage;
import othello.view.javaFx.AlertBox;
import othello.view.javaFx.ConfigurationPane;

/**
 * Author: Alec Waumans (2025)
 * Controller for the login view of the Othello client application.
 * Handles the connection process to the server using the provided email, host, and port.
 */
public class LoginController {

    // Text field for entering the user's email
    @FXML
    private TextField emailField;
    
    // Text field for entering the server host
    @FXML
    private TextField hostField;
    
    // Text field for entering the server port
    @FXML
    private TextField portField;

    /**
     * Handles the click event on the "Connect" button.
     * Attempts to connect to the server using the provided host, port, and email.
     * If the connection is successful, switches to the configuration screen.
     * Otherwise, displays an error message.
     *
     * @param event the ActionEvent triggered by clicking the "Connect" button
     */
    @FXML
    private void handleConnect(ActionEvent event) {
        try {
            // Retrieve input values from the fields
            String email = emailField.getText();
            String host = hostField.getText();
            int port = Integer.parseInt(portField.getText());

            // Create the client instance
            OthelloClient client = new OthelloClient(host, port, email, "");
            ClientConsole console = new ClientConsole(client);
            ClientJavaFx controller = new ClientJavaFx(client);
            client.setController(controller);

            // Wait for server confirmation
            if (client.connectAndCheckProfile()) {
                // Redirect to the main configuration interface
                Stage stage = (Stage) emailField.getScene().getWindow();
                Platform.runLater(() -> {
                    ConfigurationPane configPane = new ConfigurationPane(stage, controller);
                    Scene scene = new Scene(configPane);
                    stage.setScene(scene);
                    stage.setTitle("Othello Configuration");
                    stage.setResizable(false);
                });
            } else {
                // Show an error alert if the server is unreachable or timeout occurred
                new AlertBox("Connection Failed", "Error", "Server unreachable or timeout exceeded").errorShow();
            }

        } catch (Exception e) {
            // Show an error alert for invalid format or network issues
            new AlertBox("Connection Error", "Error", "Invalid format or network problem").errorShow();
            e.printStackTrace();
        }
    }
    /**
     * Alec 58399 : ICI j'insere donc le Handle du bouton RESET. 
     * @param Event Le click sur le bouton.
     */
    @FXML
    private void handleReset(ActionEvent Event) {
        try {
            System.out.println("RESET");

        } catch (Exception e) {
            // Show an error alert for invalid format or network issues
            new AlertBox("Reset Error", "Error", "Contact Admin").errorShow();
            e.printStackTrace();
        }
    }
}
