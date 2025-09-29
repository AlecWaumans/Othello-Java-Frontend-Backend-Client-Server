package message;

import common.Config;
import common.User;

/**
 * @author : Alec Waumans (2025)
 * Represents an initialization message sent by a player to the server.
 */
public class MessageInit implements Message {

    /** The player who sends the initialization request. */
    private final User author;

    /** The configuration details for the game (board size, mode, etc.). */
    private final Config config;

    /**
     * Constructs a new MessageInit with the specified sender and configuration.
     *
     * @param author The player who sends the message.
     * @param config The game configuration data.
     */
    public MessageInit(User author, Config config) {
        this.author = author;
        this.config = config;
    }

    /**
     * Returns the author (sender) of the message.
     *
     * @return The sender's User object.
     */
    @Override
    public User getAuthor() {
        return author;
    }

    /**
     * Returns the recipient of the message.
     * This is always the server ADMIN USER for initialization messages.
     *
     * @return The server user object.
     */
    @Override
    public User getRecipient() {
        return User.ADMIN;
    }

    /**
     * Returns the type of the message.
     * In this case, it is always INIT. 
     *
     * @return The message type.
     */
    @Override
    public Type getType() {
        return Type.INIT;
    }

    /**
     * Returns the content of the message.
     * Here, it is the config object containing the game settings.
     *
     * @return The game configuration.
     */
    @Override
    public Object getContent() {
        return config;
    }
}
