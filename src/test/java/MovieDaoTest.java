import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.*;
import java.sql.SQLException;
import java.sql.Statement;

import com.movie.dao.MovieDao;
import com.movie.model.Movie;


import java.util.List;
import com.util.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class MovieDaoTest {

    private MovieDao movieDao;
    private Connection connection; // Add connection field

    @BeforeAll
    static void globalSetup() {
        // Initialize any shared resources
    }
    
    @BeforeEach
    void setUp() throws SQLException {
        connection = DatabaseConnection.getConnection(); // Initialize connection
        movieDao = new MovieDao();
    }
    
    @Test
    void testAddMovie() throws SQLException {
        Movie movie = new Movie();
        movie.setTitle("Test Movie");
        movie.setDescription("Some description");
        movie.setGenre("Action");
        movie.setDuration("150");
        boolean result = movieDao.addMovie(movie);
        assertTrue(result, "Movie should be added successfully");
    }

    @Test
    void testGetAllMovies() {
        List<Movie> movies = movieDao.getAllMovies();
        assertNotNull(movies, "Movie list should not be null");
    }

    @Test
    void testUpdateMovie() throws SQLException {
        // Retrieve or add a valid movie from DB to get its existing ID
    	
    	
        Movie existingMovie = new Movie();
        existingMovie.setMovieId(9); 
        existingMovie.setTitle("New Title");
        existingMovie.setDescription("Updated Description");
        existingMovie.setGenre("Comedy");
        existingMovie.setDuration("2h 30m");
        
        // Fix date format: Use java.sql.Date
        existingMovie.setReleaseDate(java.sql.Date.valueOf("2024-01-01"));
        existingMovie.setStatus("CONFIRMED");
        
        // Fix rating: Use BigDecimal instead of double
        existingMovie.setRating(java.math.BigDecimal.valueOf(5.7));
        
        existingMovie.setImageUrl("www.example.com");
        existingMovie.setPrice(34.0);
        
        boolean result = movieDao.updateMovie(existingMovie);
        assertTrue(result, "Movie should be updated successfully");
    }
    

    @Test
    void testDeleteMovie() throws SQLException {
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            
            // First add a test movie with required fields
            Movie movie = createTestMovie();
            
            // Add movie and get its ID
            try (PreparedStatement stmt = conn.prepareStatement(
                "INSERT INTO movies (title, description, genre, duration, release_date, status, rating, image_url, price) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)", 
                Statement.RETURN_GENERATED_KEYS)) {
                
                setMovieParameters(stmt, movie);
                int affectedRows = stmt.executeUpdate();
                assertTrue(affectedRows > 0, "Movie should be inserted");
                
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    assertTrue(generatedKeys.next(), "Should have generated keys");
                    int movieId = generatedKeys.getInt(1);
                    assertTrue(movieId > 0, "Should have valid movie ID");
                    
                    // Clean up related records
                    cleanupRelatedRecords(movieId, conn);
                    
                    // Now test the delete
                    boolean result = movieDao.deleteMovie(movieId);
                    assertTrue(result, "Movie should be deleted successfully");
                    
                    // Verify deletion
                    verifyMovieDeletion(movieId, conn);
                }
            }
        } finally {
            if (conn != null && !conn.isClosed()) {
                conn.close();
            }
        }
    }

    private Movie createTestMovie() {
        Movie movie = new Movie();
        movie.setTitle("Test Delete Movie");
        movie.setDescription("Test Description");
        movie.setGenre("Action");
        movie.setDuration("120");
        movie.setStatus("ACTIVE");
        movie.setReleaseDate(java.sql.Date.valueOf("2024-01-01"));
        movie.setRating(java.math.BigDecimal.valueOf(4.5));
        movie.setImageUrl("test.jpg");
        movie.setPrice(10.0);
        return movie;
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

    private void cleanupRelatedRecords(int movieId, Connection conn) throws SQLException {
        // Delete bookings first
        try (PreparedStatement stmt = conn.prepareStatement(
            "DELETE FROM bookings WHERE showtime_id IN (SELECT showtime_id FROM showtimes WHERE movie_id = ?)")) {
            stmt.setInt(1, movieId);
            stmt.executeUpdate();
        }
        
        // Then delete showtimes
        try (PreparedStatement stmt = conn.prepareStatement(
            "DELETE FROM showtimes WHERE movie_id = ?")) {
            stmt.setInt(1, movieId);
            stmt.executeUpdate();
        }
    }

    private void verifyMovieDeletion(int movieId, Connection conn) throws SQLException {
        try (PreparedStatement verifyStmt = conn.prepareStatement("SELECT COUNT(*) FROM movies WHERE movie_id = ?")) {
            verifyStmt.setInt(1, movieId);
            try (ResultSet rs = verifyStmt.executeQuery()) {
                assertTrue(rs.next());
                assertEquals(0, rs.getInt(1), "Movie should not exist after deletion");
            }
        }
    }

    @Test
    void testSearchMovies() {
        List<Movie> found = movieDao.searchMovies("test");
        assertNotNull(found, "Search result should not be null");
    }

    @AfterEach
    void cleanup() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            // Clean up test data
            try (PreparedStatement stmt = connection.prepareStatement("DELETE FROM movies WHERE title LIKE 'Test%'")) {
                stmt.executeUpdate();
            }
            connection.close();
        }
    }

    @AfterAll
    static void globalCleanup() throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection()) {
            // Clean up any remaining test data
            try (PreparedStatement stmt = conn.prepareStatement("DELETE FROM movies WHERE title LIKE 'Test%'")) {
                stmt.executeUpdate();
            }
        }
    }
}
