package othello.controller;

import othello.model.Game;
import othello.model.Position;

/**
 * @author : Sami EL YAGHMOURI (2024) & Alec Waumans (2025) 
 * Represents a command to compute moves in the Othello game.
 */
public class OthelloCompute implements Command{

    private final Game model;
    private final Position position;
    private final boolean surrender;
    //Alec : delete the view because in the client-server architecture 
    //the server not must to have acces to the view.

    /**
     * @author : Sami EL YAGHMOURI (2024) & Alec Waumans (2025)
     * Alec : I delete the view on the constructor. 
     * Constructs an OthelloCompute command with the specified parameters.
     *
     * @param model     The game model.
     * @param x         The x-coordinate of the move.
     * @param y         The y-coordinate of the move.
     * @param surrender Indicates whether the move is a surrender.
     */
    public OthelloCompute(Game model, int x, int y, boolean surrender) {
        this.model = model;
        this.position = new Position(x, y);
        this.surrender = surrender;
    }


    /**
     * @author : Sami EL YAGHMOURI (2024) & Alec Waumans (2025)
     * Constructs an OthelloCompute command with the specified parameters.
     *
     * @param model     The game model.
     * @param surrender Indicates whether the move is a surrender.
     */
    public OthelloCompute(Game model, boolean surrender) {
        //Alec: I delete the null because it was the view from the constructor i had delete.
        this(model, 0, 0, surrender);
        //End Alec
    }

    /**
     * @author : Sami EL YAGHMOURI (2024) & Alec Waumans (2025)
     * Constructs an OthelloCompute command with the specified parameters.
     *
     * @param model The game model.
     */
    public OthelloCompute(Game model) {
        //Alec: I delete the null because it was the view from the constructor i had delete
        this(model, 0, 0, false);
        //End Alec
    }
    
    /**
     * @author : Sami EL YAGHMOURI (2024) & Alec Waumans (2025)
     */
    @Override
    public void execute() {
        // Alec : the logic change here because no acces to view i just delete the try catch. 
        // if it's a invalid move the client will detect that on his side. 
        model.addPiece(position, surrender);
    }
    

    /**
     * @author : Sami EL YAGHMOURI (2024)
     */
    @Override
    public void undo() {
        model.undoGame();
    }
    
    /**
     * @author : Sami EL YAGHMOURI (2024)
     */
    @Override
    public void redo() {
        model.redoGame();
    }
}
