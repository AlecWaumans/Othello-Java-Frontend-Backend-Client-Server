package othello.model;

import java.util.*;

/**
 * @author : Sami EL YAGHMOURI (2024) 
 * Represents the game
 * board for Othello.
 */
public class Board {

    private final Map<Position, ColorToken> whiteCase;
    private final Map<Position, ColorToken> blackCase;
    //Also give the number of point won with this move
    private final Map<Position, ArrayList<Position>> possibleMove;

    int sizeBoard;

    /**
     * Constructs a new Board with the specified size.
     *
     * @param sizeBoard The size of the board.
     * @throws IllegalArgumentException If the size is not within the allowed
     * range.
     */
    Board(int sizeBoard) {
        if (sizeBoard < Constants.minSizeBoard
                || sizeBoard > Constants.maxSizeBoard) {
            throw new IllegalArgumentException("size of Board " +
                    "must be between " + Constants.minSizeBoard + " and "
                    + Constants.maxSizeBoard + ": " + sizeBoard);
        }
        this.sizeBoard = sizeBoard;
        this.whiteCase = new HashMap<>();
        this.blackCase = new HashMap<>();
        this.possibleMove = new HashMap<>();

        initialize();
    }

    /**
     * Initializes the board with initial tokens.
     */
    private void initialize() {
        int middle = sizeBoard / 2;
        Position whiteToken1 = new Position(middle - 1, middle - 1);
        Position blackToken1 = new Position(middle - 1, middle);
        Position whiteToken2 = new Position(middle, middle);
        Position blackToken2 = new Position(middle, middle - 1);

        whiteCase.put(whiteToken1, ColorToken.WHITE);
        whiteCase.put(whiteToken2, ColorToken.WHITE);
        blackCase.put(blackToken1, ColorToken.BLACK);
        blackCase.put(blackToken2, ColorToken.BLACK);
        searchPossibleMove(ColorToken.BLACK);
    }

    /**
     * Adds a token to the board and updates the state accordingly.
     *
     * @param position The position to add the token to.
     * @param colorToken The color of the token to add.
     * @return The list of positions taken over by adding the token.
     * @throws IllegalArgumentException If the move is invalid.
     */
    ArrayList<Position> add(Position position, ColorToken colorToken) {
        if (!possibleMove.containsKey(position)) {
            throw new IllegalArgumentException("invalid move: "
                    + "(" + position.getX() + ", " + position.getY() + ")");
        }
        Objects.requireNonNull(blackCase,
                 "blackCase not found");
        Objects.requireNonNull(whiteCase,
                 "whiteCase not found");

        Map<Position, ColorToken> currentPlayer = getMap(colorToken);
        Map<Position, ColorToken> adversePlayer = getAdverseMap(colorToken);
        ArrayList<Position> takeOver = possibleMove.get(position);
        for (Position positionToken : takeOver) {
            currentPlayer.put(positionToken, colorToken);
            adversePlayer.remove(positionToken);
        }
        currentPlayer.put(position, colorToken);
        takeOver.add(position);
        possibleMove.remove(position);

        return takeOver;
    }

    /**
     * Searches for all possible moves for the specified color token on the
     * current board.
     *
     * @param colorToken The color token for which to search possible moves.
     */
    void searchPossibleMove(ColorToken colorToken) {
        Objects.requireNonNull(blackCase,
                 "blackCase not found");
        Objects.requireNonNull(whiteCase,
                 "whiteCase not found");

        possibleMove.clear();

        Map<Position, ColorToken> playerTurn = getMap(colorToken);
        for (Map.Entry<Position, ColorToken> positionToken : playerTurn.entrySet()) {
            possibleAddVerticals(positionToken);
            possibleAddHorizontals(positionToken);
            possibleAddDiagonals(positionToken);
        }
    }

    //Calculates possible moves diagonally from the given position token.
    private void possibleAddDiagonals(Map.Entry<Position, ColorToken> positionToken) {
        Objects.requireNonNull(positionToken,
                 "cannot add diagonally without a position token");
        addPossible(positionToken, 1, 1);
        addPossible(positionToken, -1, -1);
        addPossible(positionToken, 1, -1);
        addPossible(positionToken, -1, 1);
    }

    //Calculates possible moves horizontally from the given position token.
    private void possibleAddHorizontals(Map.Entry<Position, ColorToken> positionToken) {
        Objects.requireNonNull(positionToken,
                 "cannot add horizontally without a position token");
        addPossible(positionToken, 1, 0);
        addPossible(positionToken, -1, 0);
    }

    //Calculates possible moves vertically from the given position token.
    private void possibleAddVerticals(Map.Entry<Position, ColorToken> positionToken) {
        Objects.requireNonNull(positionToken,
                 "cannot add vertically without a position token");
        addPossible(positionToken, 0, 1);
        addPossible(positionToken, 0, -1);
    }

    //Adds possible moves to the board based on the given position token and delta values.
    private void addPossible(Map.Entry<Position, ColorToken> positionToken,
             int deltaX, int deltaY) {
        Objects.requireNonNull(positionToken,
                 "cannot verify is an add is possible without a position token");
        ArrayList<Position> possibleTakeOver = new ArrayList<>();

        int count = 0;
        Position searchPosition = new Position(positionToken.getKey());
        searchPosition.movePosition(deltaX, deltaY);
        Map<Position, ColorToken> adverseMap = getAdverseMap(positionToken.getValue());
        Map<Position, ColorToken> currentPlayer = getMap(positionToken.getValue());
        boolean hasPositionAdverse = adverseMap.containsKey(searchPosition);
        boolean hasPosition = currentPlayer.containsKey(searchPosition);
        while ((searchPosition.getY() < sizeBoard && searchPosition.getY() >= 0)
                && (searchPosition.getX() < sizeBoard && searchPosition.getX() >= 0)
                && hasPositionAdverse && !hasPosition) {
            possibleTakeOver.add(new Position(searchPosition));
            searchPosition.movePosition(deltaX, deltaY);
            count++;
            hasPositionAdverse = adverseMap.containsKey(searchPosition);
            hasPosition = currentPlayer.containsKey(searchPosition);
        }
        addPossibleMap(possibleTakeOver, searchPosition, count, !hasPosition);
    }

    // Adds the list of possible takeovers to the possible move map.
    private void addPossibleMap(ArrayList<Position> possibleTakeOver,
             Position position, int count, boolean canBeAdd) {
        Objects.requireNonNull(possibleTakeOver,
                 "list of possible take over not found");
        Objects.requireNonNull(position,
                 "cannot add without a position");
        if ((position.getY() >= sizeBoard || position.getY() < 0
                || position.getX() >= sizeBoard || position.getX() < 0)
                || count == 0 || !canBeAdd) {
            possibleTakeOver.clear();
        } else if (possibleMove.containsKey(position)) {
            possibleMove.get(position).addAll(possibleTakeOver);
        } else {
            possibleMove.put(position, possibleTakeOver);
        }
    }

    private Map<Position, ColorToken> getAdverseMap(ColorToken colorToken) {
        return switch (colorToken) {
            case BLACK ->
                whiteCase;
            case WHITE ->
                blackCase;
        };
    }

    Map<Position, ColorToken> getMap(ColorToken colorToken) {
        return switch (colorToken) {
            case BLACK ->
                blackCase;
            case WHITE ->
                whiteCase;
        };
    }

    Map<Position, ArrayList<Position>> getMapPossible() {
        return possibleMove;
    }

    ArrayList<Position> getMovePossible() {
        return new ArrayList<>(possibleMove.keySet());
    }

    ArrayList<Position> getPositionsColor(ColorToken colorToken) {
        return switch (colorToken) {
            case BLACK ->
                new ArrayList<>(blackCase.keySet());
            case WHITE ->
                new ArrayList<>(whiteCase.keySet());
        };
    }

    int[] getScore() {
        return new int[]{blackCase.size(), whiteCase.size()};
    }

    /**
     * Sets the board state based on the provided saved state.
     *
     * @param saveState An array containing the saved state of the board. The
     * first element should contain positions of black tokens, and the second
     * element should contain positions of white tokens.
     * @throws NullPointerException If the saved state array is null.
     */
    void setBoard(ArrayList<Position>[] saveState) {
        Objects.requireNonNull(saveState, "Save is null");
        blackCase.clear();
        whiteCase.clear();
        setMap(saveState[0], ColorToken.BLACK);
        setMap(saveState[1], ColorToken.WHITE);
    }

    //Sets the positions of tokens on the board based on the provided list of positions.
    private void setMap(ArrayList<Position> positions, ColorToken colorToken) {
        for (Position position : positions) {
            switch (colorToken) {
                case BLACK ->
                    blackCase.put(position, colorToken);
                case WHITE ->
                    whiteCase.put(position, colorToken);
            }
        }
    }

    /**
     * Gets a random move from the available possible moves on the board.
     *
     * @return A random move from the available possible moves, or null if no
     * moves are available.
     */
    Position getRandomMove() {
        Position randomMove = null;
        int index = 0;
        int randomIndex = (int) (Math.random() * possibleMove.size());

        for (Map.Entry<Position, ArrayList<Position>> position
                : possibleMove.entrySet()) {
            randomMove = position.getKey();
            if (index == randomIndex) {
                return randomMove;
            }
            index++;
        }
        return randomMove;
    }

    /**
     * Gets the move with the maximum number of positions taken over from the
     * available possible moves on the board.
     *
     * @return The move with the maximum number of positions taken over, or null
     * if no moves are available.
     */
    Position getMaxMove() {
        Position maxMove = null;
        int max = -1;

        for (Map.Entry<Position, ArrayList<Position>> position
                : possibleMove.entrySet()) {
            if (position.getValue().size() > max) {
                maxMove = position.getKey();
                max = position.getValue().size();
            }
        }
        return maxMove;
    }
}
