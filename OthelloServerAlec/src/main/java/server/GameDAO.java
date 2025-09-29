package server;

import java.sql.*;

/**
 * Data Access Object (DAO) for managing game records in the database.
 * 
 * This class provides methods to insert a new game record and retrieve its generated ID.
 * 
 * Author: Alec Waumans (2025)
 */
public class GameDAO {

    /**
     * Inserts a new game into the GAME table.
     *
     * @param userId     The ID of the user who started the game.
     * @param timeStart  The starting time of the game, as a String (e.g., "2025-08-10 14:30:00").
     * @return The auto-generated game ID from the database.
     * @throws SQLException if there is an error executing the SQL statement or retrieving the generated key.
     */
    public static int insertGame(int userId, String timeStart) throws SQLException {
        
        // SQL query for inserting a new game record
        String insert = "INSERT INTO GAME(user_id, timeStart) VALUES (?, ?)";

        // Use try-with-resources to ensure the connection and statement are automatically closed
        try (
            Connection conn = DBManager.getConnection(); // Get a connection from the database manager
            PreparedStatement stmt = conn.prepareStatement(insert, Statement.RETURN_GENERATED_KEYS) // Allow retrieval of generated keys
        ) {
            // Set the query parameters
            stmt.setInt(1, userId);     // First placeholder: user_id
            stmt.setString(2, timeStart); // Second placeholder: timeStart

            // Execute the insert operation
            stmt.executeUpdate();

            // Retrieve the auto-generated game ID from the database
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1); // Return the first generated key (game ID)
            } else {
                throw new SQLException("Failed to retrieve the generated game ID.");
            }
        }
    }
}
