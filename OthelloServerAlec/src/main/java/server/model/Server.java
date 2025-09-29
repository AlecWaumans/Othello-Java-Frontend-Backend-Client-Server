package server.model;

import server.ConnectionToClient;
import common.Config;
import common.GameInfo;
import common.Members;
import common.PositionDTO;
import common.User;
import message.Message;
import message.MessageMembers;
import message.MessageProfile;
import message.Type;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import message.MessageGameInfo;
import message.MessageReset;
import othello.controller.Controller;
import othello.model.State;
import server.AbstractServer;
import server.GameDAO;
import server.ResetDAO;
import server.ScoreDAO;
import server.StateMapper;
import server.UserDAO;

/**
 * The <code> ChatServer </code> contains all the methods necessary to set up a
 * Instant messaging server.
 *
 * @author Dr. Robert Lagani&egrave;re
 * @author Dr. Timothy C. Lethbridge
 * @author Fran&ccedil;ois B&eacutel;langer
 * @author Paul Holden
 * @author Alec Waumans
 * @version August 2025
 *
 * Alec : Every changement i do is specified on the code. if there are no
 * specification it's the code from the other author.
 */
public class Server extends AbstractServer {

    private static final int PORT = 12345;
    static final String ID_MAPINFO = "ID";

    // Alec: store active Othello controllers per connected user
    private final Map<Integer, Controller> controllers = new HashMap<>();

    private final Members members;

    /**
     * Returns the local machine's LAN IP address.
     *
     * @return the site-local {@link InetAddress}, or {@code null} if not found.
     */
    private static InetAddress getLocalAddress() {
        try {
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            while (interfaces.hasMoreElements()) {
                for (InterfaceAddress addr : interfaces.nextElement().getInterfaceAddresses()) {
                    if (addr.getAddress().isSiteLocalAddress()) {
                        return addr.getAddress();
                    }
                }
            }
        } catch (SocketException e) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, "NetworkInterface error", e);
        }
        return null;
    }

    /**
     * @author : Alec Waumans (2025) & others authors i had delete the client id
     * because i increment and choose the id directly on the db. Constructs the
     * server, binds it to the default port, and starts listening for connection
     * requests.
     *
     * @throws IOException if an I/O error occurs when creating the server
     * socket.
     */
    public Server() throws IOException {
        super(PORT);
        members = new Members();
        this.listen();
    }

    /**
     * @return the list of currently connected users.
     */
    public Members getMembers() {
        return members;
    }

    /**
     * @return the server's IP address, or "Unknown" if it cannot be determined.
     */
    public String getIP() {
        InetAddress address = getLocalAddress();
        return (address != null) ? address.getHostAddress() : "Unknown";
    }

    /**
     * @return the number of currently connected clients.
     */
    public int getNbConnected() {
        return getNumberOfClients();
    }

    /**
     * Stops the server and closes all client connections.
     *
     * @throws IOException if an I/O error occurs during shutdown.
     */
    public void quit() throws IOException {
        this.stopListening();
        this.close();
    }

    /**
     * @author : Alec Waumans (2025) & others authors Message handling is
     * significantly different from older version: - Supports PROFILE with DB
     * persistence and unique-session enforcement. - Supports Othello
     * INIT/PLAY/UNDO/REDO with DB persistence for game/score. - MEMBERS
     * passthrough; QUIT closes client.
     *
     * Handles all incoming messages from clients.
     *
     * @param msg the message object received from a client.
     * @param client the {@link ConnectionToClient} instance representing the
     * sender.
     */
    @Override
    protected void handleMessageFromClient(Object msg, ConnectionToClient client) {
        Message message = (Message) msg;
        Type type = message.getType();

        switch (type) {
            case PROFILE -> {
                // Alec: persist/find user and enforce single active session per user 
                User author = message.getAuthor();
                int memberId;

                try {
                    memberId = UserDAO.findOrCreate(author.getName());
                } catch (SQLException e) {
                    e.printStackTrace();
                    return;
                }
                //End Alec 

                // Alec: Automatically close any older connection using the same user ID (session deduplication)
                for (Thread t : getClientConnections()) {
                    ConnectionToClient other = (ConnectionToClient) t;
                    Object id = other.getInfo(ID_MAPINFO);
                    if (id instanceof Integer oldId && oldId == memberId && other != client) {
                        System.out.println("Old session detected for ID " + memberId + ", attempting to close...");

                        if (other.isConnected()) {
                            try {
                                other.close();
                                System.out.println("Old session closed for ID " + memberId);
                            } catch (IOException ex) {
                                System.err.println("Error closing old session for ID: " + memberId);
                            }
                        } else {
                            System.out.println("Session already closed for ID " + memberId);
                        }
                    }
                }
                // End Alec

                // Alec: attach ID to connection + add to members
                client.setInfo(ID_MAPINFO, memberId);
                User user = new User(memberId, author.getName(), client.getInetAddress());
                members.add(user);
                //End Alec

                // Alec: acknowledge profile to client
                MessageProfile messageName = new MessageProfile(memberId, author.getName());
                try {
                    client.sendToClient(messageName);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                //End Alec 
            }

            case INIT -> {
                // Alec: start an Othello game session, configure CPU strategy, and persist a Game row. 
                Config config = (Config) message.getContent();

                System.out.println("------ CONFIGURATION RECEIVED ------");
                System.out.println("Author: " + message.getAuthor().getName());
                System.out.println("Board size: " + config.getBoardSize());
                System.out.println("Mode: " + (config.isCpuMode() ? "CPU" : "Multiplayer"));
                if (config.isCpuMode()) {
                    System.out.println("CPU difficulty: " + (config.isSmartCPU() ? "Smart" : "Dumb"));
                }
                System.out.println("------------------------------------");

                // Alec : Create and initialize a new game controller
                Controller ctrl = new Controller();
                ctrl.initialize(config.getBoardSize());

                // Alec : Configure CPU strategy if needed
                if (config.isCpuMode()) {
                    ctrl.setStrategy(config.isSmartCPU());
                }

                // Alec :  Save game record in the database
                int userId = message.getAuthor().getId();
                String timeStart = LocalDateTime.now().toString();
                int gameId = -1;
                try {
                    gameId = GameDAO.insertGame(userId, timeStart);
                } catch (SQLException e) {
                    System.err.println("Error persisting Game:");
                    e.printStackTrace();
                }
                ctrl.setGameId(gameId);

                // Alec : Store controller in map
                controllers.put(userId, ctrl);

                // Alec : Send initial game state to the client
                sendGameInfo(message.getAuthor(), ctrl);
                // End Alec
            }

            case PLAY -> {
                // Alec: compute move in Othello controller and persist score snapshot
                PositionDTO pos = (PositionDTO) message.getContent();
                System.out.println("------ PLAY RECEIVED ------");
                System.out.println("Author: " + message.getAuthor().getName());
                System.out.println("Position: (" + pos.getCol() + ";" + pos.getRow() + ")");
                System.out.println("isEndGame: " + pos.getEndGame());
                System.out.println("----------------------------------");

                Controller ctrl = controllers.get(message.getAuthor().getId());
                if (ctrl == null) {
                    return;
                }

                // Alec : ompute move and send updated state
                ctrl.compute(pos.getCol(), pos.getRow(), pos.getEndGame());
                sendGameInfo(message.getAuthor(), ctrl);
                ctrl.setNumCoup(ctrl.getNumCoup() + 1);

                // Alec : Persist score
                try {
                    int gameId = ctrl.getGameId();
                    int num = ctrl.getNumCoup();
                    int[] score = ctrl.getState().getScore();
                    int scoreJ = score[0];
                    int scoreIA = score[1];
                    String gameState = ctrl.getState().getGameState().toString();
                    String timeS = LocalDateTime.now().toString();

                    ScoreDAO.insertScore(gameId, num, scoreJ, scoreIA, gameState, timeS);
                    System.out.println("Score saved: Turn " + num);
                } catch (Exception e) {
                    System.err.println("Error saving score:");
                    e.printStackTrace();
                }
                // End Alec
            }

            case UNDO -> {
                // Alec: undo last move via controller
                Controller ctrl = controllers.get(message.getAuthor().getId());
                if (ctrl != null) {
                    ctrl.undo();
                    sendGameInfo(message.getAuthor(), ctrl);
                }
                // End Alec
            }

            case REDO -> {
                // Alec: redo move via controller
                Controller ctrl = controllers.get(message.getAuthor().getId());
                if (ctrl != null) {
                    ctrl.redo();
                    sendGameInfo(message.getAuthor(), ctrl);
                }
                // End Alec
            }

            case QUIT -> {
                // Alec: gracefully close client
                try {
                    client.close();
                } catch (IOException ex) {
                    Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
                }
                // End Alec
            }

            case MEMBERS -> {
                // Alec : I not have implement this part because it was not 
                // asked on the project but i keep if we must to improve the project. 
            }
            
            //Alec : J'ai rajouter tout ca pour la gestion du reset dans la database.
            case RESET -> {
                Controller ctrl = controllers.get(message.getAuthor().getId());
                if (ctrl == null) {
                    return;
                }
                // Alec : Persist reset
                try {
                    int id = 0;
                    int player = message.getAuthor().getId();
                    String timeS = LocalDateTime.now().toString();

                    ResetDAO.insertReset(id, player, timeS);
                    System.out.println("Insertion du reset dans la database");
                    
                    int countReset = ResetDAO.findNumbReset(player);
                    sendResetCount(message.getAuthor(), countReset);
                     System.out.println("Envoie du message au ");
                } catch (Exception e) {
                    System.err.println("Error reset:");
                    e.printStackTrace();
                }
                // End Alec
            }

            default ->
                throw new IllegalArgumentException("Unknown message type: " + type);
        }

        setChanged();
        notifyObservers(message);
    }

    /**
     * @author : Alec Waumans (2025) & others authors
     * 
     * Alec : Does not create incremental IDs. Broadcasts members list on connect 
     * (same idea), but profile/ID are set in PROFILE flow.
     * 
     * Called when a client successfully connects. Sends the current member list
     * to all clients.
     *
     * @param client the newly connected client.
     */
    @Override
    protected void clientConnected(ConnectionToClient client) {
        // Alec: keep parent hook; then broadcast members 
        super.clientConnected(client);
        sendToAllClients(new MessageMembers(members));
        setChanged();
        // End Alec
    }

    /**
     * @author : Alec Waumans (2025) & others authors
     * 
     * Alec : Differs from ChatServer (which is empty):
     * - Removes user from members and controllers on disconnect.
     * - Notifies observers and removes the connection.
     *
     * @param client the disconnected client.
     */
    @Override
    protected synchronized void clientDisconnected(ConnectionToClient client) {
        // Alec: cleanup state on disconnect (members + controllers), then notify/broadcast (ChatServer left empty)
        Object idObj = client.getInfo(ID_MAPINFO);
        if (idObj instanceof Integer memberId) {
            members.remove(memberId);
            controllers.remove(memberId);
        } else {
            System.err.println("Could not retrieve disconnected client ID.");
        }

        sendToAllClients(new MessageMembers(members));
        setChanged();
        notifyObservers();
        removeConnection(client);
        // End Alec
    }

    /**
     * 
     * Handles exceptions thrown by a connected client.
     */
    @Override
    protected synchronized void clientException(ConnectionToClient client, Throwable exception) {
        super.clientException(client, exception);
        try {
            if (client.isConnected()) {
                client.sendToClient(new IllegalArgumentException("Unreadable message: " + exception.getMessage()));
            }
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, "Unable to send error to client", ex);
        }
    }

    /**
     * Sends a message to a specific client.
     */
    void sendToClient(Message message, User recipient) {
        sendToClient(message, recipient.getId());
    }

    /**
     * @author : Alec Waumans (2025) & others authors
     * Full implementation (ChatServer’s version is empty):
     * routes a message to a specific connected client by stored ID.
     */
    void sendToClient(Message message, int clientId) {
        // Alec: iterate known connections, match by ID_MAPINFO, then send
        for (Thread t : getClientConnections()) {
            ConnectionToClient conn = (ConnectionToClient) t;
            Object id = conn.getInfo(ID_MAPINFO);
            if (id instanceof Integer && ((Integer) id) == clientId) {
                try {
                    conn.sendToClient(message);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            }
        }
        // End Alec
    }

    /**
     * @author : Alec Waumans (2025)
     * Alec : this function is totally add from me. 
     * Sends the current game state to a specific player.
     */
    private void sendGameInfo(User player, Controller controller) {
        State state = controller.getState(); // Internal game model
        GameInfo info = StateMapper.toGameInfo(state); // Convert to DTO
        MessageGameInfo msg = new MessageGameInfo(User.ADMIN, player, info);
        sendToClient(msg, player.getId());
    }
    private void sendResetCount(User player, int count){
        //pas le temps mais j'aurais rajouter un object Reset. 
        MessageReset msg = new MessageReset(player);
        sendToClient(msg, player.getId());
    }
}
