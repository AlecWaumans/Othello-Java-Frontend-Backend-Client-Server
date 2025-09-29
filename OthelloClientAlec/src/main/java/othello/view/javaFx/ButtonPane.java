package othello.view.javaFx;

import client.controller.ClientJavaFx;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;

/**
 * @author : Sami EL YAGHMOURI (2024) & Alec Waumans (2025) 
 * The ButtonPane class
 * represents a pane containing buttons for game actions such as undo, redo, and
 * surrender.
 */
public class ButtonPane extends HBox {

    private final Button undo;
    private final Button redo;
    private final Button surrender;
    private EventHandler<ActionEvent> undoHandler;
    private EventHandler<ActionEvent> redoHandler;
    private EventHandler<ActionEvent> surrenderHandler;

    /**
     * @author : Sami EL YAGHMOURI (2024) Constructs a ButtonPane object with
     * buttons for undo, redo, and surrender actions.
     */
    public ButtonPane() {
        undo = new Button("Undo");
        redo = new Button("Redo");
        surrender = new Button("Surrender");

        this.getChildren().addAll(undo, redo, surrender);
    }

    /**
     * @author : Sami EL YAGHMOURI (2024) & Alec Waumans (2025) 
     * Initializes the button pane with event handlers for each button.
     *
     * @param controller Alec: The game model controller is replaced by the
     * client controller, so that when the player clicks a button, the client
     * sends a message to the server.
     *
     * @implNote This method sets up event handlers for each button to perform
     * corresponding actions in the game.
     *
     * @see Controller
     */
    public void init(ClientJavaFx controller) {

        undoHandler = new EventHandler<>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                // Alec: instructs the client controller to send a message 
                // to the server with the intention of performing an undo.
                controller.sendUndo();
                // End Alec
            }
        };

        redoHandler = new EventHandler<>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                // Alec: instructs the client controller to send a message 
                // to the server with the intention of performing a redo.
                controller.sendRedo();
                // End Alec
            }
        };

        surrenderHandler = new EventHandler<>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                // Alec: instructs the client controller to send a message 
                // to the server with the intention of surrendering.
                controller.sendPlay(0, 0, true);
                // End Alec
            }
        };

        surrender.setOnAction(surrenderHandler);
        undo.setOnAction(undoHandler);
        redo.setOnAction(redoHandler);

        this.setSpacing(10);
        this.setAlignment(Pos.CENTER);
        this.setPadding(new Insets(0, 0, 10, 0));
    }
}
