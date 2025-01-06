package com.movie.dao;

import java.sql.*;


import com.movie.model.Movie;
import java.util.ArrayList;
import java.util.List;

import com.util.Constants;
import com.util.DatabaseConnection;

public class MovieDao {
    private String jdbcURL = Constants.DB_URL;
    private String jdbcUsername = Constants.DB_USERNAME;
    private String jdbcPassword = Constants.DB_PASSWORD;

    private static final String SELECT_ALL_MOVIES = 
        "SELECT movie_id, title, description, genre, duration, release_date, status, rating, image_url, price, created_at FROM movies";
    private static final String SEARCH_MOVIES = 
        "SELECT movie_id, title, description, genre, rating, image_url, price, created_at FROM movies WHERE title LIKE ? OR genre LIKE ?";
    
    private static final String ADD_MOVIE =  "INSERT INTO movies ( title, description, genre, duration, release_date,status, rating, image_url, price ) VALUES (?, ?, ?, ?, ?, ?, ?, ?,?)";
    
    private static final String DELETE_MOVIE =  "DELETE FROM movies WHERE movie_id=?";
    
    private static final String UPDATE_MOVIE = 
        "UPDATE movies SET title=?, description=?, genre=?, duration=?, release_date=?, status=?, rating=?, image_url=?, price=? "
      + "WHERE movie_id=?";

    public MovieDao() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public List<Movie> getAllMovies() {
        List<Movie> movies = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(jdbcURL, jdbcUsername, jdbcPassword);
             PreparedStatement ps = connection.prepareStatement(SELECT_ALL_MOVIES)) {
            
            System.out.println("Executing query: " + SELECT_ALL_MOVIES);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                Movie movie = new Movie();
                movie.setMovieId(rs.getInt("movie_id"));
                movie.setTitle(rs.getString("title"));
                movie.setDescription(rs.getString("description"));
                movie.setGenre(rs.getString("genre"));
                movie.setDuration(rs.getString("duration"));
                movie.setReleaseDate(rs.getDate("release_date")); // Make sure this line exists
                movie.setStatus(rs.getString("status"));
                movie.setRating(rs.getBigDecimal("rating"));
                movie.setImageUrl(rs.getString("image_url"));
                movie.setPrice(rs.getDouble("price"));
                movie.setCreatedAt(rs.getTimestamp("created_at"));
                movies.add(movie);
                System.out.println("Loaded movie: " + movie.getTitle() + ", Release Date: " + movie.getReleaseDate()); // Add debug log
            }
        } catch (SQLException e) {
            System.err.println("Database error: " + e.getMessage());
            e.printStackTrace();
        }
        return movies;
    }

    public List<Movie> searchMovies(String query) {
        List<Movie> movies = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(jdbcURL, jdbcUsername, jdbcPassword);
             PreparedStatement ps = connection.prepareStatement(SEARCH_MOVIES)) {
            String searchQuery = "%" + query + "%";
            ps.setString(1, searchQuery);
            ps.setString(2, searchQuery);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Movie movie = new Movie();
                movie.setMovieId(rs.getInt("movie_id")); // Fix column name
                movie.setTitle(rs.getString("title"));
                movie.setDescription(rs.getString("description"));
                movie.setGenre(rs.getString("genre"));
                movie.setRating(rs.getBigDecimal("rating"));
                movie.setImageUrl(rs.getString("image_url"));
                movie.setPrice(rs.getDouble("price"));
                movie.setCreatedAt(rs.getTimestamp("created_at"));
                movies.add(movie);
            }
        } catch (SQLException e) {
            System.err.println("Search error: " + e.getMessage());
            e.printStackTrace();
        }
        return movies;
    }

    public List<Movie> getRecentMovies(int limit) {
        List<Movie> movies = new ArrayList<>();
        String query = SELECT_ALL_MOVIES + " ORDER BY created_at DESC LIMIT ?";
        
        try (Connection connection = DriverManager.getConnection(jdbcURL, jdbcUsername, jdbcPassword);
             PreparedStatement ps = connection.prepareStatement(query)) {
            
            ps.setInt(1, limit);
            System.out.println("Executing query: " + query);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                Movie movie = new Movie();
                movie.setMovieId(rs.getInt("movie_id"));
                movie.setTitle(rs.getString("title"));
                movie.setDescription(rs.getString("description"));
                movie.setGenre(rs.getString("genre"));
                movie.setRating(rs.getBigDecimal("rating"));
                movie.setImageUrl(rs.getString("image_url"));
                movie.setPrice(rs.getDouble("price"));
                movie.setCreatedAt(rs.getTimestamp("created_at"));
                movies.add(movie);
                System.out.println("Loaded recent movie: " + movie.getTitle());
            }
        } catch (SQLException e) {
            System.err.println("Database error: " + e.getMessage());
            e.printStackTrace();
        }
        return movies;
    }

    public boolean addMovie(Movie movie) throws SQLException {
        try (Connection connection = DriverManager.getConnection(jdbcURL, jdbcUsername, jdbcPassword);
             PreparedStatement stmt = connection.prepareStatement(ADD_MOVIE, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, movie.getTitle());
            stmt.setString(2, movie.getDescription());
            stmt.setString(3, movie.getGenre());
            stmt.setString(4, movie.getDuration());
            stmt.setDate(5, movie.getReleaseDate());
            stmt.setString(6, movie.getStatus());
            stmt.setBigDecimal(7, movie.getRating());
            stmt.setString(8, movie.getImageUrl());
            stmt.setDouble(9, movie.getPrice());
            
            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        movie.setMovieId(generatedKeys.getInt(1));
                        return true;
                    }
                }
            }
            return false;
        }
    }

    public boolean updateMovie(Movie movie) throws SQLException {
        try (Connection connection = DriverManager.getConnection(jdbcURL, jdbcUsername, jdbcPassword);
             PreparedStatement stmt = connection.prepareStatement(UPDATE_MOVIE)) {
            stmt.setString(1, movie.getTitle());
            stmt.setString(2, movie.getDescription());
            stmt.setString(3, movie.getGenre());
            stmt.setString(4, movie.getDuration());
            stmt.setDate(5, movie.getReleaseDate());
            stmt.setString(6, movie.getStatus());
            stmt.setBigDecimal(7, movie.getRating());
            stmt.setString(8, movie.getImageUrl());
            stmt.setDouble(9, movie.getPrice());
            stmt.setInt(10, movie.getMovieId());
            return stmt.executeUpdate() > 0;
        }
    }

    public boolean deleteMovie(int movieId) throws SQLException {
        try (Connection connection = DriverManager.getConnection(jdbcURL, jdbcUsername, jdbcPassword);
             PreparedStatement stmt = connection.prepareStatement(DELETE_MOVIE)) {
            stmt.setInt(1, movieId);
            return stmt.executeUpdate() > 0;
        }
    }

    
    private void setMovieParameters(PreparedStatement stmt, Movie movie) throws SQLException {
        stmt.setString(1, movie.getTitle());
        stmt.setString(2, movie.getDescription());
        stmt.setString(3, movie.getGenre());
        stmt.setString(4, movie.getDuration());
        stmt.setDate(5, movie.getReleaseDate());
        stmt.setString(6, movie.getStatus());
        stmt.setBigDecimal(7, movie.getRating());
        stmt.setString(8, movie.getImageUrl());
        stmt.setDouble(9, movie.getPrice());
    }

    public int getLastInsertedId() throws SQLException {
        String sql = "SELECT LAST_INSERT_ID()";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getInt(1);
            }
            return -1;
        }
    }
    
}



