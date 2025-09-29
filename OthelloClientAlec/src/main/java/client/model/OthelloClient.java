package client.model;

import common.Members;
import common.User;
import client.AbstractClient;
import client.controller.ClientJavaFx;
import common.GameInfo;
import message.Message;
import message.MessageProfile;
import message.Type;
import java.io.IOException;

/**
 * The <code> ChatClient </code> contains all the methods necessary to set up a
 * Instant messaging client.
 *
 * @author Dr. Robert Lagani&egrave;re
 * @author Dr. Timothy C. Lethbridge
 * @author Fran&ccedil;ois B&eacutel;langer
 * @author Paul Holden
 * @author Alec Waumans
 * @version august 2025
 *
 *  Alec : When i had change the code is specified by comment on the code !
 */
public class OthelloClient extends AbstractClient {

    private final Members members;
    private User mySelf;
    private boolean isConnectedSuccessfully = false; // Add by Alec
    private ClientJavaFx controller; // Add by Alec
    // Alec : Like this i separate the view from the client. 

    /**
     * Constructs the client. Opens the connection with the server. Sends the
     * user name inside a <code> MessageProfile </code> to the server. Builds an
     * empty list of users.
     *
     * @param host the server's host name.
     * @param port the port number.
     * @param name the name of the user.
     * @param password the password needed to connect.
     * @throws IOException if an I/O error occurs when opening.
     */
    public OthelloClient(String host, int port, String name, String password) throws IOException {
        super(host, port);
        openConnection();
        updateName(name);
        members = new Members();
    }

    /**
     * @author : Create by other, Modify by Alec Waumans (2025) 
     * This methode is to define how the
     * client must to react when we receive a specific message from the server.
     *
     * @param msg : message receive by the servor.
     */
    @Override
    protected void handleMessageFromServer(Object msg) {
        Message message = (Message) msg;
        Type type = message.getType();
        switch (type) {
            // Alec : when we receive the message of type PROFILE from the server. 
            case PROFILE -> {
                setMySelf(message.getAuthor());
                members.add(getMySelf());
                notifyChange(message);
                isConnectedSuccessfully = true;
                break;
            }
            // Alec : when we receive the message of type GAMEINFO from the server. 
            case GAMEINFO -> {
                GameInfo info = (GameInfo) message.getContent();

                System.out.println("---- GAMEINFO received ----");
                System.out.println("Game status: " + info.getStatus());
                System.out.println("Score: " + info.getScore()[0] + " - " + info.getScore()[1]);
                System.out.println("Next player's color: " + info.getNextColor());
                System.out.println("Number of possible moves: " + info.getPossibleMoves().size());
                System.out.println("---------------------------");

                if (controller != null) {
                    controller.updateBoard(info);
                }
                break;
            }
            // End Alec
            // NOT CHANGED
            case MEMBERS -> {
                Members members = (Members) message.getContent();
                updateMembers(members);
                break;
            }
            //58399 Alec 
            case INIT -> {
                System.out.println("j'ai recu le count");
            }

            default ->
                throw new IllegalArgumentException("Message type unknown " + type);
        }
    }

    /**
     * @author : Alec Waumans (2025) i add this function. 
     *
     * @param controller
     */
    public void setController(ClientJavaFx controller) {
        this.controller = controller;
    }

    /**
     * Quits the client and closes all aspects of the connection to the server.
     *
     * @throws IOException if an I/O error occurs when closing.
     */
    public void quit() throws IOException {
        closeConnection();
    }

    /**
     * Return all the connected users.
     *
     * @return all the connected users.
     */
    public Members getMembers() {
        return members;
    }

    /**
     * Return the user with the given id.
     *
     * @param id of the user.
     * @return the user with the given id.
     */
    public User getUser(int id) {
        return members.getUser(id);
    }

    /**
     * Return the user.
     *
     * @return the user.
     */
    public User getMySelf() {
        return mySelf;
    }

    /**
     * @Author : Modified by Alec Waumans (2025) On private
     *
     * @param user
     */
    private void setMySelf(User user) {
        this.mySelf = user;
    }

    void updateMembers(Members members) {
        this.members.clear();
        for (User member : members) {
            this.members.add(member);
        }
        notifyChange();
    }

    private void updateName(String name) throws IOException {
        sendToServer(new MessageProfile(0, name));
    }

    private void notifyChange() {
        setChanged();
        notifyObservers();
    }

    private void notifyChange(Message message) {
        setChanged();
        notifyObservers(message);
    }

    /**
     * Return the numbers of connected users.
     *
     * @return the numbers of connected users.
     */
    public int getNbConnected() {
        return members.size();
    }
    /**
     * @author : Alec Waumans (2025) I add this function.
     * Interrupt the thread of the client for give the chance of the server to answer after send profile message
     * like this we receive a confirmation from the server the client is really connected before to continue the program. 
     */
    public boolean connectAndCheckProfile() {
        try {
            for (int i = 0; i < 30; i++) {
                Thread.sleep(100); // 100ms
                if (isConnectedSuccessfully) {
                    return true;
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return false;
    }

}
