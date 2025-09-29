package common;

import java.io.Serializable;

/**
 * Author: Alec Waumans (2025)
 * 
 * Data Transfer Object (DTO) representing a position on the Othello board.
 * Contains the row and column coordinates, as well as a flag indicating
 * whether this position is part of an end-of-game action (e.g., surrender).
 * Implements Serializable to allow network transmission between
 * client and server.
 *
 */
public class PositionDTO implements Serializable {

    /** Serialization identifier for network transfer. */
    private static final long serialVersionUID = 1L;

    /** The row index of the position (0-based). */
    private int row;

    /** The column index of the position (0-based). */
    private int col;

    /** True if this position is used to indicate the end of the game. */
    private boolean endGame;

    /**
     * Constructs a new PositionDTO.
     *
     * @param row     the row index of the position
     * @param col     the column index of the position
     * @param endGame true if this position marks an end-of-game event
     */
    public PositionDTO(int row, int col, boolean endGame) {
        this.row = row;
        this.col = col;
        this.endGame = endGame;
    }

    /** @return the row index of this position */
    public int getRow() {
        return row;
    }

    /** @return the column index of this position */
    public int getCol() {
        return col;
    }

    /** @return true if this position marks an end-of-game event */
    public boolean getEndGame() {
        return this.endGame;
    }

    /**
     * Generates a hash code based on row and column.
     *
     * @return hash code for this position
     */
    @Override
    public int hashCode() {
        int hash = 5;
        hash = 97 * hash + this.row;
        hash = 97 * hash + this.col;
        return hash;
    }

    /**
     * Compares this position with another object for equality.
     * Two PositionDTO objects are equal if they have the same row and column.
     *
     * @param obj the object to compare
     * @return true if the positions are equal, false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final PositionDTO other = (PositionDTO) obj;
        if (this.row != other.row) {
            return false;
        }
        return this.col == other.col;
    }
}
