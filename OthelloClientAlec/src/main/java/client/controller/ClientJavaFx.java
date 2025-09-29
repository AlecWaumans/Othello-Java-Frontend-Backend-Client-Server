package client.controller;

import client.model.OthelloClient;
import common.Config;
import common.GameInfo;
import common.PositionDTO;
import common.User;
import java.io.IOException;
import message.MessageInit;
import message.MessagePlay;
import message.MessageQuit;
import message.MessageRedo;
import message.MessageReset;
import message.MessageUndo;
import othello.view.javaFx.MainPane;

/**
 * JavaFX view controller for the Othello client.
 * <p>
 * Responsibilities:
 * <ul>
 *   <li>Bridges the JavaFX view and the network client.</li>
 *   <li>Sends user intentions to the server (init, play, undo, redo, quit).</li>
 *   <li>Receives model snapshots (GameInfo) from the client and forwards them to the view.</li>
 * </ul>
 *
 * Author: Alec Waumans (2025)
 */
public class ClientJavaFx {

    /** Network client used to communicate with the server. */
    private final OthelloClient client;

    /** Reference to the main JavaFX view. Set in {@link #initialize(MainPane, int)}. */
    private MainPane view;

    /** Whether the client runs in CPU mode (AI plays locally on the client side). */
    private boolean cpuMode;

    /** Whether the CPU uses the "smart" strategy. */
    private boolean smartCpu;

    /**
     * Constructs the JavaFX controller with the given network client.
     *
     * @param client the Othello network client used to send/receive messages
     */
    public ClientJavaFx(OthelloClient client) {
        this.client = client;
    }

    /**
     * Initializes the controller with the provided view and board size.
     * Also resolves and displays the current user's info on the view.
     *
     * @param view the main view bound to this controller
     * @param size the board size (forwarded to the view if needed)
     */
    public void initialize(MainPane view, int size) {
        this.view = view;

        // Wire the controller into the view (allows the view to call back).
        view.initialize(this);

        // Important: resolve the current user through the client registry
        int id = client.getMySelf().getId(); // obtain current user's ID
        User user = client.getUser(id);      // resolve full user object

        if (user != null) {
            // Show the client's identity on the UI (e.g., header/status area)
            view.setClientInfo(user.getId(), user.getName());
        } else {
            System.err.println("User not found in the member list!");
        }
    }

    /**
     * Enables CPU mode and selects the CPU strategy.
     *
     * @param smart true to enable the smart CPU strategy; false for the basic one
     */
    public void setStrategy(boolean smart) {
        this.cpuMode = true;
        this.smartCpu = smart;
    }

    /** @return true if CPU mode is enabled. */
    public boolean isCpu() {
        return cpuMode;
    }

    /** @return true if the "smart" CPU strategy is enabled. */
    public boolean isSmartCpu() {
        return smartCpu;
    }

    /**
     * Sends an INIT message to the server to start a session/game with the given config.
     *
     * @param config the initial configuration (board size, CPU flags, etc.)
     */
    public void sendInit(Config config) {
        try {
            MessageInit initMessage = new MessageInit(client.getMySelf(), config);
            client.sendToServer(initMessage);
        } catch (IOException e) {
            System.err.println("Error while sending INIT message: " + e.getMessage());
        }
    }

    /**
     * Sends a PLAY message to the server.
     *
     * @param x       column (0-based)
     * @param y       row (0-based)
     * @param endGame true if the action represents a surrender or end-of-game signal
     */
    public void sendPlay(int x, int y, boolean endGame) {
        try {
            MessagePlay playMessage = new MessagePlay(client.getMySelf(), new PositionDTO(x, y, endGame));
            client.sendToServer(playMessage);
        } catch (IOException e) {
            System.err.println("Error while sending PLAY message: " + e.getMessage());
        }
    }

    /**
     * Sends an UNDO request to the server.
     */
    public void sendUndo() {
        try {
            MessageUndo undoMessage = new MessageUndo(client.getMySelf());
            client.sendToServer(undoMessage);
        } catch (IOException e) {
            System.err.println("Error while sending UNDO message: " + e.getMessage());
        }
    }

    /**
     * Sends a REDO request to the server.
     */
    public void sendRedo() {
        try {
            MessageRedo redoMessage = new MessageRedo(client.getMySelf());
            client.sendToServer(redoMessage);
        } catch (IOException e) {
            System.err.println("Error while sending REDO message: " + e.getMessage());
        }
    }

    /**
     * Sends a QUIT notification to the server.
     */
    public void sendQuit() {
        try {
            MessageQuit quitMessage = new MessageQuit(client.getMySelf());
            client.sendToServer(quitMessage);
        } catch (IOException e) {
            System.err.println("Error while sending QUIT message: " + e.getMessage());
        }
    }
    
    /**
     * sends a reset notification to the server
     */
    public void sendReset() {
        try {
            MessageReset resetMessage = new MessageReset(client.getMySelf());
            client.sendToServer(resetMessage);
        } catch (IOException e) {
            System.err.println("Error while sending QUIT message: " + e.getMessage());
        }
    }

    /**
     * Forwards a fresh {@link GameInfo} snapshot to the view so it can update the board.
     *
     * @param info the latest game state received from the server
     */
    public void updateBoard(GameInfo info) {
        if (view != null) {
            // Delegate UI rendering to the JavaFX view component
            view.updateBoard(info);
        }
    }

    /**
     * Requests a graceful disconnect: notify the server and close the client.
     *
     * @throws IOException if the underlying client fails to close
     */
    public void disconnect() throws IOException {
        // Inform the server that this client is quitting
        this.sendQuit();

        // Close network resources
        this.client.quit();
    }
}
