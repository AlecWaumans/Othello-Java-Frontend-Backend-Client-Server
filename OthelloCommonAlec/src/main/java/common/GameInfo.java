package common;

import java.io.Serializable;
import java.util.List;

/**
 * Author: Alec Waumans (2025)
 * Serializable snapshot of the current Othello game state.
 * Contains token positions, possible moves, score, next player color, and game status.
 * This object is sent between the server and client to synchronize the game state.
 */
public class GameInfo implements Serializable {

    /** Serialization identifier for network transmission. */
    private static final long serialVersionUID = 1L;

    /** Positions of all black tokens on the board. */
    private final List<PositionDTO> blackTokens;

    /** Positions of all white tokens on the board. */
    private final List<PositionDTO> whiteTokens;

    /** List of possible moves for the next player. */
    private final List<PositionDTO> possibleMoves;

    /** Current score: index 0 for black, index 1 for white. */
    private final int[] score;

    /** Color of the next player to move ("BLACK" or "WHITE"). */
    private final String nextColor;

    /** Current status of the game (running, endgame, surrender). */
    private final GameStatus status;

    /**
     * Constructs a new game state snapshot.
     *
     * @param blackTokens   list of positions of black tokens
     * @param whiteTokens   list of positions of white tokens
     * @param possibleMoves list of valid moves for the next player
     * @param score         the current score, index 0 = black, index 1 = white
     * @param nextColor     the color of the next player ("BLACK" or "WHITE")
     * @param status        the current status of the game
     */
    public GameInfo(List<PositionDTO> blackTokens,
                    List<PositionDTO> whiteTokens,
                    List<PositionDTO> possibleMoves,
                    int[] score,
                    String nextColor,
                    GameStatus status) {
        this.blackTokens = blackTokens;
        this.whiteTokens = whiteTokens;
        this.possibleMoves = possibleMoves;
        this.score = score;
        this.nextColor = nextColor;
        this.status = status;
    }

    /** @return list of positions of all black tokens */
    public List<PositionDTO> getBlackTokens() {
        return blackTokens;
    }

    /** @return list of positions of all white tokens */
    public List<PositionDTO> getWhiteTokens() {
        return whiteTokens;
    }

    /** @return list of possible moves for the next player */
    public List<PositionDTO> getPossibleMoves() {
        return possibleMoves;
    }

    /** @return current score, index 0 = black, index 1 = white */
    public int[] getScore() {
        return score;
    }

    /** @return color of the next player to move ("BLACK" or "WHITE") */
    public String getNextColor() {
        return nextColor;
    }

    /** @return current status of the game */
    public GameStatus getStatus() {
        return status;
    }

    /**
     * Enumeration of possible game statuses.
     */
    public enum GameStatus {
        RUNNING,   // Game is in progress
        ENDGAME,   // Game has ended
        SURRENDER  // A player surrendered
    }
}
