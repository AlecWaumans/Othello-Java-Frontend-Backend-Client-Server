package message;

import common.User;

/**
 * Author: Alec Waumans (2025)
 * UNDO-type message used to request the server to undo the last move.
 */
public class MessageUndo implements Message {

    /** The player requesting the undo action. */
    private final User author;

    /**
     * Constructs a new UNDO message.
     *
     * @param author the player sending the undo request
     */
    public MessageUndo(User author) {
        this.author = author;
    }

    /** @return the author of the message */
    @Override
    public User getAuthor() {
        return author;
    }

    /** @return the type of the message, always {@link Type#UNDO} for this class */
    @Override
    public Type getType() {
        return Type.UNDO;
    }

    /**
     * UNDO messages are always addressed to the server (ADMIN user).
     *
     * @return the recipient of the message
     */
    @Override
    public User getRecipient() {
        return User.ADMIN;
    }

    /**
     * UNDO messages do not contain any additional content.
     *
     * @return null (no extra data)
     */
    @Override
    public Object getContent() {
        return null;
    }
}
