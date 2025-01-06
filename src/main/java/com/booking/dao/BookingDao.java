package com.booking.dao;
import java.sql.*;

import java.util.ArrayList;
import java.util.List;
import com.booking.model.Booking;
import com.util.DatabaseConnection;

public class BookingDao {
    private Connection connection;

    public BookingDao(Connection connection) {
        this.connection = connection;
    }

    // Add method to get connection if not exists
    private Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connection = DatabaseConnection.getConnection();
        }
        return connection;
    }

    private static final String GET_TOTAL_BOOKINGS = "SELECT COUNT(*) FROM bookings";
    private static final String GET_TOTAL_REVENUE = "SELECT SUM(total_amount) FROM bookings";
    private static final String INSERT_BOOKING = 
        "INSERT INTO bookings (user_id, showtime_id, theatre_id, seats_booked, total_seats, " +
        "total_amount, booking_status) VALUES (?, ?, ?, CAST(? AS JSON), ?, ?, ?)";

    public boolean addBooking(Booking booking) throws SQLException {
        String sql = "INSERT INTO bookings (user_id, showtime_id, theatre_id, seats_booked, total_seats, " +
                    "total_amount, booking_status) VALUES (?, ?, ?, CAST(? AS JSON), ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, booking.getUserId());
            stmt.setInt(2, booking.getShowtimeId());
            stmt.setInt(3, booking.getTheatreId());
            stmt.setString(4, booking.getSeatsBooked()); // JSON string
            stmt.setInt(5, booking.getTotalSeats());
            stmt.setDouble(6, booking.getTotalAmount());
            stmt.setString(7, booking.getBooking_status());
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
        String sql = "SELECT b.*, m.title, t.name as theatre_name, t.location as theatre_location, " + // Added t.location
                    "b.total_amount, b.booking_date, b.seats_booked " +
                    "FROM bookings b " +
                    "JOIN showtimes s ON b.showtime_id = s.showtime_id " +
                    "JOIN movies m ON s.movie_id = m.movie_id " +
                    "JOIN theatres t ON b.theatre_id = t.theatre_id " +
                    "WHERE b.booking_id = ?";
                    
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, bookingId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Booking booking = new Booking();
                booking.setBookingId(rs.getInt("booking_id"));
                booking.setShowtimeId(rs.getInt("showtime_id"));
                booking.setUserId(rs.getInt("user_id"));
                booking.setSeatsBooked(rs.getString("seats_booked"));
                booking.setBookingDate(rs.getTimestamp("booking_date"));
                booking.setTheatreId(rs.getInt("theatre_id"));
                booking.setTotalSeats(rs.getInt("total_seats"));
                booking.setMovieTitle(rs.getString("title"));
                booking.setTheatreName(rs.getString("theatre_name"));
                booking.setTheatreLocation(rs.getString("theatre_location")); // Add this line
                booking.setTotalAmount(rs.getDouble("total_amount"));
                booking.setBooking_status(rs.getString("booking_status"));
                return booking;
            }
        }
        return null;
    }

    public List<Booking> getBookingsByUserId(int userId) throws SQLException {
        String sql = "SELECT b.*, m.title as movie_title, t.name as theatre_name, s.show_date, s.show_time, " +
                    "b.total_amount, b.booking_status " +
                    "FROM bookings b " +
                    "JOIN showtimes s ON b.showtime_id = s.showtime_id " +
                    "JOIN movies m ON s.movie_id = m.movie_id " +
                    "JOIN theatres t ON b.theatre_id = t.theatre_id " +
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
                booking.setTheatreName(rs.getString("theatre_name"));
                booking.setTotalAmount(rs.getDouble("total_amount"));
                booking.setBooking_status(rs.getString("booking_status"));
                
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
        booking.setMovieTitle(rs.getString("movie_title")); // Changed from 'title' to 'movie_title'
        booking.setTheatreName(rs.getString("theatre_name")); // Changed from 'name' to 'theatre_name'
        booking.setTotalAmount(rs.getDouble("total_amount"));
        booking.setBooking_status(rs.getString("booking_status"));
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

  

    public int getTotalBookings() throws SQLException {
        String sql = "SELECT COUNT(*) FROM bookings";
        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            return rs.next() ? rs.getInt(1) : 0;
        }
    }

    public double getTotalRevenue() throws SQLException {
        String sql = "SELECT COALESCE(SUM(total_amount), 0) FROM bookings";
        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            return rs.next() ? rs.getDouble(1) : 0.0;
        }
    }

    public Booking getBookingById(int bookingId) throws SQLException {
        String sql = "SELECT b.*, m.title as movie_title, t.name as theatre_name " +
                    "FROM bookings b " +
                    "LEFT JOIN showtimes s ON b.showtime_id = s.showtime_id " +
                    "LEFT JOIN movies m ON s.movie_id = m.movie_id " +
                    "LEFT JOIN theatres t ON b.theatre_id = t.theatre_id " +
                    "WHERE b.booking_id = ?";
        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setInt(1, bookingId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return extractBookingFromResultSet(rs);
            }
        }
        return null;
    }

    public List<Booking> getAllBookings() throws SQLException {
        List<Booking> bookings = new ArrayList<>();
        String sql = "SELECT b.*, m.title as movie_title, t.name as theatre_name " +
                    "FROM bookings b " +
                    "LEFT JOIN showtimes s ON b.showtime_id = s.showtime_id " +
                    "LEFT JOIN movies m ON s.movie_id = m.movie_id " +
                    "LEFT JOIN theatres t ON b.theatre_id = t.theatre_id " +
                    "ORDER BY b.booking_date DESC";
        
        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            System.out.println("Executing query: " + sql); // Debug log
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                bookings.add(extractBookingFromResultSet(rs));
            }
        }
        return bookings;
    }

    public int getLastGeneratedBookingId() throws SQLException {
        String sql = "SELECT LAST_INSERT_ID()";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
            throw new SQLException("Could not get generated booking ID");
        }
    }
}




