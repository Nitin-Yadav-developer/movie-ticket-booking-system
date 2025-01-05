import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Date;
import java.sql.Time;
import java.sql.SQLException;
import java.util.List;
import java.util.Calendar;

import com.showtime.dao.ShowtimeDao;
import com.showtime.model.Showtime;

public class ShowtimeDaoTest {
    private ShowtimeDao showtimeDao;
    private Showtime testShowtime;

    @BeforeEach  // Changed from @BeforeAll to @BeforeEach
    public void setUp() {
        showtimeDao = new ShowtimeDao();
        // Create test showtime data
        testShowtime = new Showtime();
        testShowtime.setMovie_id(1);
        testShowtime.setTheatre_id(1);
        testShowtime.setShow_date(new Date(Calendar.getInstance().getTimeInMillis()));
        testShowtime.setShow_time(new Time(Calendar.getInstance().getTimeInMillis()));
        testShowtime.setPrice(10.50);
        testShowtime.setAvailable_seats(100);
        testShowtime.setStatus("ACTIVE");
    }

    @Test
    public void testAddShowtime() {
        try {
            boolean result = showtimeDao.addShowtime(testShowtime);
            assertTrue(result, "Showtime should be added successfully");
        } catch (SQLException e) {
            fail("Exception occurred: " + e.getMessage());
        }
    }

    @Test
    public void testGetShowtime() {
        try {
            // First add a showtime
            showtimeDao.addShowtime(testShowtime);
            
            // Then retrieve it (assuming showtime_id = 1 for test)
            Showtime retrievedShowtime = showtimeDao.getShowtime(1);
            
            assertNotNull(retrievedShowtime, "Retrieved showtime should not be null");
            assertEquals(testShowtime.getMovie_id(), retrievedShowtime.getMovie_id());
            assertEquals(testShowtime.getTheatre_id(), retrievedShowtime.getTheatre_id());
        } catch (SQLException e) {
            fail("Exception occurred: " + e.getMessage());
        }
    }

    @Test
    public void testGetAllShowtimes() {
        try {
            List<Showtime> showtimes = showtimeDao.getAllShowtimes();
            assertNotNull(showtimes, "Showtimes list should not be null");
        } catch (SQLException e) {
            fail("Exception occurred: " + e.getMessage());
        }
    }

    @Test
    public void testGetShowtimesByMovie() {
        try {
            List<Showtime> showtimes = showtimeDao.getShowtimesByMovie(1);
            assertNotNull(showtimes, "Showtimes list should not be null");
        } catch (SQLException e) {
            fail("Exception occurred: " + e.getMessage());
        }
    }

    @Test
    public void testUpdateShowtime() {
        try {
            // First add a showtime
            showtimeDao.addShowtime(testShowtime);
            
            // Modify some values
            testShowtime.setShowtime_id(1); // Assuming ID = 1 for test
            testShowtime.setPrice(12.50);
            
            boolean result = showtimeDao.updateShowtime(testShowtime);
            assertTrue(result, "Showtime should be updated successfully");
        } catch (SQLException e) {
            fail("Exception occurred: " + e.getMessage());
        }
    }

    @Test
    public void testDeleteShowtime() {
        try {
            // First add a showtime
            showtimeDao.addShowtime(testShowtime);
            
            // Then delete it (assuming showtime_id = 1 for test)
            boolean result = showtimeDao.deleteShowtime(1);
            assertTrue(result, "Showtime should be deleted successfully");
        } catch (SQLException e) {
            fail("Exception occurred: " + e.getMessage());
        }
    }

    @AfterEach  // Changed from @After to @AfterEach
    public void tearDown() {
        // Clean up test data if necessary
        try {
            showtimeDao.deleteShowtime(1);
        } catch (SQLException e) {
            // Ignore cleanup errors
        }
    }
}
