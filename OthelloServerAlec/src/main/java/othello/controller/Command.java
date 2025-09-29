package othello.controller;

/**
 * @author : Sami EL YAGHMOURI (2024)
 * Represents a command interface with methods for execution, undoing, and redoing.
 */
public interface Command {
    void execute(); //Executes the command.
    void undo();    //Undoes the command.
    void redo();    //Redoes the command.
}
