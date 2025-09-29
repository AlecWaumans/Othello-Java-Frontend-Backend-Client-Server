package message;

/**
 * The <code> Type </code> represents the type of a message send between a user
 * and the server.
 *
 * @author Dr. Robert Lagani&egrave;re
 * @author Dr. Timothy C. Lethbridge
 * @author Fran&ccedil;ois B&eacutel;langer
 * @author Paul Holden
 * @author Alec Waumans
 * @version August 2025
 */
public enum Type {

    /**
     * Message containing the profile of a specific user.
     */
    PROFILE,
    /**
     * Message containing the list of all connected users.
     */
    MEMBERS,
    /**
     * Added by Alec Waumans — message sent to play a move in the game.
     */
    PLAY,
    /**
     * Added by Alec Waumans — request to undo the last move.
     */
    UNDO,
    /**
     * Added by Alec Waumans — request to redo the previously undone move.
     */
    REDO,
    /**
     * Added by Alec Waumans — message indicating that the player is quitting the game.
     */
    QUIT,
    /**
     * Added by Alec Waumans — message containing the complete current game state.
     */
    GAMEINFO,
    /**
     * Added by Alec Waumans — message to initialize a new game session.
     */
    INIT,
    /**
     * ALEC58399 - message to delete the historic of the author
     */
    RESET;
}