package othello.controller;

import othello.model.Game;
import othello.model.Position;

/**
 * @author : Sami EL YAGHMOURI (2024)
 * Represents a simple strategy for playing moves in the game.
 */
public class DumbStrategy implements Strategy{

    /**
     * Plays a move in the game using a simple strategy.
     *
     * @param game The game instance to play.
     */
    @Override
    public void play(Game game) {
        Position position = game.getRandomMove();
        Command command = new OthelloCompute(game
                , position.getX(), position.getY(), false);
        command.execute();
    }
}
