import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import com.theatre.dao.TheatreDao;
import com.theatre.model.Theatre;
import com.util.DatabaseConnection;

public class TheatreDaoTest {
    private TheatreDao theatreDao;
    private Connection connection;
    private Theatre testTheatre;

    @BeforeEach
    public void setUp() throws SQLException {
        connection = DatabaseConnection.getConnection();
        theatreDao = new TheatreDao(connection);
        
        // Create test theatre data
        testTheatre = new Theatre();
        testTheatre.setName("Test Theatre");
        testTheatre.setLocation("Test Location");
        testTheatre.setTotal_seats(200);
        testTheatre.setStatus("ACTIVE");
    }

    @Test
    public void testAddTheatre() {
        try {
            boolean result = theatreDao.addTheatre(testTheatre);
            assertTrue(result, "Theatre should be added successfully");
        } catch (SQLException e) {
            fail("Exception occurred: " + e.getMessage());
        }
    }

    @Test
    public void testGetTheatre() {
        try {
            // First add a theatre
            theatreDao.addTheatre(testTheatre);
            
            // Then retrieve it (assuming theatre_id = 1 for test)
            Theatre retrievedTheatre = theatreDao.getTheatre(1);
            
            assertNotNull(retrievedTheatre, "Retrieved theatre should not be null");
            assertEquals(testTheatre.getName(), retrievedTheatre.getName());
            assertEquals(testTheatre.getLocation(), retrievedTheatre.getLocation());
            assertEquals(testTheatre.getTotal_seats(), retrievedTheatre.getTotal_seats());
        } catch (SQLException e) {
            fail("Exception occurred: " + e.getMessage());
        }
    }

    @Test
    public void testGetAllTheatres() {
        try {
            // Add test theatre first
            theatreDao.addTheatre(testTheatre);
            
            List<Theatre> theatres = theatreDao.getAllTheatres();
            assertNotNull(theatres, "Theatres list should not be null");
            assertFalse(theatres.isEmpty(), "Theatres list should not be empty");
        } catch (SQLException e) {
            fail("Exception occurred: " + e.getMessage());
        }
    }

    @Test
    public void testUpdateTheatre() {
        try {
            // First add a theatre
            theatreDao.addTheatre(testTheatre);
            
            // Modify theatre details
            testTheatre.setTheatre_id(1); // Assuming ID = 1 for test
            testTheatre.setName("Updated Theatre");
            testTheatre.setTotal_seats(300);
            
            boolean result = theatreDao.updateTheatre(testTheatre);
            assertTrue(result, "Theatre should be updated successfully");
            
            // Verify update
            Theatre updatedTheatre = theatreDao.getTheatreById(1);
            assertEquals("Updated Theatre", updatedTheatre.getName());
            assertEquals(300, updatedTheatre.getTotal_seats());
        } catch (SQLException e) {
            fail("Exception occurred: " + e.getMessage());
        }
    }

    @Test
    public void testDeleteTheatre() {
        try {
            // First add a theatre
            theatreDao.addTheatre(testTheatre);
            
            // Then delete it (assuming theatre_id = 1 for test)
            boolean result = theatreDao.deleteTheatre(1);
            assertTrue(result, "Theatre should be deleted successfully");
            
            // Verify deletion
            Theatre deletedTheatre = theatreDao.getTheatreById(1);
            assertNull(deletedTheatre, "Theatre should not exist after deletion");
        } catch (SQLException e) {
            fail("Exception occurred: " + e.getMessage());
        }
    }

    @AfterEach
    public void tearDown() {
        try {
            // Clean up test data
            if (connection != null && !connection.isClosed()) {
                theatreDao.deleteTheatre(1);
                connection.close();
            }
        } catch (SQLException e) {
            // Ignore cleanup errors
        }
    }
}
