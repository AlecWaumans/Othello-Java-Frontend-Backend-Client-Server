package othello.view.javaFx;

import common.GameInfo;
import common.PositionDTO;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.layout.*;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.List;

/**
 * @author : Sami EL YAGHMOURI (2024) & Alec Waumans (2025) The BoardPane class
 * represents the graphical user interface component responsible for displaying
 * the game board.
 */
public class BoardPane extends GridPane {

    private final int size;
    private final Circle[][] circles;
    private List<PositionDTO> previousPossibleMove;

    /**
     * @author : Sami EL YAGHMOURI (2024) Constructs a BoardPane object with the
     * specified size.
     *
     * @param size The size of the game board.
     */
    public BoardPane(int size) {
        this.size = size;
        this.circles = new Circle[size][size];
        this.previousPossibleMove = new ArrayList<>();

        createBoard();

        this.setGridLinesVisible(true);
        this.setAlignment(Pos.CENTER);
        this.setPadding(new Insets(0, 10, 0, 10));
    }

    /* 
     * @author : Sami EL YAGHMOURI (2024)
     * Creates the initial game board by adding rectangles and circles to represent each cell.
     * Each cell is represented by a rectangle and a circle.
     * The circles are added on top of the rectangles to allow interaction.
     * The size of the grid is determined by the 'size' parameter of the class.
     */
    private void createBoard() {
        for (int row = 0; row < size; row++) {
            RowConstraints rowGrid = new RowConstraints(ConstantsView.sizeGrid);
            rowGrid.setValignment(VPos.CENTER);
            this.getRowConstraints().add(rowGrid);
            ColumnConstraints colGrid = new ColumnConstraints(ConstantsView.sizeGrid);
            colGrid.setHalignment(HPos.CENTER);
            this.getColumnConstraints().add(colGrid);
            for (int col = 0; col < size; col++) {
                Rectangle rectangle = new Rectangle(ConstantsView.sizeGrid,
                        ConstantsView.sizeGrid, ConstantsView.colorGrid);
                Circle circle = new Circle(ConstantsView.radiusCircle, ConstantsView.colorGrid);
                circles[row][col] = circle;
                this.add(rectangle, row, col);
                this.add(circle, row, col);
            }
        }
    }

    /**
     * @author : Sami EL YAGHMOURI (2024) & Alec Waumans (2025)
     *
     * Alec : We do not use the game model directly, but a serializable object
     * for client-server communication. So I replaced state with gameInfo and
     * now use gameInfo.getStatus() for debugging purposes. And we use this
     * function to call a function to update the board on UI when the state of
     * the game is running.
     *
     * @param state The modified state of the game.
     */
    void modify(GameInfo gameInfo) {
        switch (gameInfo.getStatus()) {
            case RUNNING:
                //System.out.println(gameInfo.getStatus());
                setBoard(gameInfo);
                break;
            case ENDGAME:
                //System.out.println(gameInfo.getStatus());
                setBoard(gameInfo);
                displayEndGame(gameInfo);
                break;
            case SURRENDER:
                //System.out.println(gameInfo.getStatus());
                displayEndGame(gameInfo);
                break;
        }
    }

    /**
     * @author : Sami EL YAGHMOURI (2024) & Alec Waumans (2025)
     *
     * @param gameInfo : information of the game.
     *
     * Alec : Removed the option to exit the application, as I prefer to decide
     * when to quit it. If we want to improve the app, the user should have the
     * possibility to start a new game. I also replaced state with gameInfo.
     *
     * Displays the end game dialog with information about the winner.
     *
     */
    private void displayEndGame(GameInfo gameInfo) {
        InfoBoxWinner info = new InfoBoxWinner(gameInfo.getScore());
    }

    /**
     * @author : Sami EL YAGHMOURI (2024) & Alec Waumans (2025) Updates the game
     * board display based on the provided GameInfo object. Retrieves the
     * positions of black tokens, white tokens, and possible moves, then updates
     * the visual representation accordingly.
     *
     * @param info GameInfo object containing the current game state and next
     * player color
     */
    public void setBoard(GameInfo info) {
        // Alec Waumans (2025) Change: use of GameInfo instead of a simple state to facilitate client-server communication,
        // as well as the use of PositionDTO instead of the Position object from the game model

        List<PositionDTO> blackTokens = info.getBlackTokens();
        List<PositionDTO> whiteTokens = info.getWhiteTokens();
        List<PositionDTO> possibleMove = info.getPossibleMoves();

        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                int finalCol = col;
                int finalRow = row;

                boolean isBlack = blackTokens.stream()
                        .anyMatch(p -> p.getCol() == finalCol && p.getRow() == finalRow);
                boolean isWhite = whiteTokens.stream()
                        .anyMatch(p -> p.getCol() == finalCol && p.getRow() == finalRow);
                boolean isPossible = possibleMove.stream()
                        .anyMatch(p -> p.getCol() == finalCol && p.getRow() == finalRow);

                if (isBlack) {
                    circles[row][col].setFill(ConstantsView.colorPlayer1);
                    circles[row][col].setStroke(ConstantsView.colorGrid);
                } else if (isWhite) {
                    circles[row][col].setFill(ConstantsView.colorPlayer2);
                    circles[row][col].setStroke(ConstantsView.colorGrid);
                } else if (isPossible) {
                    circles[row][col].setFill(ConstantsView.colorGrid);

                    switch (info.getNextColor()) {
                        case "BLACK" ->
                            circles[row][col].setStroke(ConstantsView.colorPlayer1);
                        case "WHITE" ->
                            circles[row][col].setStroke(ConstantsView.colorPlayer2);
                    }
                } else {
                    circles[row][col].setFill(ConstantsView.colorGrid);
                    circles[row][col].setStroke(ConstantsView.colorGrid);
                }
            }
        }

        previousPossibleMove = possibleMove;
        // End Alec
    }
}
