package com.movie.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import com.movie.model.Movie;

public class MovieDao {
    private String jdbcURL = "jdbc:mysql://localhost:3306/Movie_Ticket_Booking_System";
    private String jdbcUsername = "root";
    private String jdbcPassword = "Nitin@1513";

    private static final String SELECT_ALL_MOVIES = 
        "SELECT movie_id, title, description, genre, rating, image_url, price, created_at FROM movies";
    private static final String SEARCH_MOVIES = 
        "SELECT movie_id, title, description, genre, rating, image_url, price, created_at FROM movies WHERE title LIKE ? OR genre LIKE ?";

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
                movie.setRating(rs.getDouble("rating"));
                movie.setImageUrl(rs.getString("image_url"));
                movie.setPrice(rs.getDouble("price"));
                movie.setCreatedAt(rs.getTimestamp("created_at"));
                movies.add(movie);
                System.out.println("Loaded movie: " + movie.getTitle());
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
                movie.setRating(rs.getDouble("rating"));
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
                movie.setRating(rs.getDouble("rating"));
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
}



