package othello.view.javaFx;

import javafx.scene.control.Alert;

/**
 * @author : Sami EL YAGHMOURI (2024) 
 * The AlertBox class creates an alert dialog
 * box for displaying error messages related
 * to input validation.
 */
public class AlertBox {
    private final Alert errorInput;

    /**
     *
     * Constructs an AlertBox object
     *
     * @param contentText The content text of the error message.
     * @param title       The title of the error message dialog box.
     * @param headerText  The header text of the error message dialog box.
     */
    public AlertBox(String contentText, String title, String headerText){

        errorInput = new Alert(Alert.AlertType.ERROR);
        errorInput.setContentText(contentText);
        errorInput.setTitle(title);
        errorInput.setHeaderText(headerText);
    }

    /**
     * 
     * Displays the error alert dialog box.
     */
    public void errorShow() {
        errorInput.show();
    }
}
