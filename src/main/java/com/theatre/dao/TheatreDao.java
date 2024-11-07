package com.theatre.dao;

import com.theatre.model.Theatre;



	  
	    import java.sql.*;
	    import java.util.ArrayList;
	    import java.util.List;

	    public class TheatreDao {
	        private Connection connection;

	        public TheatreDao(Connection connection) {
	            this.connection = connection;
	        }

	        public boolean addTheatre(Theatre theatre) throws SQLException {
	            String sql = "INSERT INTO theatres (theatre_id,name, location,total_seats) VALUES (?, ?,?,?)";
	            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
	               
	            	stmt.setInt(1, theatre.getTheatreId());
	            	stmt.setString(2, theatre.getName());
	                stmt.setString(3, theatre.getLocation());
	                stmt.setInt(4, theatre.getToatalseats());
	               
	                return stmt.executeUpdate() > 0;
	            }
	        }

	        public Theatre getTheatre(int id) throws SQLException {
	            String sql = "SELECT * FROM theatres WHERE theatre_id = ?";
	            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
	                stmt.setInt(1, id);
	                ResultSet rs = stmt.executeQuery();
	                if (rs.next()) {
	                    return new Theatre(
	                        rs.getInt("theatre_id"),
	                        rs.getString("name"),
	                        rs.getString("location"),
	                        rs.getInt("totalseats")
	                    );
	                }
	            }
	            return null;
	        }

	        
	        public List<Theatre> getAllTheatres() throws SQLException {
	            String sql = "SELECT * FROM theatres";
	            List<Theatre> theatres = new ArrayList<>();
	            try (Statement stmt = connection.createStatement()) {
	                ResultSet rs = stmt.executeQuery(sql);
	                while (rs.next()) {
	                    theatres.add(new Theatre(
	                        rs.getInt("id"),
	                        rs.getString("name"),
	                        rs.getString("location"),
	                        rs.getInt("totalseats")
	                    ));
	                }
	            }
	            return theatres;
	        }

	        public boolean updateTheatre(Theatre theatre) throws SQLException {
	            String sql = "UPDATE theatres SET name = ?, location = ? WHERE theatre_id = ?";
	            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
	                stmt.setString(1, theatre.getName());
	                stmt.setString(2, theatre.getLocation());
	                stmt.setInt(3, theatre.getTheatreId());
	                return stmt.executeUpdate() > 0;
	            }
	        }

	        public boolean deleteTheatre(int id) throws SQLException {
	            String sql = "DELETE FROM theatres WHERE theatre_id = ?";
	            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
	                stmt.setInt(1, id);
	                return stmt.executeUpdate() > 0;
	            }
	        }
	    }

	    
	    

