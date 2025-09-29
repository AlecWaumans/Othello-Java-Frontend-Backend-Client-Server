package othello.view.javaFx;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.MenuBar;
import client.controller.ClientJavaFx;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;



/**
 * @author : Sami EL YAGHMOURI (2024) & Alec Waumans (2025)
 * The MenuBarOption class represents the menu bar options for
 * the Othello application.
 */
public class MenuBarOption extends MenuBar {

    private final OptionExit optionExit;
    private final EventHandler<ActionEvent> exitHandler;
    
    //ici je rajoute donc les atributs du bouton RESET sur le menu bar.
    private final OptionReset optionReset;
    private final EventHandler<ActionEvent> resetHandler;
    

    /**
     * Alec 58399 : J'ai rajouter cette méthode pour le bouton Reset
     * Constructs a MenuBarOption object with exit option.
     */
    public MenuBarOption(ClientJavaFx client) {
        
        this.optionReset = new OptionReset();
        this.getMenus().add(optionReset);

        optionExit = new OptionExit();
        this.getMenus().add(optionExit);
        
        resetHandler = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                client.sendReset();
                System.out.println("ResetMenu");
            }
            
        };
        optionReset.setOnAction(resetHandler);

        exitHandler = new EventHandler<ActionEvent>() {
            // Alec Waumans: I made it so that instead of just closing the platform, 
            // it disconnects the client, closes the interface, and exits the program properly.
            @Override
            public void handle(ActionEvent actionEvent) {
                try {
                    client.disconnect();
                    Platform.exit();
                    System.exit(0);
                } catch (IOException ex) {
                    Logger.getLogger(MenuBarOption.class.getName()).log(Level.SEVERE, null, ex);
                }
                Platform.exit();
            }
            //End Alec
        };

        optionExit.setOnAction(exitHandler);
    }
}
