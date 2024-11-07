package com.movie.dao;

import com.movie.model.Movie;


	    
	    
	   

	   
	    import java.sql.*;
	    import java.util.ArrayList;
	    import java.util.List;

	    public class MovieDao {
	        private Connection connection;

	        public MovieDao(Connection connection) {
	            this.connection = connection;
	        }

	        public boolean addMovie(Movie movie) throws SQLException {
	            String sql = "INSERT INTO movies (title, genre, duration) VALUES (?, ?, ?)";
	            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
	                stmt.setString(1, movie.getTitle());
	                stmt.setString(2, movie.getGenre());
	                stmt.setInt(3, movie.getDuration());
	                return stmt.executeUpdate() > 0;
	            }
	        }

	        public Movie getMovie(int id) throws SQLException {
	            String sql = "SELECT * FROM movies WHERE id = ?";
	            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
	                stmt.setInt(1, id);
	                ResultSet rs = stmt.executeQuery();
	                if (rs.next()) {
	                    return new Movie(
	                        rs.getInt("id"),
	                        rs.getString("title"),
	                        rs.getString("genre"),
	                        rs.getInt("duration")
	                    );
	                }
	            }
	            return null;
	        }

	        public List<Movie> getAllMovies() throws SQLException {
	            String sql = "SELECT * FROM movies";
	            List<Movie> movies = new ArrayList<>();
	            try (Statement stmt = connection.createStatement()) {
	                ResultSet rs = stmt.executeQuery(sql);
	                while (rs.next()) {
	                    movies.add(new Movie(
	                        rs.getInt("id"),
	                        rs.getString("title"),
	                        rs.getString("genre"),
	                        rs.getInt("duration")
	                    ));
	                }
	            }
	            return movies;
	        }

	        public boolean updateMovie(Movie movie) throws SQLException {
	            String sql = "UPDATE movies SET title = ?, genre = ?, duration = ? WHERE id = ?";
	            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
	                stmt.setString(1, movie.getTitle());
	                stmt.setString(2, movie.getGenre());
	                stmt.setInt(3, movie.getDuration());
	                stmt.setInt(4, movie.getMovieId());
	                return stmt.executeUpdate() > 0;
	            }
	        }

	        public boolean deleteMovie(int id) throws SQLException {
	            String sql = "DELETE FROM movies WHERE id = ?";
	            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
	                stmt.setInt(1, id);
	                return stmt.executeUpdate() > 0;
	            }
	        }
	    }

	    
	    
	    
	

