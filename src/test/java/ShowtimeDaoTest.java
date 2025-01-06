import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import com.showtime.dao.ShowtimeDao;
import com.showtime.model.Showtime;
import java.sql.Date;
import java.sql.Time;
import java.sql.SQLException;
import java.util.List;
import java.time.LocalDate;
import java.time.LocalTime;

public class ShowtimeDaoTest {
    private ShowtimeDao showtimeDao;
    private Showtime testShowtime;

    @BeforeEach
    void setUp() {
        showtimeDao = new ShowtimeDao();
        testShowtime = new Showtime();
        // Set dummy movie and theatre IDs (make sure these exist in your test database)
        testShowtime.setMovie_id(1);
        testShowtime.setTheatre_id(1);
        testShowtime.setShow_date(Date.valueOf(LocalDate.now().plusDays(1)));
        testShowtime.setShow_time(Time.valueOf(LocalTime.of(14, 30))); // 2:30 PM
        testShowtime.setPrice(15.99);
        testShowtime.setAvailable_seats(100);
        testShowtime.setStatus("ACTIVE");
    }

    @Test
    void testAddShowtime() {
        assertDoesNotThrow(() -> {
            // Add the showtime
            boolean result = showtimeDao.addShowtime(testShowtime);
            assertTrue(result, "Showtime addition should succeed");
            assertTrue(testShowtime.getShowtime_id() > 0, "Showtime ID should be set after insertion");
            
            // Now we can get the showtime using the ID that was set
            Showtime retrievedShowtime = showtimeDao.getShowtime(testShowtime.getShowtime_id());
            assertNotNull(retrievedShowtime, "Retrieved showtime should not be null");
            
            // Verify all properties
            assertAll("Showtime properties",
                () -> assertEquals(testShowtime.getMovie_id(), retrievedShowtime.getMovie_id()),
                () -> assertEquals(testShowtime.getTheatre_id(), retrievedShowtime.getTheatre_id()),
                () -> assertEquals(testShowtime.getShow_date().toString(), 
                                 retrievedShowtime.getShow_date().toString()),
                () -> assertEquals(testShowtime.getShow_time().toString(), 
                                 retrievedShowtime.getShow_time().toString()),
                () -> assertEquals(testShowtime.getPrice(), 
                                 retrievedShowtime.getPrice(), 0.01)
            );
        });
    }

    @Test
    void testGetAllShowtimes() {
        assertDoesNotThrow(() -> {
            showtimeDao.addShowtime(testShowtime);
            List<Showtime> showtimes = showtimeDao.getAllShowtimes();
            assertNotNull(showtimes, "Showtimes list should not be null");
            assertFalse(showtimes.isEmpty(), "Showtimes list should not be empty");
        });
    }

    @Test
    void testUpdateShowtime() {
        assertDoesNotThrow(() -> {
            // First add the showtime
            boolean added = showtimeDao.addShowtime(testShowtime);
            assertTrue(added, "Showtime should be added successfully");
            
            // Get all showtimes and find the one we just added
            List<Showtime> showtimes = showtimeDao.getAllShowtimes();
            Showtime retrievedShowtime = showtimes.stream()
                .filter(s -> s.getMovie_id() == testShowtime.getMovie_id() 
                        && s.getTheatre_id() == testShowtime.getTheatre_id()
                        && Math.abs(s.getPrice() - testShowtime.getPrice()) < 0.01)
                .findFirst()
                .orElseThrow(() -> new AssertionError("Added showtime not found"));
            
            assertNotNull(retrievedShowtime, "Showtime should be retrieved before update");
            
            // Update showtime details
            double newPrice = 20.99;
            int newSeats = 80;
            retrievedShowtime.setPrice(newPrice);
            retrievedShowtime.setAvailable_seats(newSeats);
            retrievedShowtime.setStatus("ACTIVE"); // Ensure status is set
            
            // Keep original date/time values
            retrievedShowtime.setShow_date(testShowtime.getShow_date());
            retrievedShowtime.setShow_time(testShowtime.getShow_time());
            
            System.out.println("Updating showtime with ID: " + retrievedShowtime.getShowtime_id());
            boolean updated = showtimeDao.updateShowtime(retrievedShowtime);
            assertTrue(updated, "Update operation should succeed");
            
            // Verify the update
            Showtime updatedShowtime = showtimeDao.getShowtime(retrievedShowtime.getShowtime_id());
            assertAll("Updated showtime properties",
                () -> assertNotNull(updatedShowtime, "Updated showtime should exist"),
                () -> assertEquals(newPrice, updatedShowtime.getPrice(), 0.01),
                () -> assertEquals(newSeats, updatedShowtime.getAvailable_seats())
            );
        });
    }

    @Test
    void testDeleteShowtime() {
        assertDoesNotThrow(() -> {
            // First add the showtime and verify it exists
            boolean added = showtimeDao.addShowtime(testShowtime);
            assertTrue(added, "Showtime should be added successfully");
            
            // Get the list of showtimes and find the one we just added
            List<Showtime> showtimes = showtimeDao.getAllShowtimes();
            Showtime showtimeToDelete = showtimes.stream()
                .filter(s -> s.getMovie_id() == testShowtime.getMovie_id() 
                        && s.getTheatre_id() == testShowtime.getTheatre_id()
                        && s.getPrice() == testShowtime.getPrice())
                .findFirst()
                .orElseThrow(() -> new AssertionError("Added showtime not found"));
            
            // Delete the showtime
            boolean deleted = showtimeDao.deleteShowtime(showtimeToDelete.getShowtime_id());
            assertTrue(deleted, "Delete operation should succeed");
            
            // Verify deletion
            Showtime deletedShowtime = showtimeDao.getShowtime(showtimeToDelete.getShowtime_id());
            assertNull(deletedShowtime, "Showtime should not exist after deletion");
        });
    }

    @Test
    void testGetShowtimesByMovie() {
        assertDoesNotThrow(() -> {
            showtimeDao.addShowtime(testShowtime);
            List<Showtime> movieShowtimes = showtimeDao.getShowtimesByMovie(testShowtime.getMovie_id());
            assertAll("Movie showtimes",
                () -> assertNotNull(movieShowtimes, "Movie showtimes should not be null"),
                () -> assertFalse(movieShowtimes.isEmpty(), "Movie showtimes should not be empty"),
                () -> assertTrue(movieShowtimes.stream()
                    .anyMatch(s -> s.getMovie_id() == testShowtime.getMovie_id()),
                    "Should contain showtime for test movie")
            );
        });
    }

    @AfterEach
    void cleanup() {
        try {
            List<Showtime> showtimes = showtimeDao.getAllShowtimes();
            for (Showtime showtime : showtimes) {
                // Delete test showtimes based on specific criteria
                if (showtime.getPrice() == 15.99 && 
                    showtime.getAvailable_seats() == 100) {
                    showtimeDao.deleteShowtime(showtime.getShowtime_id());
                }
            }
        } catch (Exception e) {
            System.err.println("Error during cleanup: " + e.getMessage());
        }
    }
}
