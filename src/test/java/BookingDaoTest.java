import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import com.booking.dao.BookingDao;
import com.booking.model.Booking;
import com.util.DatabaseConnection;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class BookingDaoTest {
    private BookingDao bookingDao;
    private Booking testBooking;
    private Connection connection;
    private int testUserId;
    private int testShowtimeId;
    private int testTheatreId;

    @BeforeEach
    void setUp() throws SQLException {
        connection = DatabaseConnection.getConnection();
        bookingDao = new BookingDao(connection);
        
        // First create test user
        testUserId = createTestUser();
        // Create test theatre
        testTheatreId = createTestTheatre();
        // Create test showtime
        testShowtimeId = createTestShowtime(testTheatreId);
        
        testBooking = new Booking();
        testBooking.setUserId(testUserId);
        testBooking.setShowtimeId(testShowtimeId);
        testBooking.setTheatreId(testTheatreId);
        testBooking.setSeatsBooked("[\"A1\",\"A2\"]");
        testBooking.setTotalSeats(2);
        testBooking.setTotalAmount(30.00);
        testBooking.setBooking_status("CONFIRMED");
    }

    private int createTestUser() throws SQLException {
        String sql = "INSERT INTO users (username, name, email, password, role) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, "testuser");
            stmt.setString(2, "Test User");
            stmt.setString(3, "test@example.com");
            stmt.setString(4, "password123");
            stmt.setString(5, "USER");
            stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1);
            }
            throw new SQLException("Creating test user failed, no ID obtained.");
        }
    }

    private int createTestTheatre() throws SQLException {
        String sql = "INSERT INTO theatres (name, location, total_seats, status) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, "Test Theatre");
            stmt.setString(2, "Test Location");
            stmt.setInt(3, 100);
            stmt.setString(4, "ACTIVE");
            stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1);
            }
            throw new SQLException("Creating test theatre failed, no ID obtained.");
        }
    }

    private int createTestShowtime(int theatreId) throws SQLException {
        String sql = "INSERT INTO showtimes (movie_id, theatre_id, show_date, show_time, price, available_seats, status) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, createTestMovie()); // Create and get movie ID
            stmt.setInt(2, theatreId);
            stmt.setDate(3, new java.sql.Date(System.currentTimeMillis()));
            stmt.setTime(4, new java.sql.Time(System.currentTimeMillis()));
            stmt.setDouble(5, 10.0);
            stmt.setInt(6, 100);
            stmt.setString(7, "ACTIVE");
            stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1);
            }
            throw new SQLException("Creating test showtime failed, no ID obtained.");
        }
    }

    private int createTestMovie() throws SQLException {
        String sql = "INSERT INTO movies (title, description, genre, duration, status) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, "Test Movie");
            stmt.setString(2, "Test Description");
            stmt.setString(3, "Action");
            stmt.setString(4, "120");
            stmt.setString(5, "ACTIVE");
            stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1);
            }
            throw new SQLException("Creating test movie failed, no ID obtained.");
        }
    }

    @Test
    void testAddBooking() {
        assertDoesNotThrow(() -> {
            boolean result = bookingDao.addBooking(testBooking);
            assertTrue(result, "Booking addition should succeed");
            assertTrue(testBooking.getBookingId() > 0, "Booking ID should be set after insertion");
            
            Booking retrievedBooking = bookingDao.getBooking(testBooking.getBookingId());
            assertNotNull(retrievedBooking, "Retrieved booking should not be null");
            assertAll("Booking properties",
                () -> assertEquals(testBooking.getUserId(), retrievedBooking.getUserId()),
                () -> assertEquals(testBooking.getShowtimeId(), retrievedBooking.getShowtimeId()),
                () -> assertEquals(testBooking.getTotalAmount(), retrievedBooking.getTotalAmount()),
                () -> assertEquals(
                    normalizeJsonString(testBooking.getSeatsBooked()), 
                    normalizeJsonString(retrievedBooking.getSeatsBooked()),
                    "Seats booked should match after normalization"
                )
            );
        });
    }

    @Test
    void testGetBookingsByUserId() {
        assertDoesNotThrow(() -> {
            bookingDao.addBooking(testBooking);
            List<Booking> userBookings = bookingDao.getBookingsByUserId(testBooking.getUserId());
            assertAll("User bookings",
                () -> assertNotNull(userBookings, "Bookings list should not be null"),
                () -> assertFalse(userBookings.isEmpty(), "Bookings list should not be empty"),
                () -> assertTrue(userBookings.stream()
                    .anyMatch(b -> b.getUserId() == testBooking.getUserId()),
                    "Should contain test booking")
            );
        });
    }

    @Test
    void testGetBookingById() {
        assertDoesNotThrow(() -> {
            bookingDao.addBooking(testBooking);
            Booking booking = bookingDao.getBookingById(testBooking.getBookingId());
            assertAll("Booking retrieval",
                () -> assertNotNull(booking, "Booking should be found"),
                () -> assertEquals(testBooking.getUserId(), booking.getUserId()),
                () -> assertEquals(testBooking.getTotalAmount(), booking.getTotalAmount()),
                () -> assertEquals(testBooking.getBooking_status(), booking.getBooking_status())
            );
        });
    }

    @Test
    void testUpdateBooking() {
        assertDoesNotThrow(() -> {
            bookingDao.addBooking(testBooking);
            
            // Update booking details
            testBooking.setSeatsBooked("[\"B1\",\"B2\"]");
            boolean updated = bookingDao.updateBooking(testBooking);
            assertTrue(updated, "Update operation should succeed");
            
            Booking updatedBooking = bookingDao.getBookingById(testBooking.getBookingId());
            assertAll("Updated booking properties",
                () -> assertNotNull(updatedBooking, "Updated booking should exist"),
                () -> assertEquals(
                    normalizeJsonString(testBooking.getSeatsBooked()), 
                    normalizeJsonString(updatedBooking.getSeatsBooked()),
                    "Seats booked should match after normalization"
                )
            );
        });
    }

    // Add this helper method to normalize JSON strings
    private String normalizeJsonString(String json) {
        return json.replaceAll("\\s+", "");
    }

    @Test
    void testGetTotalBookings() {
        assertDoesNotThrow(() -> {
            int initialCount = bookingDao.getTotalBookings();
            bookingDao.addBooking(testBooking);
            int newCount = bookingDao.getTotalBookings();
            assertEquals(initialCount + 1, newCount, "Total bookings should increase by 1");
        });
    }

    @Test
    void testGetTotalRevenue() {
        assertDoesNotThrow(() -> {
            double initialRevenue = bookingDao.getTotalRevenue();
            bookingDao.addBooking(testBooking);
            double newRevenue = bookingDao.getTotalRevenue();
            assertEquals(initialRevenue + testBooking.getTotalAmount(), newRevenue, 0.01, 
                "Total revenue should increase by booking amount");
        });
    }

    @AfterEach
    void cleanup() {
        try {
            // Clean up in reverse order of dependencies
            if (testBooking.getBookingId() > 0) {
                cleanupTestBooking(testBooking.getBookingId());
            }
            cleanupTestShowtime(testShowtimeId);
            cleanupTestTheatre(testTheatreId);
            cleanupTestUser(testUserId);
            
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (Exception e) {
            System.err.println("Error during cleanup: " + e.getMessage());
        }
    }

    private void cleanupTestBooking(int bookingId) throws SQLException {
        try (var stmt = connection.prepareStatement("DELETE FROM bookings WHERE booking_id = ?")) {
            stmt.setInt(1, bookingId);
            stmt.executeUpdate();
        }
    }

    private void cleanupTestShowtime(int showtimeId) throws SQLException {
        try (var stmt = connection.prepareStatement("DELETE FROM showtimes WHERE showtime_id = ?")) {
            stmt.setInt(1, showtimeId);
            stmt.executeUpdate();
        }
    }

    private void cleanupTestTheatre(int theatreId) throws SQLException {
        try (var stmt = connection.prepareStatement("DELETE FROM theatres WHERE theatre_id = ?")) {
            stmt.setInt(1, theatreId);
            stmt.executeUpdate();
        }
    }

    private void cleanupTestUser(int userId) throws SQLException {
        try (var stmt = connection.prepareStatement("DELETE FROM users WHERE user_id = ?")) {
            stmt.setInt(1, userId);
            stmt.executeUpdate();
        }
    }
}
