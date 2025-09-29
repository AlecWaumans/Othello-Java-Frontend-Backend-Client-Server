package othello.view.javaFx;

import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;

/**
 * @author : Sami EL YAGHMOURI (2024)
 * The SizePane class represents a pane for entering
 * the size of the game array.
 */
public class SizePane extends HBox {
    private final Label labelSize;
    private final TextField textSize;

    /**
     * @author : Sami EL YAGHMOURI (2024)
     * Constructs a SizePane object and initializes
     * its components.
     */
    public SizePane() {
        this.labelSize = new Label("Array's size:");

        this.textSize = new TextField("Between 3 and 15");
        this.getChildren().addAll(labelSize, textSize);

        textSize.addEventFilter(KeyEvent.KEY_TYPED, new EventHandler<>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                textSize.setStyle("-fx-text-fill: black;");
                String regex = "^[0-9]$";
                if (!keyEvent.getCharacter().matches(regex)) {

                    keyEvent.consume();
                }
            }
        });

        textSize.addEventHandler(MouseEvent.MOUSE_CLICKED
                , e -> {textSize.setText("");});

        textSize.setStyle("-fx-text-fill: grey;");

        this.setAlignment(Pos.CENTER);
        this.setSpacing(10);
        this.setPadding(new Insets(10)); //Object who made the padding. 
    }

    public String getTextSize() {
        return textSize.getText();
    }
}
