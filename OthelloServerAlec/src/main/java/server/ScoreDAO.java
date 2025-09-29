package server;

import java.sql.*;

/**
 * Data Access Object (DAO) for managing score records in the database.
 * 
 * This class provides a method to insert a score record associated with a specific game.
 * 
 * Author: Alec Waumans (2025)
 */
public class ScoreDAO {

    /**
     * Inserts a score record into the SCORE table.
     *
     * @param gameId     The ID of the game to which this score belongs.
     * @param num        The turn number or sequence number of the score record.
     * @param scoreJ     The score of the human player.
     * @param scoreIA    The score of the AI player.
     * @param gameState  The current state of the game (e.g., "RUNNING", "ENDGAME", "SURRENDER").
     * @param timeS      The timestamp of when the score was recorded.
     * @throws SQLException if there is an error executing the SQL statement.
     */
    public static void insertScore(int gameId, int num, int scoreJ, int scoreIA, String gameState, String timeS) throws SQLException {
        
        // SQL query for inserting a new score record
        String insert = "INSERT INTO SCORE(game_id, num, scoreJ, scoreIA, gameState, timeS) VALUES (?, ?, ?, ?, ?, ?)";

        // Use try-with-resources to ensure database resources are closed automatically
        try (
            Connection conn = DBManager.getConnection();              // Get a connection from the database manager
            PreparedStatement stmt = conn.prepareStatement(insert)    // Prepare the SQL statement
        ) {
            // Set query parameters
            stmt.setInt(1, gameId);     // Game ID foreign key
            stmt.setInt(2, num);        // Turn or sequence number
            stmt.setInt(3, scoreJ);     // Human player score
            stmt.setInt(4, scoreIA);    // AI player score
            stmt.setString(5, gameState); // Current state of the game
            stmt.setString(6, timeS);     // Timestamp of score entry

            // Execute the insert operation
            stmt.executeUpdate();
        }
    }
}
