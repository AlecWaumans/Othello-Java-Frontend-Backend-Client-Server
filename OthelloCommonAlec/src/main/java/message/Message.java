package message;

import common.User;
import java.io.Serializable;

/**
 * The <code> Message </code> represents a general message send to a user.
 * @author Dr. Robert Lagani&egrave;re
 * @author Dr. Timothy C. Lethbridge
 * @author Fran&ccedil;ois B&eacutel;langer
 * @author Paul Holden
 * @version December 2003
 */
public interface Message extends Serializable {

    /**
     * Return the message type.
     *
     * @return the message type.
     */
    Type getType();

    /**
     * Return the message author.
     *
     * @return the message author.
     */
    User getAuthor();

    /**
     * Return the message recipient.
     *
     * @return the message recipient.
     */
    User getRecipient();

    /**
     * Return the message content.
     *
     * @return the message content.
     */
    Object getContent();

}
