package com.controller;

import jakarta.servlet.ServletException;


import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import com.user.dao.UserDao;
import com.user.model.User;
import com.movie.dao.MovieDao;
import com.movie.model.Movie;
import com.showtime.dao.ShowtimeDao;
import com.showtime.model.Showtime;
import com.theatre.dao.TheatreDao;
import com.theatre.model.Theatre;
import com.booking.dao.BookingDao;
import com.booking.model.Booking;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.sql.Time;
import java.util.List;
import java.sql.Connection;
import com.util.DatabaseConnection;
import java.util.ArrayList;
import java.sql.Date;

@WebServlet("/AdminServlet")
public class AdminServlet extends HttpServlet {
    private UserDao userDao;
    private MovieDao movieDao;
    private TheatreDao theatreDao;
    private ShowtimeDao showtimeDao;
    private BookingDao bookingDao;
    private Connection connection;
    
    @Override
    public void init() throws ServletException {
        try {
            initializeConnections();
        } catch (SQLException e) {
            throw new ServletException("Error initializing DAOs", e);
        }
    }

    private void initializeConnections() throws SQLException {
        // Create single connection instance for the servlet
        connection = DatabaseConnection.getConnection();
        System.out.println("Database connection established");
        
        // Initialize DAOs with the connection
        userDao = new UserDao();
        movieDao = new MovieDao();
        theatreDao = new TheatreDao(connection);
        showtimeDao = new ShowtimeDao();
        bookingDao = new BookingDao(connection);
        
        System.out.println("All DAOs initialized successfully");
    }

    private void checkAndRefreshConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            System.out.println("Refreshing database connection");
            initializeConnections();
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        try {
            String action = request.getParameter("action");
            if (action == null) {
                action = "showDashboard";
            }
            
            HttpSession session = request.getSession();
            User user = (User) session.getAttribute("user");

            if (user == null || !user.isAdmin()) {
                response.sendRedirect("login.jsp");
                return;
            }

            switch (action) {
                case "showDashboard":
                    showDashboard(request, response);
                    break;
                case "listMovies":
                    listMovies(request, response);
                    break;
                case "listShows":
                    listShows(request, response);
                    break;
                case "listBookings":
                    listBookings(request, response);
                    break;
                case "listUsers":
                    listUsers(request, response);
                    break;
                case "listTheaters":
                    listTheaters(request, response);
                    break;
                default:
                    showDashboard(request, response);
            }
        } catch (Exception e) {
            System.err.println("Error in doGet: " + e.getMessage());
            e.printStackTrace();
            request.setAttribute("error", "System error: " + e.getMessage());
            request.getRequestDispatcher("/admin_dashboard.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String action = request.getParameter("action");
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        if (user == null || !user.isAdmin()) {
            response.sendRedirect("login.jsp");
            return;
        }

        switch (action) {
            case "addMovie":
                addMovie(request, response);
                break;
            case "updateMovie":
                updateMovie(request, response);
                break;
            case "deleteMovie":
                deleteMovie(request, response);
                break;
            case "addShow":
                addShow(request, response);
                break;
            case "updateBooking":
                updateBooking(request, response);
                break;
            case "logout":
                handleLogout(request, response);
                break;
            default:
                showDashboard(request, response);
        }
    }

    private void updateMovie(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        try {
            int movieId = Integer.parseInt(request.getParameter("movieId"));
            String title = request.getParameter("title");
            String genre = request.getParameter("genre");
            String duration = request.getParameter("duration");
            // Convert String date to SQL Date
            Date releaseDate = Date.valueOf(request.getParameter("releaseDate"));
            String description = request.getParameter("description");
            double price = Double.parseDouble(request.getParameter("price"));
            String status = request.getParameter("status");
            
            Movie movie = new Movie();
            movie.setMovieId(movieId);
            movie.setTitle(title);
            movie.setGenre(genre);
            movie.setDuration(duration);
            movie.setReleaseDate(releaseDate); // Now using proper SQL Date
            movie.setDescription(description);
            movie.setPrice(price);
            movie.setStatus(status);
            
            movieDao.updateMovie(movie);
            response.sendRedirect("AdminServlet?action=listMovies&success=true");
        } catch (IllegalArgumentException e) {
            request.setAttribute("error", "Invalid date format. Please use YYYY-MM-DD format.");
            request.getRequestDispatcher("/admin_dashboard.jsp").forward(request, response);
        } catch (Exception e) {
            request.setAttribute("error", "Error updating movie: " + e.getMessage());
            request.getRequestDispatcher("/admin_dashboard.jsp").forward(request, response);
        }
    }

    private void deleteMovie(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        try {
            int movieId = Integer.parseInt(request.getParameter("movieId"));
            movieDao.deleteMovie(movieId);
            response.sendRedirect("AdminServlet?action=listMovies&success=true");
        } catch (Exception e) {
            request.setAttribute("error", "Error deleting movie: " + e.getMessage());
            request.getRequestDispatcher("/admin_dashboard.jsp").forward(request, response);
        }
    }

private void showDashboard(HttpServletRequest request, HttpServletResponse response) 
        throws ServletException, IOException {
    try {
        System.out.println("Fetching dashboard data...");
        checkAndRefreshConnection(); // Add this line

        // Use single queries with proper connection handling
        List<Movie> movies;
        List<Theatre> theaters;
        List<Showtime> shows;
        List<User> users;
        int totalBookings;
        double totalRevenue;

        // Get all data in try-with-resources blocks
        try {
            movies = movieDao.getAllMovies();
            theaters = theatreDao.getAllTheatres();
            shows = showtimeDao.getAllShowtimes();
            users = userDao.selectAllUsers();
            totalBookings = bookingDao.getTotalBookings();
            totalRevenue = bookingDao.getTotalRevenue();

            // Add bookings loading
            List<Booking> bookings = bookingDao.getAllBookings();
            System.out.println("Loaded bookings: " + (bookings != null ? bookings.size() : 0));

            // Set attributes
            request.setAttribute("movies", movies);
            request.setAttribute("theaters", theaters);
            request.setAttribute("shows", shows);
            request.setAttribute("users", users);
            request.setAttribute("bookings", bookings);  // Add this line
            request.setAttribute("totalBookings", totalBookings);
            request.setAttribute("totalRevenue", totalRevenue);

            // Debug logs
            System.out.println("Dashboard data loaded successfully");
            System.out.println("Movies: " + (movies != null ? movies.size() : 0));
            System.out.println("Theaters: " + (theaters != null ? theaters.size() : 0));
            System.out.println("Shows: " + (shows != null ? shows.size() : 0));
        } catch (SQLException e) {
            throw new ServletException("Error fetching dashboard data", e);
        }

        // Forward to dashboard
        request.getRequestDispatcher("/admin_dashboard.jsp").forward(request, response);
        
    } catch (Exception e) {
        System.err.println("Error loading dashboard: " + e.getMessage());
        e.printStackTrace();
        request.setAttribute("error", "Error loading dashboard: " + e.getMessage());
        request.getRequestDispatcher("/admin_dashboard.jsp").forward(request, response);
    }
}

    private void addMovie(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        try {
            String title = request.getParameter("title");
            String genre = request.getParameter("genre");
            String duration = request.getParameter("duration");
            // Convert String date to SQL Date
            Date releaseDate = Date.valueOf(request.getParameter("releaseDate"));
            String description = request.getParameter("description");
            double price = Double.parseDouble(request.getParameter("price"));
            
            Movie movie = new Movie();
            movie.setTitle(title);
            movie.setGenre(genre);
            movie.setDuration(duration);
            movie.setReleaseDate(releaseDate); // Now using proper SQL Date
            movie.setDescription(description);
            movie.setPrice(price);
            movie.setStatus("ACTIVE"); // Set default status
            
            // Fix: Use movieDao instead of userDao
            movieDao.addMovie(movie);
            
            response.sendRedirect("AdminServlet?action=listMovies&success=true");
        } catch (IllegalArgumentException e) {
            request.setAttribute("error", "Invalid date format. Please use YYYY-MM-DD format.");
            request.getRequestDispatcher("/admin_dashboard.jsp").forward(request, response);
        } catch (Exception e) {
            request.setAttribute("error", "Error adding movie: " + e.getMessage());
            request.getRequestDispatcher("/admin_dashboard.jsp").forward(request, response);
        }
    }

    private void listMovies(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        try {
            // Get movies from database
            List<Movie> movies = movieDao.getAllMovies();
            
            // Debug logging
            System.out.println("Fetched movies: " + (movies != null ? movies.size() : 0));
            if (movies != null && !movies.isEmpty()) {
                System.out.println("First movie: " + movies.get(0).getTitle());
            }
            
            // Set the movies attribute
            request.setAttribute("movies", movies);
            
            // Check if it's an AJAX request
            String isAjax = request.getHeader("X-Requested-With");
            if ("XMLHttpRequest".equals(isAjax)) {
                // If AJAX, forward to movies section only
                request.getRequestDispatcher("/WEB-INF/partials/movies-table.jsp").forward(request, response);
            } else {
                // If not AJAX, forward to full dashboard
                request.getRequestDispatcher("/admin_dashboard.jsp").forward(request, response);
            }
        } catch (Exception e) {
            System.err.println("Error in listMovies: " + e.getMessage());
            e.printStackTrace();
            request.setAttribute("error", "Error loading movies: " + e.getMessage());
            request.getRequestDispatcher("/admin_dashboard.jsp").forward(request, response);
        }
    }

    private void listUsers(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        List<User> users = userDao.selectAllUsers();
        request.setAttribute("users", users);
        request.getRequestDispatcher("/admin_dashboard.jsp").forward(request, response);
    }

    private void listTheaters(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException, SQLException {
        List<Theatre> theaters = theatreDao.getAllTheatres();
        request.setAttribute("theaters", theaters);
        request.getRequestDispatcher("/admin_dashboard.jsp").forward(request, response);
    }

    private void addTheater(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException, SQLException {
        String name = request.getParameter("theaterName");
        String location = request.getParameter("location");
        int totalSeats = Integer.parseInt(request.getParameter("totalSeats"));
        
        Theatre theatre = new Theatre(0, name, location, totalSeats);
        theatreDao.addTheatre(theatre);
        
        response.sendRedirect("AdminServlet?action=listTheaters");
    }

    private void addShow(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        try {
            int movieId = Integer.parseInt(request.getParameter("movieId"));
            int theaterId = Integer.parseInt(request.getParameter("theaterId"));
            String showDateStr = request.getParameter("showDate");
            String showTimeStr = request.getParameter("showTime");
            Double ticketPrice = Double.parseDouble(request.getParameter("ticketPrice"));
            
            // Convert String dates to SQL Date
            Date showDate = Date.valueOf(showDateStr);
            Time showTime = Time.valueOf(showTimeStr);
            
            Showtime show = new Showtime();
            show.setMovie_id(movieId);
            show.setTheatre_id(theaterId);
            show.setShow_date(showDate);
            show.setShow_time(showTime);
            show.setPrice(ticketPrice);
            
            showtimeDao.addShowtime(show);
            
            response.sendRedirect("AdminServlet?action=listShows");
        } catch (Exception e) {
            request.setAttribute("error", "Error adding show: " + e.getMessage());
            request.getRequestDispatcher("/admin_dashboard.jsp").forward(request, response);
        }
    }

    private void listShows(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException, SQLException {
        List<Showtime> shows = showtimeDao.getAllShowtimes();
        request.setAttribute("shows", shows);
        request.getRequestDispatcher("/admin_dashboard.jsp").forward(request, response);
    }

    private void updateBooking(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        try {
            int bookingId = Integer.parseInt(request.getParameter("bookingId"));
            String bookingStatus = request.getParameter("status");
            
            Booking booking = bookingDao.getBookingById(bookingId);
            if (booking != null) {
                // Update booking status using proper method
                booking.setBooking_status(bookingStatus); // Make sure this method exists in Booking class
                bookingDao.updateBooking(booking);
            }
            
            response.sendRedirect("AdminServlet?action=listBookings");
        } catch (Exception e) {
            request.setAttribute("error", "Error updating booking: " + e.getMessage());
            request.getRequestDispatcher("/admin_dashboard.jsp").forward(request, response);
        }
    }

    private void listBookings(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException, SQLException {
    try {
        List<Booking> bookings = bookingDao.getAllBookings();
        System.out.println("Retrieved " + bookings.size() + " bookings"); // Debug log
        
        if (bookings != null && !bookings.isEmpty()) {
            System.out.println("First booking: " + bookings.get(0).toString()); // Debug log
        }
        
        request.setAttribute("bookings", bookings);
        request.getRequestDispatcher("/admin_dashboard.jsp").forward(request, response);
    } catch (Exception e) {
        System.err.println("Error loading bookings: " + e.getMessage());
        e.printStackTrace();
        request.setAttribute("error", "Error loading bookings: " + e.getMessage());
        request.getRequestDispatcher("/admin_dashboard.jsp").forward(request, response);
    }
}

    private void handleLogout(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        try {
            // Invalidate session first
            HttpSession session = request.getSession(false);
            if (session != null) {
                session.invalidate();
            }

            // Then close the connection
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
            
            // Clean up resources
            userDao = null;
            movieDao = null;
            theatreDao = null;
            showtimeDao = null;
            bookingDao = null;
            connection = null;
            
            // Redirect to login page
            response.sendRedirect("login.jsp");
        } catch (SQLException e) {
            System.err.println("Error during logout: " + e.getMessage());
            request.setAttribute("error", "Error during logout");
            request.getRequestDispatcher("/admin_dashboard.jsp").forward(request, response);
        }
    }

    @Override
    public void destroy() {
        // Only clean up non-connection resources
        userDao = null;
        movieDao = null;
        theatreDao = null;
        showtimeDao = null;
        bookingDao = null;
    }
}
