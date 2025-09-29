package message;

import common.User;

/**
 * Message qui est implementer pour faire passée l'information que l'auteur veut ressetée sont historic de jeux
 * au server;
 * @author g58399
 */
public class MessageReset implements Message{
    private final User author;

    public MessageReset(User author) {
        this.author = author;
    }
    
    @Override
    public User getAuthor() {
        return author;
    }

    @Override
    public Type getType() {
        return Type.RESET;
    }

    @Override
    public User getRecipient() {
        return User.ADMIN;
    }

    @Override
    public Object getContent() {
        return this.author;
    }
    
}
