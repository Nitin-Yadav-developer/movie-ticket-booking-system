package com.booking.dao;
import java.sql.*;


import com.booking.model.Booking;


	   
	   
	    import java.util.ArrayList;
	    import java.util.List;

	    public class BookingDao {
	        private Connection connection;

	        public BookingDao(Connection connection) {
	            this.connection = connection;
	        }

	        public boolean addBooking(Booking booking) throws SQLException {
	            String sql = "INSERT INTO bookings (userId, showtimeId, numberofSeats,bookingDate) VALUES (?, ?, ?,?)";
	            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
	                stmt.setInt(1, booking.getUserId());
	                stmt.setInt(2, booking.getShowtimeId());
	                stmt.setInt(3, booking.getNumberOfSeats());
	                stmt.setDate(4, new java.sql.Date(booking.getBookingdate().getTime()));
	                return stmt.executeUpdate() > 0;
	            }
	        }

	        public Booking getBooking(int id) throws SQLException {
	            String sql = "SELECT * FROM bookings WHERE id = ?";
	            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
	                stmt.setInt(1, id);
	                ResultSet rs = stmt.executeQuery();
	                if (rs.next()) {
	                    return new Booking(
	                        rs.getInt("id"),
	                        rs.getInt("userId"),
	                        rs.getInt("showtimeId"),
	                        rs.getInt("numberofSeats"),
	                        rs.getDate("bookingDate")
	                    );
	                }
	            }
	            return null;
	        }

	        public List<Booking> getAllBookings() throws SQLException {
	            String sql = "SELECT * FROM bookings";
	            List<Booking> bookings = new ArrayList<>();
	            try (Statement stmt = connection.createStatement()) {
	                ResultSet rs = stmt.executeQuery(sql);
	                while (rs.next()) {
	                    bookings.add(new Booking(
	                        rs.getInt("id"),
	                        rs.getInt("userId"),
	                        rs.getInt("showtimeId"),
	                        rs.getInt("numberofSeats"),
	                        rs.getDate("bookingDate")
	                    ));
	                }
	            }
	            return bookings;
	        }

	        public boolean updateBooking(Booking booking) throws SQLException {
	            String sql = "UPDATE bookings SET userId = ?, showtimeId = ?, bookingDate = ? WHERE id = ?";
	            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
	                stmt.setInt(1, booking.getUserId());
	                stmt.setInt(2, booking.getShowtimeId());
	                stmt.setInt(3, booking.getNumberOfSeats());
	                stmt.setDate(4, new java.sql.Date(booking.getBookingdate().getTime()));
	                stmt.setInt(5, booking.getUserId());
	                return stmt.executeUpdate() > 0;
	            }
	        }

	        public boolean deleteBooking(int id) throws SQLException {
	            String sql = "DELETE FROM bookings WHERE id = ?";
	            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
	                stmt.setInt(1, id);
	                return stmt.executeUpdate() > 0;
	            }
	        }
	    }




