package com.showtime.dao;

import com.showtime.model.Showtime;


	    
	    
	    import java.sql.*;
	    import java.util.ArrayList;
	    import java.util.List;

	    public class ShowtimeDao {
	        private Connection connection;

	        public ShowtimeDao(Connection connection) {
	            this.connection = connection;
	        }

	        public boolean addShowtime(Showtime showtime) throws SQLException {
	            String sql = "INSERT INTO showtimes (showtimeId,movieId, theatre_id, showtime) VALUES (?, ?, ?,?)";
	            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
	                stmt.setInt(1, showtime.getMovieId());
	                stmt.setInt(2, showtime.getTheatreId());
	                stmt.setTimestamp(3, new java.sql.Timestamp(showtime.getShowtime().getTime()));
	                return stmt.executeUpdate() > 0;
	            }
	        }

	        public Showtime getShowtime(int id) throws SQLException {
	            String sql = "SELECT * FROM showtimes WHERE id = ?";
	            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
	                stmt.setInt(1, id);
	                ResultSet rs = stmt.executeQuery();
	                if (rs.next()) {
	                    return new Showtime(
	                        rs.getInt("showtimeId"),
	                        rs.getInt("movieId"),
	                        rs.getInt("theatreId"),
	                        rs.getTimestamp("showtime")
	                    );
	                }
	            }
	            return null;
	        }

	        public List<Showtime> getAllShowtimes() throws SQLException {
	            String sql = "SELECT * FROM showtimes";
	            List<Showtime> showtimes = new ArrayList<>();
	            try (Statement stmt = connection.createStatement()) {
	                ResultSet rs = stmt.executeQuery(sql);
	                while (rs.next()) {
	                    showtimes.add(new Showtime(
	                        rs.getInt("showtimeId"),
	                        rs.getInt("movieId"),
	                        rs.getInt("theatreId"),
	                        rs.getTimestamp("showtime")
	                    ));
	                }
	            }
	            return showtimes;
	        }

	        public boolean updateShowtime(Showtime showtime) throws SQLException {
	            String sql = "UPDATE showtimes SET movieId = ?, theatreId = ?, showtime = ? WHERE id = ?";
	            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
	                stmt.setInt(1, showtime.getMovieId());
	                stmt.setInt(2, showtime.getTheatreId());
	                stmt.setTimestamp(3, new java.sql.Timestamp(showtime.getShowtime().getTime()));
	                stmt.setInt(4, showtime.getShowtimeId());
	                return stmt.executeUpdate() > 0;
	            }
	        }

	        public boolean deleteShowtime(int id) throws SQLException {
	            String sql = "DELETE FROM showtimes WHERE id = ?";
	            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
	                stmt.setInt(1, id);
	                return stmt.executeUpdate() > 0;
	            }
	        }
	    }

	    
	    
	    
	    
	
