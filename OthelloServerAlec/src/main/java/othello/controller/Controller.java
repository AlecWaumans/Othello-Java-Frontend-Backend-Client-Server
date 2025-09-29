package othello.controller;

import othello.model.ColorToken;
import othello.model.Game;
import othello.model.GameState;
import othello.model.State;

/**
 * @author : Sami EL YAGHMOURI (2024) & Alec Waumans (2025) The controller class
 * responsible for managing the game.
 */
public class Controller {

    private Game model;
    private Strategy strategy;
    private int gameId;
    private int numCoup;

    /**
     * @author : Sami EL YAGHMOURI (2024)
     */
    public Controller() {
        //constructor garantit la solidité des attribut
        //c'est pour ca que les parametres doivent etre non NUll
        //Objects.requireNonNull(model, "model Null");
        //Objects.requireNonNull(view, "view Null");
        //this.model = model;
        //this.view = view;
    }

    /**
     * @author : Sami EL YAGHMOURI (2024) & Alec Waumans (2025)
     * Alec : i just initalize the game because is the controlleur only of the game now. 
     * And the observer pattern is delete it also. 
     *
     * @param size The size of the game board.
     */
    public void initialize(int size) {
        this.model = new Game(size);
        model.initialize();
    }
    /**
     * @author : Alec Waumans (2025)
     * @return the current state of the game. 
     */
    public State getState() {
        return model.getCurrentState();
    }
    
    /**
     * @author : Alec Waumans (2025)
     * @return The number of times a player has made a move.
     */
    public int getNumCoup() {
        return this.numCoup;
    }
    
    /**
     * @author : Alec Waumans (2025)
     * @param numCoup 
     */
    public void setNumCoup(int numCoup) {
        this.numCoup = numCoup;
    }
    
    /**
     * @author : Alec Waumans (2025)
     * @return the ID of the Actual Game.
     */
    public int getGameId() {
        return this.gameId;
    }
    
    /**
     * @author : Alec Waumans (2025)
     * @param id of the game. 
     */
    public void setGameId(int id) {
        this.gameId = id;
    }

    /**
     * @author : Sami EL YAGHMOURI (2024)
     * Computes a move in the game.
     *
     * @param x The x-coordinate of the move.
     * @param y The y-coordinate of the move.
     * @param surrender Indicates if the move is a surrender.
     */
    public void compute(int x, int y, boolean surrender) {
        Command command = surrender
                ? new OthelloCompute(model, true)
                : new OthelloCompute(model, x, y, false);

        command.execute();

        if (model.getGameState() == GameState.RUNNING
                && strategy != null
                && model.getCurrentPlayer() == ColorToken.WHITE) {
            strategy.play(model);
        }
    }

    /**
     * @author : Sami EL YAGHMOURI (2024)
     * Redoes the last move in the game.
     */
    public void redo() {
        Command command = new OthelloCompute(model);
        command.redo();
        if (model.getGameState() == GameState.RUNNING && strategy != null) {
            Command commandCPU = new OthelloCompute(model);
            commandCPU.redo();
        }
    }

    /**
     * @author : Sami EL YAGHMOURI (2024)
     * Undoes the last move in the game.
     */
    public void undo() {
        Command command = new OthelloCompute(model);
        command.undo();
        if (model.getGameState() == GameState.RUNNING && strategy != null) {
            Command commandCPU = new OthelloCompute(model);
            commandCPU.undo();
        }
    }

    /**
     * @author : Sami EL YAGHMOURI (2024)
     * Sets the strategy for the game.
     *
     * @param smart Indicates if the strategy is smart.
     */
    public void setStrategy(boolean smart) {
        //System.out.println(smart);
        if (smart) {
            strategy = new PseudoSmartStrategy();
        } else {
            strategy = new DumbStrategy();
        }
    }
}
