package othello.model;

import java.util.Objects;

/**
 * @author : Sami EL YAGHMOURI (2024)
 * Represents a position on the game board.
 */
public class Position {
    private int x;
    private int y;

    /**
     * Constructs a new Position object with the specified coordinates.
     *
     * @param x The x-coordinate.
     * @param y The y-coordinate.
     */
    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Constructs a new Position object by copying another Position object.
     *
     * @param position The Position object to copy.
     */
    public Position(Position position) {
        this(position.getX(), position.getY());
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    /**
     * Moves the position by the specified offsets.
     *
     * @param x The offset in the x-direction.
     * @param y The offset in the y-direction.
     */
    void movePosition(int x, int y) {
        this.x += x;
        this.y += y;
    }


    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }


    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Position position = (Position) object;
        return x == position.x && y == position.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}
