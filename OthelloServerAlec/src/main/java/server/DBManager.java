package server;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import org.sqlite.SQLiteConfig;

/**
 * Author: Alec Waumans (2025)
 * SQLite database connection manager.
 * Opens a new connection for each request.
 */
public class DBManager {

    // Path to the SQLite database file (relative to the project). 
    private static final String DB_PATH = "src/main/resources/sqliteDB/othello.db";

    // Classpath resource for the bootstrap SQL script. 
    private static final String INIT_SQL_CLASSPATH = "/sqliteDB/othello.sql";

    // Simple JVM-level lock to serialize sequence increments. 
    private static final Object SEQ_LOCK = new Object();

    // Static initializer: ensure DB exists and is initialized once
    static {
        ensureDatabaseInitialized();
    }

    /**
     * Returns a new JDBC connection to the SQLite database.
     * Enables foreign key constraints.
     */
    public static Connection getConnection() throws SQLException {
        SQLiteConfig config = new SQLiteConfig();
        config.enforceForeignKeys(true);
        return DriverManager.getConnection("jdbc:sqlite:" + DB_PATH, config.toProperties());
    }

    /**
     * Retrieves and increments the value of a named sequence in a safe manner.
     * Uses a short transaction + a JVM lock to avoid races under concurrency.
     */
    public static int getNextId(String name) throws SQLException {
        synchronized (SEQ_LOCK) {
            try (Connection conn = getConnection()) {
                conn.setAutoCommit(false);

                int current;
                try (var sel = conn.prepareStatement("SELECT value FROM SEQUENCE WHERE name = ?")) {
                    sel.setString(1, name);
                    try (var rs = sel.executeQuery()) {
                        if (!rs.next()) {
                            conn.rollback();
                            throw new SQLException("Sequence not found: " + name);
                        }
                        current = rs.getInt("value");
                    }
                }

                int next = current + 1;

                try (var upd = conn.prepareStatement("UPDATE SEQUENCE SET value = ? WHERE name = ?")) {
                    upd.setInt(1, next);
                    upd.setString(2, name);
                    upd.executeUpdate();
                }

                conn.commit();
                conn.setAutoCommit(true);
                return next;
            } catch (SQLException e) {
                throw e;
            }
        }
    }



    // Ensure the DB file exists; if not, create it by running othello.sql from resources. 
    private static void ensureDatabaseInitialized() {
        File dbFile = new File(DB_PATH);
        if (dbFile.exists()) {
            return; // already there
        }
        // Make sure parent directory exists (if running from a clean workspace)
        dbFile.getParentFile().mkdirs();

        try (Connection conn = getConnection()) {
            runSqlScriptFromClasspath(conn, INIT_SQL_CLASSPATH);
            System.out.println("[DBManager] Database initialized from " + INIT_SQL_CLASSPATH);
        } catch (Exception e) {
            System.err.println("[DBManager] Failed to initialize database: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Load and execute a SQL script bundled in resources.
    private static void runSqlScriptFromClasspath(Connection conn, String resourcePath) throws Exception {
        try (InputStream in = DBManager.class.getResourceAsStream(resourcePath)) {
            if (in == null) {
                throw new IllegalStateException("Resource not found: " + resourcePath);
            }
            try (BufferedReader br = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8))) {
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null) {
                    sb.append(line).append('\n');
                }
                executeSqlBatch(conn, sb.toString());
            }
        }
    }

    // Execute multiple SQL statements separated by ';'.
    private static void executeSqlBatch(Connection conn, String script) throws SQLException {
        String[] statements = script.split(";(\\s*\\r?\\n|\\s*$)");
        try (Statement st = conn.createStatement()) {
            for (String raw : statements) {
                String sql = raw.trim();
                if (sql.isEmpty() || sql.startsWith("--")) continue;
                st.executeUpdate(sql);
            }
        }
    }
}
