import static org.junit.jupiter.api.Assertions.*;


import org.junit.jupiter.api.*;
import java.sql.Connection;
import java.sql.SQLException;
import com.booking.model.Booking;
import com.booking.dao.BookingDao;
import com.util.DatabaseConnection;


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class BookingDaoTest {

	private Connection connection;
	private BookingDao bookingDao;

	@BeforeAll
	void setUp() throws SQLException {
		connection = DatabaseConnection.getConnection();
		bookingDao = new BookingDao(connection);
	}

	@Test
	void testAddBooking() throws SQLException {
		Booking booking = new Booking();
		// ...set necessary fields...
		booking.setUserId(1);
		booking.setShowtimeId(6);
		booking.setTheatreId(1);
		booking.setSeatsBooked("[\"A1\",\"A2\"]");
		booking.setTotalSeats(2);
		booking.setTotalAmount(30.0);
		booking.setBooking_status("CONFIRMED");
		boolean result = bookingDao.addBooking(booking);
		assertTrue(result, "Booking should be added successfully");
		assertTrue(booking.getBookingId() > 0, "Booking ID should be generated");
	}

	@Test
	void testGetBooking() throws SQLException {
		Booking booking = bookingDao.getBooking(1);
		assertNotNull(booking, "Booking with ID 1 should exist");
	}

	@Test
	void testUpdateBooking() throws SQLException {
		Booking booking = bookingDao.getBookingById(1);
		assertNotNull(booking, "Booking to update should exist");
		booking.setSeatsBooked("[\"B1\",\"B2\"]");
		boolean updated = bookingDao.updateBooking(booking);
		assertTrue(updated, "Booking should be updated");
	}

	@Test
	void testDeleteBooking() throws SQLException {
		// Assume a booking with ID 1 or a newly added booking
		boolean result = bookingDao.deleteBooking(1);
		assertTrue(result, "Booking should be deleted");
	}

	@AfterAll
	void tearDown() throws SQLException {
		if (connection != null && !connection.isClosed()) {
			connection.close();
		}
	}
}
