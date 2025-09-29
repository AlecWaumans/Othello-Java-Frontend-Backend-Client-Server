package server;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Data Access Object (DAO) for managing {@code USER} table interactions.
 * 
 * This class is responsible for retrieving an existing user by email or creating
 * a new one if it does not exist.
 * 
 * Author: Alec Waumans (2025)
 */
public class UserDAO {

    /**
     * Finds a user by their email address or creates a new entry if none exists.
     *
     * @param email The user's email address.
     * @return The ID of the existing or newly created user.
     * @throws SQLException If an error occurs while accessing the database.
     */
    public static int findOrCreate(String email) throws SQLException {

        // Step 1: Attempt to find the user in the database
        String selectSQL = "SELECT id FROM USER WHERE email = ?";
        try (PreparedStatement stmt = DBManager.getConnection().prepareStatement(selectSQL)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("id"); // User found → return their ID
            }
        }

        // Step 2: If the user does not exist, generate a new ID
        int newId = DBManager.getNextId("user");

        // Step 3: Insert the new user record into the database
        String insertSQL = "INSERT INTO USER(id, email) VALUES(?, ?)";
        try (PreparedStatement insertStmt = DBManager.getConnection().prepareStatement(insertSQL)) {
            insertStmt.setInt(1, newId);
            insertStmt.setString(2, email);
            insertStmt.executeUpdate();
        }

        // Step 4: Return the newly generated ID
        return newId;
    }
}
