package message;

import common.User;

/**
 * Author: Alec Waumans (2025)
 * REDO-type message used to request the server to redo the last undone move.
 */
public class MessageRedo implements Message {

    /** The player requesting the redo action. */
    private final User author;

    /**
     * Constructs a new REDO message.
     *
     * @param author the player sending the redo request
     */
    public MessageRedo(User author) {
        this.author = author;
    }

    /** @return the author of the message */
    @Override
    public User getAuthor() {
        return author;
    }

    /** @return the type of the message, always {@link Type#REDO} for this class */
    @Override
    public Type getType() {
        return Type.REDO;
    }

    /**
     * REDO messages are always addressed to the server (ADMIN user).
     *
     * @return the recipient of the message
     */
    @Override
    public User getRecipient() {
        return User.ADMIN;
    }

    /**
     * REDO messages do not contain any additional content.
     *
     * @return null (no extra data)
     */
    @Override
    public Object getContent() {
        return null;
    }
}
