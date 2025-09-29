package othello.view.javaFx;

import client.controller.ClientJavaFx;
import common.GameInfo;
import common.PositionDTO;
import java.util.List;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

/**
 * @author : Sami EL YAGHMOURI (2024) & Alec Waumans (2025) 
 * 
 * The MainPane class represents the main graphical user interface pane for the Othello game. 
 * 
 * Alec:
 * I dropped the Observer pattern because the communication between the client
 * and server indicates when there is a change, and applies the change to the
 * model on the server and to the UI on the client.
 */
public class MainPane extends VBox {

    private final BoardPane boardPane;
    private final ScorePane scorePane;
    private final ButtonPane buttonPane;
    private final AlertBox invalidMove;
    private final MenuBarOption menuBarOption;
    private final Label clientInfoLabel; // Alec : add this attribut for show client information.
    private List<PositionDTO> possibleMoves;

    /**
     * @author : Sami EL YAGHMOURI (2024)& Alec Waumans (2025)
     * Constructs a MainPane object with the specified size.
     *
     * @param size The size of the game board.
     * @param client
     */
    public MainPane(int size, ClientJavaFx client) {
        menuBarOption = new MenuBarOption(client);
        boardPane = new BoardPane(size);
        scorePane = new ScorePane();
        buttonPane = new ButtonPane();
        //Alec : Here, I added the display of the client information on the mainPane with the ID (default 0).
        clientInfoLabel = new Label("Client: not connected");
        this.getChildren().add(0, clientInfoLabel);
        //Alec : End Alec 
        this.getChildren().addAll(menuBarOption, scorePane, boardPane, buttonPane);

        this.setSpacing(10);

        invalidMove = new AlertBox("Invalid Position! "
                + "Select a position from the empty circles.",
                "Warning", "Invalid Move selected");

        menuBarOption.minWidthProperty().bind(this.widthProperty());
        menuBarOption.maxWidthProperty().bind(this.widthProperty());
    }

    /**
     * @author : Sami EL YAGHMOURI (2024)& Alec Waumans (2025) Initializes the
     * client controller and sets up event handlers for user interactions.
     *
     * @param controller The controller managing the client logic
     */
    public void initialize(ClientJavaFx controller) {
        this.boardPane.addEventHandler(MouseEvent.MOUSE_CLICKED,
                e -> {
                    Node node = (Node) e.getTarget();
                    int row = GridPane.getRowIndex(node);
                    int column = GridPane.getColumnIndex(node);
                    //Alec : Use the serializable object PositionDTO. 
                    PositionDTO position = new PositionDTO(column, row, false);
                    //System.out.println("debug initialize (" + position.getCol() + ";" + position.getRow() + ")");
                    // see if it's an impossible move or not and play if is possible.
                    if (possibleMoves != null && possibleMoves.stream().anyMatch(p -> p.equals(position))) {
                        controller.sendPlay(row, column, false);
                    } else {
                        showInvalidMove();
                    }
                    //the client send the message play but with endgame equals true. (i not have implements pass). 
                    if (possibleMoves.isEmpty()) {
                        controller.sendPlay(row, column, true);
                    }
                    //End Alec
                }
        );
        this.buttonPane.init(controller);
    }

    /**
     * @author : Alec Waumans (2025)
     * This function define the information of the client. 
     * @param id : from the client
     * @param email : from the client.
     */
    public void setClientInfo(int id, String email) {
        clientInfoLabel.setText("ID: " + id + " | Email: " + email);
    }

    /**
     * @author : Sami EL YAGHMOURI (2024)
     * Displays an alert box for an invalid move.
     */
    public void showInvalidMove() {
        invalidMove.errorShow();
    }

    /**
     * @author : Alec Waumans (2025)
     * this function is calling by the client when he want to update the interface. 
     * @param info of the actual state of the game. 
     */
    public void updateBoard(GameInfo info) {
        Platform.runLater(() -> {
            this.possibleMoves = info.getPossibleMoves();
            this.boardPane.modify(info);
            this.scorePane.update(info);
        });
    }
}
