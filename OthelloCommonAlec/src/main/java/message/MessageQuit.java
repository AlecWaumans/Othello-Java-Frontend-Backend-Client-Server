package message;

import common.User;

/**
 * Author: Alec Waumans (2025)
 * QUIT-type message used to notify the server that a player wishes to leave the game.
 */
public class MessageQuit implements Message {

    /** The player who is quitting. */
    private final User author;

    /**
     * Constructs a new QUIT message.
     *
     * @param author the player sending the quit notification
     */
    public MessageQuit(User author) {
        this.author = author;
    }

    /** @return the author of the message */
    @Override
    public User getAuthor() {
        return author;
    }

    /** @return the type of the message, always {@link Type#QUIT} for this class */
    @Override
    public Type getType() {
        return Type.QUIT;
    }

    /**
     * QUIT messages are always addressed to the server (ADMIN user).
     *
     * @return the recipient of the message
     */
    @Override
    public User getRecipient() {
        return User.ADMIN;
    }

    /**
     * QUIT messages do not contain any additional content.
     *
     * @return null (no extra data)
     */
    @Override
    public Object getContent() {
        return null;
    }
}
