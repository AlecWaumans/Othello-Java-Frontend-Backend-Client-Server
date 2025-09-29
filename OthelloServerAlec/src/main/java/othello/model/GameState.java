package othello.model;

/**
 * @author : Sami EL YAGHMOURI (2024)
 * Represents the possible states of the game.
 */
public enum GameState {
    RUNNING,    //The game is currently running.
    ENDGAME,    //The game has ended.
    SURRENDER,  //One of the players has surrendered.
}
