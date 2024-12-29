package com.booking.dao;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import com.booking.model.Booking;

public class BookingDao {
    private Connection connection;

    public BookingDao(Connection connection) {
        this.connection = connection;
    }

    public boolean addBooking(Booking booking) throws SQLException {
        String sql = "INSERT INTO bookings (showtime_id, user_id, seats_booked, theatre_id, total_seats, booking_date) " +
                    "VALUES (?, ?, CAST(? AS JSON), ?, ?, NOW())";
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, booking.getShowtimeId());
            stmt.setObject(2, booking.getUserId());
            stmt.setString(3, booking.getSeatsBooked()); // JSON formatted string
            stmt.setInt(4, booking.getTheatreId());
            stmt.setInt(5, booking.getTotalSeats());
            
            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        booking.setBookingId(generatedKeys.getInt(1));
                        return true;
                    }
                }
            }
            return false;
        }
    }

    public Booking getBooking(int bookingId) throws SQLException {
        String sql = "SELECT b.*, m.title, t.name as theatre_name, s.ticket_price " +
                    "FROM bookings b " +
                    "JOIN showtimes s ON b.showtime_id = s.showtime_id " +
                    "JOIN movies m ON s.movie_id = m.movie_id " +
                    "JOIN theatres t ON b.theatre_id = t.theatre_id " +
                    "WHERE b.booking_id = ?";
                    
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, bookingId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return extractBookingFromResultSet(rs);
            }
        }
        return null;
    }

    public List<Booking> getBookingsByUserId(int userId) throws SQLException {
        String sql = "SELECT b.booking_id, b.showtime_id, b.user_id, b.seats_booked, " +
                    "b.booking_date, b.theatre_id, b.total_seats, m.title as movie_title " +
                    "FROM bookings b " +
                    "JOIN showtimes s ON b.showtime_id = s.showtime_id " +
                    "JOIN movies m ON s.movie_id = m.movie_id " +
                    "WHERE b.user_id = ? " +
                    "ORDER BY b.booking_date DESC";
                    
        List<Booking> bookings = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Booking booking = new Booking();
                booking.setBookingId(rs.getInt("booking_id"));
                booking.setShowtimeId(rs.getInt("showtime_id"));
                booking.setUserId(rs.getInt("user_id"));
                booking.setSeatsBooked(rs.getString("seats_booked"));
                booking.setBookingDate(rs.getTimestamp("booking_date"));
                booking.setTheatreId(rs.getInt("theatre_id"));
                booking.setTotalSeats(rs.getInt("total_seats"));
                booking.setMovieTitle(rs.getString("movie_title"));
                
                // Calculate total amount
                double pricePerSeat = 10.0; // Fixed price per seat
                booking.setTotalAmount(booking.getTotalSeats() * pricePerSeat);
                
                bookings.add(booking);
            }
        }
        return bookings;
    }

    private Booking extractBookingFromResultSet(ResultSet rs) throws SQLException {
        Booking booking = new Booking();
        booking.setBookingId(rs.getInt("booking_id"));
        booking.setShowtimeId(rs.getInt("showtime_id"));
        booking.setUserId(rs.getObject("user_id", Integer.class));
        booking.setSeatsBooked(rs.getString("seats_booked"));
        booking.setBookingDate(rs.getTimestamp("booking_date"));
        booking.setTheatreId(rs.getInt("theatre_id"));
        booking.setTotalSeats(rs.getInt("total_seats"));
        
        // Set movie title
        booking.setMovieTitle(rs.getString("title"));
        
        // Calculate total amount (assuming fixed price per seat)
        double pricePerSeat = 10.0; // You can modify this or get from database
        int seatCount = booking.getTotalSeats();
        booking.setTotalAmount(seatCount * pricePerSeat);
        
        return booking;
    }

    public boolean updateBooking(Booking booking) throws SQLException {
        String sql = "UPDATE bookings SET seats_booked = ? WHERE booking_id = ? AND user_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, booking.getSeatsBooked());
            stmt.setInt(2, booking.getBookingId());
            stmt.setInt(3, booking.getUserId());
            return stmt.executeUpdate() > 0;
        }
    }

    public boolean deleteBooking(int bookingId) throws SQLException {
        String sql = "DELETE FROM bookings WHERE booking_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, bookingId);
            return stmt.executeUpdate() > 0;
        }
    }
}




