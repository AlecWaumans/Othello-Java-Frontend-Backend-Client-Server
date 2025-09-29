package othello.view.javaFx;

import javafx.scene.control.Alert;


/**
 * @author : Sami EL YAGHMOURI (2024)
 * The InfoBoxWinner class creates an alert dialog box to display the game result.
 * It shows whether the black player or the white player won, or if the game ended in a tie (ex aequo).
 */
public class InfoBoxWinner {

    private final Alert alert;

    /**
     * @author : Sami EL YAGHMOURI (2024)
     * Constructs an InfoBoxWinner object and initializes the alert dialog.
     * @param scores An array containing the scores of both players.
     */
    public InfoBoxWinner(int[] scores) {
        this.alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Game Over");
        alert.setHeaderText("Game Over");
        int scorePlayer1 = scores[0];
        int scorePlayer2 = scores[1];
        if (scorePlayer1 > scorePlayer2) {
            alert.setContentText(ConstantsView.namePlayer1 + " Won");
        }
        else if (scorePlayer1 < scorePlayer2) {
            alert.setContentText(ConstantsView.namePlayer2 + " Won");
        }
        else {
            alert.setContentText("ex aequo");
        }
        alert.showAndWait();
    }
}
