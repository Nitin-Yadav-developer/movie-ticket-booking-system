package com.showtime.dao;

import java.sql.Date;
import java.sql.Timestamp;
import java.sql.Time;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.showtime.model.Showtime;
import com.util.DatabaseConnection;

public class ShowtimeDao {
    
    public ShowtimeDao() {
        // No need for connection in constructor
    }

    private Connection getConnection() throws SQLException {
        return DatabaseConnection.getConnection();
    }

    private static final String ADD_SHOWTIME = "INSERT INTO showtimes (movie_id, theatre_id, show_date, show_time, price) VALUES (?, ?, ?, ?, ?)";
    private static final String DELETE_SHOWTIME = "DELETE FROM showtimes WHERE showtime_id=?";
    private static final String INSERT_SHOWTIME = 
        "INSERT INTO showtimes (movie_id, theatre_id, show_date, show_time, " +
        "price, available_seats, status) VALUES (?, ?, ?, ?, ?, ?, ?)";
    private static final String SELECT_ALL_SHOWTIMES = 
        "SELECT s.*, m.title as movie_title, t.name as theatre_name, " +
        "t.total_seats " +
        "FROM showtimes s " +
        "JOIN movies m ON s.movie_id = m.movie_id " +
        "JOIN theatres t ON s.theatre_id = t.theatre_id " +
        "WHERE (s.show_date > CURRENT_DATE) OR " +
        "(s.show_date = CURRENT_DATE AND s.show_time > CURRENT_TIME) " +
        "ORDER BY s.show_date ASC, s.show_time ASC";

    public boolean addShowtime(Showtime showtime) throws SQLException {
        try (Connection connection = getConnection();
             PreparedStatement stmt = connection.prepareStatement(INSERT_SHOWTIME)) {
            stmt.setInt(1, showtime.getMovie_id());
            stmt.setInt(2, showtime.getTheatre_id());
            stmt.setDate(3, showtime.getShow_date());
            stmt.setTime(4, showtime.getShow_time());
            stmt.setDouble(5, showtime.getPrice());
            stmt.setInt(6, showtime.getAvailable_seats());
            stmt.setString(7, showtime.getStatus());
            return stmt.executeUpdate() > 0;
        }
    }

    public Showtime getShowtime(int id) throws SQLException {
        String sql = "SELECT s.*, m.title as movie_title, t.name as theatre_name, " +
                    "t.total_seats, " +
                    "(t.total_seats - (SELECT COUNT(*) FROM bookings b WHERE b.showtime_id = s.showtime_id)) as available_seats, " +
                    "s.ticket_price " +
                    "FROM showtimes s " +
                    "JOIN movies m ON s.movie_id = m.movie_id " +
                    "JOIN theatres t ON s.theatre_id = t.theatre_id " +
                    "WHERE s.showtime_id = ?";

        try (Connection connection = getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Showtime(
                    rs.getInt("showtime_id"),
                    rs.getInt("movie_id"),
                    rs.getInt("theatre_id"),
                    rs.getDate("showDate"),
                    rs.getTimestamp("showtime"),
                    rs.getString("movie_title"),
                    rs.getString("theatre_name"),
                    rs.getInt("total_seats"),
                    rs.getInt("available_seats"),
                    rs.getDouble("ticket_price")
                );
            }
        }
        return null;
    }

    public List<Showtime> getAllShowtimes() throws SQLException {
        List<Showtime> showtimes = new ArrayList<>();
        String sql = "SELECT s.*, m.title as movie_title, t.name as theatre_name, t.total_seats " +
                    "FROM showtimes s " +
                    "JOIN movies m ON s.movie_id = m.movie_id " +
                    "JOIN theatres t ON s.theatre_id = t.theatre_id " +
                    "WHERE (s.show_date > CURRENT_DATE) OR " +
                    "(s.show_date = CURRENT_DATE AND s.show_time > CURRENT_TIME) " +
                    "ORDER BY s.show_date ASC, s.show_time ASC";
        
        try (Connection connection = getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
                
            while (rs.next()) {
                Showtime showtime = new Showtime();
                showtime.setShowtime_id(rs.getInt("showtime_id"));
                showtime.setMovie_id(rs.getInt("movie_id"));
                showtime.setTheatre_id(rs.getInt("theatre_id"));
                showtime.setShow_date(rs.getDate("show_date"));
                showtime.setShow_time(rs.getTime("show_time"));
                showtime.setPrice(rs.getDouble("price"));
                showtime.setAvailable_seats(rs.getInt("available_seats"));
                showtime.setStatus(rs.getString("status"));
                showtime.setMovieTitle(rs.getString("movie_title"));
                showtime.setTheatreName(rs.getString("theatre_name"));
                showtime.setTotalSeats(rs.getInt("total_seats"));
                showtimes.add(showtime);
            }
        }
        return showtimes;
    }

    public List<Showtime> getShowtimesByMovie(int movieId) throws SQLException {
        List<Showtime> showtimes = new ArrayList<>();
        String sql = "SELECT s.*, m.title as movie_title, t.name as theatre_name, " +
                    "t.total_seats " +
                    "FROM showtimes s " +
                    "JOIN movies m ON s.movie_id = m.movie_id " +
                    "JOIN theatres t ON s.theatre_id = t.theatre_id " +
                    "WHERE s.movie_id = ? AND " +
                    "(s.show_date > CURRENT_DATE OR " +
                    "(s.show_date = CURRENT_DATE AND s.show_time > CURRENT_TIME)) " +
                    "ORDER BY s.show_date ASC, s.show_time ASC";

        try (Connection connection = getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, movieId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Showtime showtime = new Showtime();
                    showtime.setShowtime_id(rs.getInt("showtime_id"));
                    showtime.setMovie_id(rs.getInt("movie_id"));
                    showtime.setTheatre_id(rs.getInt("theatre_id"));
                    showtime.setShow_date(rs.getDate("show_date"));
                    showtime.setShow_time(rs.getTime("show_time"));
                    showtime.setPrice(rs.getDouble("price"));
                    showtime.setAvailable_seats(rs.getInt("available_seats"));
                    showtime.setStatus(rs.getString("status"));
                    showtime.setMovieTitle(rs.getString("movie_title"));
                    showtime.setTheatreName(rs.getString("theatre_name"));
                    showtime.setTotalSeats(rs.getInt("total_seats"));
                    showtimes.add(showtime);
                }
            }
        }
        return showtimes;
    }

    public boolean updateShowtime(Showtime showtime) throws SQLException {
        String sql = "UPDATE showtimes SET movie_id = ?, theatre_id = ?, showDate = ?, showtime = ? WHERE showtime_id = ?";
        try (Connection connection = getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, showtime.getMovie_id());
            stmt.setInt(2, showtime.getTheatre_id());
            stmt.setDate(3, (Date) showtime.getShow_date());
            stmt.setTime(4, (Time) showtime.getShow_time());
            stmt.setInt(5, showtime.getShowtime_id());
            return stmt.executeUpdate() > 0;
        }
    }

    public boolean deleteShowtime1(int id) throws SQLException {
        String sql = "DELETE FROM showtimes WHERE showtime_id = ?";
        try (Connection connection = getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        }
    }

    public boolean deleteShowtime(int showtimeId) throws SQLException {
        try (Connection connection = getConnection();
             PreparedStatement stmt = connection.prepareStatement(DELETE_SHOWTIME)) {
            stmt.setInt(1, showtimeId);
            return stmt.executeUpdate() > 0;
        }
    }
}






