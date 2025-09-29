package othello.controller;

import othello.model.Game;

/**
 * @author Sami EL YAGHMOURI (2024) 
 * Defines a strategy for playing moves in the game.
 */
public interface Strategy {
    void play(Game game);   //Plays a move in the game according to the implemented strategy.
}
