package othello.model;

import java.util.ArrayList;

/**
 * @author : Sami EL YAGHMOURI (2024) & Alec Waumans (2025)
 * Represents the state of the Othello game, including the positions of black and white tokens,
 * the modified board, possible moves, scores, and the current game state.
 */
public class State {
    private final ArrayList<Position> blackTokens;
    private final ArrayList<Position> whiteTokens;
    private final ArrayList<Position> modifyBoard;
    private final ArrayList<Position> possibleMove;
    private final int[] score;
    private final GameState gameState;
    private final ColorToken colorToken;
    private final ColorToken nextColor;

    /**
     * Alec : Here i had replace to public because i need this for to get all of the state of the game.
     * Like this the server can send a GameInfo to the client with ALL of the information of the current Game. 
     * 
     * Constructs a State object with the specified parameters.
     *
     * @param blackTokens  ArrayList containing positions of black tokens.
     * @param whiteTokens  ArrayList containing positions of white tokens.
     * @param modifyBoard  ArrayList representing the modified board.
     * @param possibleMove ArrayList containing possible moves.
     * @param colorToken   Enum representing the current player's color.
     * @param nextColor    Enum representing the color of the next player.
     * @param score        Array containing the scores of the black and white players.
     * @param gameState    Enum representing the state of the game.
     */
    public State(ArrayList<Position> blackTokens
            , ArrayList<Position> whiteTokens
            , ArrayList<Position> modifyBoard
            , ArrayList<Position> possibleMove
            , ColorToken colorToken, ColorToken nextColor, int[] score, GameState gameState) {
        this.blackTokens = blackTokens;
        this.whiteTokens = whiteTokens;
        this.modifyBoard = modifyBoard;
        this.possibleMove = possibleMove;
        this.colorToken = colorToken;
        this.nextColor = nextColor;
        this.score = score;
        this.gameState = gameState;
    }

    /**
     * Constructs a State object with the specified parameters.
     *
     * @param blackTokens  ArrayList containing positions of black tokens.
     * @param whiteTokens  ArrayList containing positions of white tokens.
     * @param colorToken   Enum representing the current player's color.
     * @param nextColor    Enum representing the color of the next player.
     * @param possibleMove ArrayList containing possible moves.
     * @param score        Array containing the scores of the black and white players.
     */
    public State(ArrayList<Position> blackTokens
            , ArrayList<Position> whiteTokens, ColorToken colorToken, ColorToken nextColor
            , ArrayList<Position> possibleMove, int[] score) {
        this(blackTokens, whiteTokens, new ArrayList<>()
                , possibleMove, colorToken, nextColor, score
                , GameState.RUNNING);
    }

    /**
     * Constructs a State object with the specified parameters.
     *
     * @param modifyBoard ArrayList representing the modified board.
     * @param possibleMove ArrayList containing possible moves.
     * @param colorToken   Enum representing the current player's color.
     * @param nextColor    Enum representing the color of the next player.
     * @param score        Array containing the scores of the black and white players.
     * @param gameState    Enum representing the state of the game.
     */
    public State(ArrayList<Position> modifyBoard
            , ArrayList<Position> possibleMove
            , ColorToken colorToken, ColorToken nextColor, int[] score, GameState gameState) {
        this(new ArrayList<>(), new ArrayList<>(), modifyBoard
                , possibleMove, colorToken, nextColor, score
                , gameState);
    }


    public ArrayList<Position> getModifyBoard() {
        return modifyBoard;
    }

    public ArrayList<Position> getPossibleMove() {
        return possibleMove;
    }

    public int[] getScore() {
        return score;
    }

    public ColorToken getColorToken() {
        return colorToken;
    }
    public ColorToken getNextColor() {
        return nextColor;
    }

    public ArrayList<Position> getBlackTokens() {
        return blackTokens;
    }

    public ArrayList<Position> getWhiteTokens() {
        return whiteTokens;
    }

    public GameState getGameState() {
        return gameState;
    }
}
