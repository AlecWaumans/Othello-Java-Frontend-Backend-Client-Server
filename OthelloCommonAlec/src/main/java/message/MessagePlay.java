package message;

import common.PositionDTO;
import common.User;

/**
 * Author: Alec Waumans (2025)
 * PLAY-type message used to transmit a move to be played in the game.
 * Contains the player's identifier and the coordinates of the move.
 *
 */
public class MessagePlay implements Message {

    /** The player who sent the message (author of the move). */
    private final User author;

    /** The position of the move to be played (row, column, and possible end-of-game flag). */
    private final PositionDTO pos;

    /**
     * Constructs a new PLAY message.
     *
     * @param author the player sending the move
     * @param pos    the position details of the move
     */
    public MessagePlay(User author, PositionDTO pos) {
        this.author = author;
        this.pos = pos;
    }

    /** @return the author of the message */
    @Override
    public User getAuthor() {
        return author;
    }

    /** @return the type of the message, always {@link Type#PLAY} for this class */
    @Override
    public Type getType() {
        return Type.PLAY;
    }

    /**
     * PLAY messages are always addressed to the server (ADMIN user).
     *
     * @return the recipient of the message
     */
    @Override
    public User getRecipient() {
        return User.ADMIN;
    }

    /** @return the move position contained in this message */
    @Override
    public Object getContent() {
        return this.pos;
    }
}
