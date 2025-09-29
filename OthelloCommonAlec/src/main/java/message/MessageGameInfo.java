package message;

import common.GameInfo;
import common.User;

/**
 * @author : Alec Waumans (2025) 
 * Represents a message containing the current game state for a specific player.
 */
public class MessageGameInfo implements Message {

    /** The user who sends the message (e.g., the server or admin). */
    private final User author;

    /** The intended recipient of the message (specific player). */
    private final User recipient;

    /** The detailed state of the game to send. */
    private final GameInfo gameInfo;

    /**
     * Constructs a new MessageGameInfo.
     *
     * @param author    The sender of the message.
     * @param recipient The player who will receive this message.
     * @param gameInfo  The game state data to transmit.
     */
    public MessageGameInfo(User author, User recipient, GameInfo gameInfo) {
        this.author = author;
        this.recipient = recipient;
        this.gameInfo = gameInfo;
    }

    /**
     * Returns the type of the message.
     * In this case, it is always {@link Type#GAMEINFO}.
     *
     * @return The message type.
     */
    @Override
    public Type getType() {
        return Type.GAMEINFO;
    }

    /**
     * Returns the author (sender) of the message.
     *
     * @return The sender's {@link User} object.
     */
    @Override
    public User getAuthor() {
        return author;
    }

    /**
     * Returns the recipient (receiver) of the message.
     *
     * @return The recipient's {@link User} object.
     */
    @Override
    public User getRecipient() {
        return recipient;
    }

    /**
     * Returns the content of the message.
     * In this case, it is the {@link GameInfo} object representing the game state.
     *
     * @return The game state.
     */
    @Override
    public Object getContent() {
        return gameInfo;
    }
}
