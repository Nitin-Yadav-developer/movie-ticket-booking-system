package com.showtime.dao;

import com.showtime.model.Showtime;

import com.util.DatabaseConnection;

import java.sql.Date;
import java.sql.Timestamp;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ShowtimeDao {
    
    public ShowtimeDao() {
        // No need for connection in constructor
    }

    private Connection getConnection() throws SQLException {
        return DatabaseConnection.getConnection();
    }

    public boolean addShowtime(Showtime showtime) throws SQLException {
        String sql = "INSERT INTO showtimes (movie_id, theatre_id, showDate, showtime, ticket_price) VALUES (?, ?, ?, ?, ?)";
        try (Connection connection = getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, showtime.getMovie_id());
            stmt.setInt(2, showtime.getTheatre_id());
            stmt.setDate(3, (Date) showtime.getShowDate());
            stmt.setDate(4, (Date) showtime.getShowtime());
            stmt.setDouble(5, showtime.getTicketPrice());
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
        String sql = "SELECT s.*, m.title as movie_title, t.name as theatre_name, " +
                    "t.total_seats, " +
                    "(t.total_seats - COALESCE((SELECT COUNT(*) FROM bookings b " +
                    "WHERE b.showtime_id = s.showtime_id), 0)) as available_seats " +
                    "FROM showtimes s " +
                    "JOIN movies m ON s.movie_id = m.movie_id " +
                    "JOIN theatres t ON s.theatre_id = t.theatre_id " +
                    "WHERE (s.showDate > CURRENT_DATE) OR " +
                    "(s.showDate = CURRENT_DATE AND s.showtime > CURRENT_TIME) " +
                    "ORDER BY s.showDate ASC, s.showtime ASC";
        
        try (Connection connection = getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Showtime showtime = new Showtime(
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
                showtimes.add(showtime);
            }
        }
        return showtimes;
    }

    public List<Showtime> getShowtimesByMovie(int movieId) throws SQLException {
        List<Showtime> showtimes = new ArrayList<>();
        String sql = "SELECT s.*, m.title as movie_title, t.name as theatre_name, " +
                    "t.total_seats, " +
                    "COALESCE(t.total_seats - COUNT(b.booking_id), t.total_seats) as available_seats " +
                    "FROM showtimes s " +
                    "JOIN movies m ON s.movie_id = m.movie_id " +
                    "JOIN theatres t ON s.theatre_id = t.theatre_id " +
                    "LEFT JOIN bookings b ON s.showtime_id = b.showtime_id " +
                    "WHERE s.movie_id = ? AND s.showDate >= CURDATE() " +
                    "GROUP BY s.showtime_id, m.title, t.name " +
                    "ORDER BY s.showDate, s.showtime";

        try (Connection connection = getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, movieId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Showtime showtime = new Showtime(
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
                showtimes.add(showtime);
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
            stmt.setDate(3, (Date) showtime.getShowDate());
            stmt.setDate(4, (Date) showtime.getShowtime());
            stmt.setInt(5, showtime.getShowtime_id());
            return stmt.executeUpdate() > 0;
        }
    }

    public boolean deleteShowtime(int id) throws SQLException {
        String sql = "DELETE FROM showtimes WHERE showtime_id = ?";
        try (Connection connection = getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        }
    }
}






