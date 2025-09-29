package othello.view.javaFx;

import common.GameInfo;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;

/**
 * @author : Sami EL YAGHMOURI (2024) & Alec Waumans (2025) 
 * The ScorePane class represents the graphical interface for displaying player scores. It extends
 * the JavaFX GridPane.
 */
public class ScorePane extends GridPane {

    private final Label score1Label;
    private final Label score2Label;
    private final TextField score1Text;
    private final TextField score2Text;
    private final Background red;
    private final Background white;

    /**
     * @author : Sami EL YAGHMOURI (2024) 
     * Constructs a ScorePane object,
     * initializing labels, text fields, and backgrounds.
     */
    public ScorePane() {
        score1Label = new Label(ConstantsView.namePlayer1);
        score2Label = new Label(ConstantsView.namePlayer2);

        score1Text = new TextField();
        score1Text.setEditable(false);

        score2Text = new TextField();
        score2Text.setEditable(false);

        this.addColumn(0, score1Text, score1Label);
        this.addColumn(1, score2Text, score2Label);
        this.setAlignment(Pos.CENTER);
        red = new Background(new BackgroundFill(Color.rgb(255, 153, 153), CornerRadii.EMPTY, null));
        white = new Background(new BackgroundFill(Color.rgb(255, 255, 255), CornerRadii.EMPTY, null));

        score1Text.setAlignment(Pos.CENTER);
        score2Text.setAlignment(Pos.CENTER);
    }

    /**
     * @author : Sami EL YAGHMOURI (2024) & Alec Waumans (2025) Updates the
     * score pane with the provided scores and indicates the turn of the current
     * player.
     * Alec : The logique of the function not change but we use GameInfo 
     * type on the new version of the code.
     * @param gameinfo : state of the game. 
     */
    public void update(GameInfo gameinfo) {
        int[] score = gameinfo.getScore();
        score1Text.setText(Integer.toString(score[0]));
        score2Text.setText(Integer.toString(score[1]));
        String currentPlayerColor;
        if (gameinfo.getNextColor().equals("BLACK")) {
            currentPlayerColor = "WHITE";
        } else {
            currentPlayerColor = "BLACK";
        }
        setTurn(currentPlayerColor);
    }

    /**
     * @author : Sami EL YAGHMOURI (2024) & Alec Waumans (2025) 
     * Sets the background color of the text fields based on the turn of the current
     * player. 
     * Alec: To avoid calling the game model, I changed the parameter
     * type, instead of using a ColorToken, I now use a String with the color
     * name.
     */
    private void setTurn(String color) {
        switch (color) {
            case "BLACK" -> {
                score1Text.setBackground(red);
                score2Text.setBackground(white);
            }
            case "WHITE" -> {
                score2Text.setBackground(red);
                score1Text.setBackground(white);
            }
        }
    }
}
