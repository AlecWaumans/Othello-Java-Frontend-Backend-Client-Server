package othello.controller;


import othello.model.Game;
import othello.model.Position;

/**
 * @author Sami EL YAGHMOURI (2024) 
 * Represents a pseudo-smart strategy for playing moves in the game.
 */
public class PseudoSmartStrategy implements Strategy {

    /**
     * Plays a move in the game using a pseudo-smart strategy.
     *
     * @param game The game instance to play.
     */
    @Override
    public void play(Game game) {
        Position position = game.getMaxMove();
        Command command = new OthelloCompute(game
                , position.getX(), position.getY(), false);
        command.execute();
    }
}
