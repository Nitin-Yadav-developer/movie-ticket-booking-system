package com.theatre.dao;

import java.sql.*;

import java.util.ArrayList;
import java.util.List;
import com.theatre.model.Theatre;
import com.util.DatabaseConnection;

public class TheatreDao {
    private Connection connection;
    private static final String INSERT_THEATRE = 
        "INSERT INTO theatres (name, location, total_seats, status) VALUES (?, ?, ?, ?)";
        
    private static final String SELECT_ALL_THEATRES = 
        "SELECT theatre_id, name, location, total_seats, status, created_at FROM theatres";
        
    private static final String UPDATE_THEATRE = 
        "UPDATE theatres SET name=?, location=?, total_seats=?, status=? WHERE theatre_id=?";

    public TheatreDao(Connection connection) {
        this.connection = connection;
    }

    
    // Add method to check and refresh connection
    private Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connection = DatabaseConnection.getConnection();
        }
        return connection;
    }

    public boolean addTheatre(Theatre theatre) throws SQLException {
        String sql = "INSERT INTO theatres (name, location, total_seats, status) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, theatre.getName());
            stmt.setString(2, theatre.getLocation());
            stmt.setInt(3, theatre.getTotal_seats());
            stmt.setString(4, theatre.getStatus());
            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        theatre.setTheatre_id(generatedKeys.getInt(1));
                        return true;
                    }
                }
            }
            return false;
        }
    }

    public int getLastInsertedId() throws SQLException {
        try (Statement stmt = getConnection().createStatement();
             ResultSet rs = stmt.executeQuery("SELECT LAST_INSERT_ID()")) {
            return rs.next() ? rs.getInt(1) : -1;
        }
    }

    public Theatre getTheatre(int id) throws SQLException {
        String sql = "SELECT * FROM theatres WHERE theatre_id = ?";
        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Theatre(
                    rs.getInt("theatre_id"),
                    rs.getString("name"),
                    rs.getString("location"),
                    rs.getInt("total_seats")  // Changed from "totalseats" to "total_seats"
                );
            }
        }
        return null;
    }

    public List<Theatre> getAllTheatres() throws SQLException {
        List<Theatre> theatres = new ArrayList<>();
        try (Statement stmt = getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(SELECT_ALL_THEATRES)) {
            while (rs.next()) {
                Theatre theatre = new Theatre(
                    rs.getInt("theatre_id"),
                    rs.getString("name"),
                    rs.getString("location"),
                    rs.getInt("total_seats")
                );
                theatre.setStatus(rs.getString("status"));
                theatre.setCreated_at(rs.getTimestamp("created_at"));
                theatres.add(theatre);
            }
        }
        return theatres;
    }

    public boolean updateTheatre(Theatre theatre) throws SQLException {
        try (PreparedStatement stmt = getConnection().prepareStatement(UPDATE_THEATRE)) {
            stmt.setString(1, theatre.getName());
            stmt.setString(2, theatre.getLocation());
            stmt.setInt(3, theatre.getTotal_seats());
            stmt.setString(4, theatre.getStatus());
            stmt.setInt(5, theatre.getTheatre_id());
            return stmt.executeUpdate() > 0;
        }
    }
    public boolean deleteTheatre(int id) throws SQLException {
        String sql = "DELETE FROM theatres WHERE theatre_id = ?";
        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        }
    }

    private static final String GET_THEATRE_BY_ID = "SELECT * FROM theatres WHERE theatre_id=?";

    public Theatre getTheatreById(int theatreId) throws SQLException {
        try (PreparedStatement stmt = getConnection().prepareStatement(GET_THEATRE_BY_ID)) {
            stmt.setInt(1, theatreId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Theatre theatre = new Theatre(
                    rs.getInt("theatre_id"),
                    rs.getString("name"),
                    rs.getString("location"),
                    rs.getInt("total_seats")
                );
                theatre.setStatus(rs.getString("status"));
                theatre.setCreated_at(rs.getTimestamp("created_at"));
                return theatre;
            }
        }
        return null;
    }
}




