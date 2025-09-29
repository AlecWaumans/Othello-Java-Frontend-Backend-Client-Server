package server;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author g58399
 */
public class ResetDAO {
    /**
     * Inserts a new game into the GAME table.
     *
     * @param Id     The ID of the sequence,
     * @param player the id of the user,
     * @param timeStart  The starting time of the game, as a String (e.g., "2025-08-10 14:30:00").
     * @return The auto-generated reset ID from the database.
     * @throws SQLException if there is an error executing the SQL statement or retrieving the generated key.
     */
    public static int insertReset(int Id,int player, String timeStart) throws SQLException {
        
        // SQL query for inserting a new game record
        String insert = "INSERT INTO RESET(Id, player, timeStart) VALUES (?, ?, ?)";

        // Use try-with-resources to ensure the connection and statement are automatically closed
        try (
            Connection conn = DBManager.getConnection(); // Get a connection from the database manager
            PreparedStatement stmt = conn.prepareStatement(insert, Statement.RETURN_GENERATED_KEYS) // Allow retrieval of generated keys
        ) {
            // Set the query parameters
            stmt.setInt(1, Id);     // First placeholder: user_id
            stmt.setInt(2, player);
            stmt.setString(3, timeStart); // Second placeholder: timeStart

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
    /**
     * Alec 58399
     * Finds a user by their email address or creates a new entry if none exists.
     *
     * @param Id du joueur/auther
     * @return Number of the reset already done
     * @throws SQLException If an error occurs while accessing the database.
     */
    public static int findNumbReset(int IdAuthor) throws SQLException {

        // Trouver Le nombre de de reset en comptant combien de reset il y a à son nom nom. 
        String selectSQL = "SELECT id FROM RESET WHERE player = ?";
        int count = 0;
        try (PreparedStatement stmt = DBManager.getConnection().prepareStatement(selectSQL)) {
            stmt.setInt(1, IdAuthor);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                count++;
            }
        }

        return count;
    }
}
