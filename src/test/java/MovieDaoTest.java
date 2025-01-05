import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.*;
import java.sql.SQLException;
import com.movie.dao.MovieDao;
import com.movie.model.Movie;


import java.util.List;
import com.showtime.dao.ShowtimeDao;
import com.booking.dao.BookingDao;
import com.util.DatabaseConnection;

public class MovieDaoTest {

    private MovieDao movieDao;

    @BeforeAll
    static void globalSetup() {
        // Initialize any shared resources
    }
    
    @BeforeEach
    void setUp() {
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
        existingMovie.setMovieId(1); 
        existingMovie.setTitle("New Title");
        existingMovie.setDescription("Updated Description");
        existingMovie.setGenre("Comedy");
        existingMovie.setDuration("2h 30m");
        //some left
        existingMovie.setStatus("CONFIRMED");
        
        existingMovie.setImageUrl("www.example.com");
        existingMovie.setPrice(34);
        
        
      
        
       
        // ...set other fields, including releaseDate, status, rating, imageUrl, price...
        
        boolean result = movieDao.updateMovie(existingMovie);
        assertTrue(result, "Movie should be updated successfully");
    }

    @Test
    void testDeleteMovie() throws SQLException {
        BookingDao bookingDao = new BookingDao(DatabaseConnection.getConnection());
        bookingDao.deleteBookingsByShowtime(1); // Remove all bookings referencing showtime=1
        ShowtimeDao showtimeDao = new ShowtimeDao();
        showtimeDao.deleteShowtime(1); // Now safely remove the showtime
        boolean result = movieDao.deleteMovie(1); // Finally remove the movie
        assertTrue(result, "Movie should be deleted successfully");
    }

    @Test
    void testSearchMovies() {
        List<Movie> found = movieDao.searchMovies("test");
        assertNotNull(found, "Search result should not be null");
    }

    @AfterAll
    static void globalTearDown() {
        // Release any shared resources
    }
}
