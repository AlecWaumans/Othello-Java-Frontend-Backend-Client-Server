package main;

import common.User;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Observable;
import java.util.Observer;
import java.util.logging.Level;
import java.util.logging.Logger;
import message.Message;
import server.model.Server;
import server.DBManager;

/**
 * The <code> ChatServerConsole </code> contains all the methods necessary view
 * in console mode the instant messaging client side.
 *
 * *@author Dr. Robert Lagani&egrave;re
 * @author Dr. Timothy C. Lethbridge
 * @author Fran&ccedil;ois B&eacutel;langer
 * @author Paul Holden
 * @author Alec Waumans
 * @version august 2025
 *
 * Alec : Every changement i do is specified on the code. if there are no
 * specification it's the code from the other author.
 */
public class ServerConsole implements Observer {

    /**
     * @author : Alec Waumans (2025) & other authors. Entry points to the
     * instant messaging server side.
     *
     * @param args no arguments needed.
     */
    public static void main(String[] args) {
        // Alec: Added to check JDBC connection to othello.db at startup and stop if it fails
        try ( Connection conn = DBManager.getConnection()) {
            System.out.println("JDBC connected to othello.db");
        } catch (SQLException e) {
            System.err.println("JDBC error during startup");
            e.printStackTrace();
            System.exit(1);
        }
        // End Alec 

        try {
            Server model = new Server();
            ServerConsole console = new ServerConsole(model);
            model.addObserver(console);
            System.out.println("Server started");
            System.out.println("");
        } catch (IOException ex) {
            Logger.getLogger(ServerConsole.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(0);
        }
    }

    private final Server model;

    /**
     * Constructs the console view. Subscribes to the instant messaging server.
     *
     * @param model instant messaging server.
     */
    public ServerConsole(Server model) {
        this.model = model;
    }

    @Override
    public void update(Observable o, Object arg) {
        updateUser();
        if (arg != null) {
            Message message = (Message) arg;
            updateMessage(message);
        }
    }

    /**
     * @author : Alec Waumans (2025) & other authors I just change the structure
     * a little bit.
     */
    private void updateUser() {
        StringBuilder builder = new StringBuilder();

        builder.append("\n").append("List of connected users\n");
        builder.append("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n");
        builder.append(String.format("%-5s %-15s %-20s\n", "ID", "IP", "Name"));

        for (User member : model.getMembers()) {
            builder.append(String.format("%-5d %-15s %-20s\n",
                    member.getId(),
                    member.getAddress(),
                    member.getName()));
        }

        builder.append("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n");

        System.out.println(builder);
    }

    /**
     * @author : Alec Waumans (2025) & other authors I just change the structure
     * a little bit.
     * @param message
     */
    private void updateMessage(Message message) {
        StringBuilder builder = new StringBuilder();
        builder.append("\n---- ---- Message received ---- ----\n");
        builder.append(LocalDateTime.now()).append(" \n");
        builder.append("Type: ").append(message.getType()).append("\n");
        builder.append("From: ").append(message.getAuthor()).append("\t");
        builder.append("\n");
        System.out.println(builder);
    }
}
