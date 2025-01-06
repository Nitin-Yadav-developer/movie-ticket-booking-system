import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import com.theatre.dao.TheatreDao;
import com.theatre.model.Theatre;
import com.util.DatabaseConnection;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class TheatreDaoTest {
    private TheatreDao theatreDao;
    private Theatre testTheatre;
    private Connection connection;

    @BeforeEach
    void setUp() throws SQLException {
        connection = DatabaseConnection.getConnection();
        theatreDao = new TheatreDao(connection);
        testTheatre = new Theatre();
        testTheatre.setName("Test Theatre");
        testTheatre.setLocation("Test Location");
        testTheatre.setTotal_seats(100);
        testTheatre.setStatus("ACTIVE");
    }

    @Test
    void testAddTheatre() {
        assertDoesNotThrow(() -> {
            boolean result = theatreDao.addTheatre(testTheatre);
            assertTrue(result, "Theatre addition should succeed");
            
            Theatre retrievedTheatre = theatreDao.getTheatreById(testTheatre.getTheatre_id());
            assertNotNull(retrievedTheatre, "Retrieved theatre should not be null");
            assertAll("Theatre properties",
                () -> assertEquals(testTheatre.getName(), retrievedTheatre.getName()),
                () -> assertEquals(testTheatre.getLocation(), retrievedTheatre.getLocation()),
                () -> assertEquals(testTheatre.getTotal_seats(), retrievedTheatre.getTotal_seats())
            );
        });
    }

    @Test
    void testGetAllTheatres() {
        assertDoesNotThrow(() -> {
            theatreDao.addTheatre(testTheatre);
            List<Theatre> theatres = theatreDao.getAllTheatres();
            assertNotNull(theatres, "Theatres list should not be null");
            assertTrue(theatres.size() > 0, "Theatres list should not be empty");
        });
    }

    @Test
    void testUpdateTheatre() {
        assertDoesNotThrow(() -> {
            theatreDao.addTheatre(testTheatre);
            Theatre retrievedTheatre = theatreDao.getTheatreById(testTheatre.getTheatre_id());
            assertNotNull(retrievedTheatre, "Theatre should be retrieved before update");
            
            retrievedTheatre.setName("Updated Theatre");
            retrievedTheatre.setLocation("Updated Location");
            retrievedTheatre.setTotal_seats(200);
            
            boolean updated = theatreDao.updateTheatre(retrievedTheatre);
            assertTrue(updated, "Update operation should succeed");
            
            Theatre updatedTheatre = theatreDao.getTheatreById(retrievedTheatre.getTheatre_id());
            assertAll("Updated theatre properties",
                () -> assertNotNull(updatedTheatre, "Updated theatre should exist"),
                () -> assertEquals("Updated Theatre", updatedTheatre.getName()),
                () -> assertEquals("Updated Location", updatedTheatre.getLocation()),
                () -> assertEquals(200, updatedTheatre.getTotal_seats())
            );
        });
    }

    @Test
    void testDeleteTheatre() {
        assertDoesNotThrow(() -> {
            theatreDao.addTheatre(testTheatre);
            Theatre retrievedTheatre = theatreDao.getTheatreById(testTheatre.getTheatre_id());
            assertNotNull(retrievedTheatre, "Theatre should exist before deletion");
            
            boolean deleted = theatreDao.deleteTheatre(retrievedTheatre.getTheatre_id());
            assertTrue(deleted, "Delete operation should succeed");
            
            Theatre deletedTheatre = theatreDao.getTheatreById(retrievedTheatre.getTheatre_id());
            assertNull(deletedTheatre, "Theatre should not exist after deletion");
        });
    }

    @Test
    void testGetTheatreById() {
        assertDoesNotThrow(() -> {
            theatreDao.addTheatre(testTheatre);
            Theatre theatre = theatreDao.getTheatreById(testTheatre.getTheatre_id());
            assertAll("Theatre retrieval",
                () -> assertNotNull(theatre, "Theatre should be found"),
                () -> assertEquals(testTheatre.getName(), theatre.getName()),
                () -> assertEquals(testTheatre.getLocation(), theatre.getLocation()),
                () -> assertEquals(testTheatre.getTotal_seats(), theatre.getTotal_seats())
            );
        });
    }

    @AfterEach
    void cleanup() {
        try {
            List<Theatre> theatres = theatreDao.getAllTheatres();
            for (Theatre theatre : theatres) {
                if ("Test Theatre".equals(theatre.getName())) {
                    theatreDao.deleteTheatre(theatre.getTheatre_id());
                }
            }
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (Exception e) {
            System.err.println("Error during cleanup: " + e.getMessage());
        }
    }
}
