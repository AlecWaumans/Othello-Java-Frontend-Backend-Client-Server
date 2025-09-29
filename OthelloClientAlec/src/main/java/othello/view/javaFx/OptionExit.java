package othello.view.javaFx;

import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;

/**
 * @authors : Sami EL YAGHMOURI (2024)
 * The OptionExit class represents the exit option in the menu bar for the Othello application.
 */
public class OptionExit extends Menu {

    private final MenuItem exitLabel;

    /**
     * Constructs an OptionExit object with the exit option.
     */
    public OptionExit(){
        super("Menu");
        exitLabel = new MenuItem("Exit");
        this.getItems().add(exitLabel);
    }
}

