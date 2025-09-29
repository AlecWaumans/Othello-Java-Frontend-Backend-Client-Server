package client.controller;

import common.User;
import client.model.OthelloClient;
import message.Message;
import java.util.Observable;
import java.util.Observer;

/**
 * The <code> ChatClientConsole </code> contains all the methods necessary view
 * in console mode the instant messaging client side.
 *
 * @author Dr. Robert Lagani&egrave;re
 * @author Dr. Timothy C. Lethbridge
 * @author Fran&ccedil;ois B&eacutel;langer
 * @author Paul Holden
 * @author Alec Waumans
 * @version August 2025
 */
public class ClientConsole implements Observer {

    private final OthelloClient model;

    /**
     * Alec : i just change the parameter for point on the client i had implement. 
     * Constructs the console view. Subscribes to the instant messaging client.
     *
     * @param client instant messaging client.
     */
    public ClientConsole(OthelloClient client) {
        this.model = client;
        this.model.addObserver(this);
    }

    /**
     * Alec : i just modifie this for have a debug view the connection of the client.
     *
     * @param o
     * @param arg
     */
    @Override
    public void update(Observable o, Object arg) {
        if (arg instanceof Message) {
            Message message = (Message) arg;
            User user = message.getAuthor();
            System.out.println("---- ---- Message received ---- ----");
            System.out.println("Successfully connected!");
            System.out.println("Your ID: " + user.getId());
            System.out.println("Your email: " + user.getName());
        }
    }
}
