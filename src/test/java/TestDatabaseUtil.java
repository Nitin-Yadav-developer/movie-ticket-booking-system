// TestDatabaseUtil.java

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class TestDatabaseUtil {
    public static void cleanupTestData(Connection conn) throws SQLException {
        // Delete in correct order to handle foreign key constraints
        String[] cleanupQueries = {
            "DELETE FROM bookings",
            "DELETE FROM showtimes",
            "DELETE FROM movies",
            "DELETE FROM theatres",
            "DELETE FROM users"
        };
        
        
        for (String query : cleanupQueries) {
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.executeUpdate();
            }
        }
    }
}
