package com.controller;

import jakarta.servlet.RequestDispatcher;
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
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.sql.SQLException;
import java.sql.Time;
import java.util.List;
import java.sql.Connection;
import com.util.DatabaseConnection;

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
            // Create single connection instance for the servlet
            connection = DatabaseConnection.getConnection();
            userDao = new UserDao();
            movieDao = new MovieDao();
            theatreDao = new TheatreDao(connection);
            showtimeDao = new ShowtimeDao();
            bookingDao = new BookingDao(connection);
            System.out.println("All DAOs initialized successfully");
        } catch (SQLException e) {
            throw new ServletException("Error initializing DAOs", e);
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
            case "addTheater":
			try {
				addTheater(request, response);
			} catch (ServletException | IOException | SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            	break;
            case "deleteTheater":
            	deleteTheater(request, response);
            	
            	break;
            case "updateTheater":
            	updateTheater(request, response);
            default:
                showDashboard(request, response);
        }
    }
    
    /*
     * private void updateTheater(HttpServletRequest request, HttpServletResponse response) 
        throws ServletException, IOException {
    Connection conn = null;
    try {
        // Get parameters
        int theaterId = Integer.parseInt(request.getParameter("theaterId"));
        String name = request.getParameter("name");
        String location = request.getParameter("location");
        int totalSeats = Integer.parseInt(request.getParameter("totalSeats"));
        String status = request.getParameter("status");

        // Create Theater object
        Theatre theatre = new Theatre(theaterId, name, location, totalSeats);
        theatre.setStatus(status);

        // Update theater
        conn = DatabaseConnection.getConnection();
        TheatreDao theatreDao = new TheatreDao(conn);
        boolean success = theatreDao.updateTheatre(theatre);

        // Redirect with appropriate message
        String redirectUrl = "AdminServlet?action=showDashboard&tab=theaters";
        if (success) {
            redirectUrl += "&message=" + URLEncoder.encode("Theater updated successfully", "UTF-8");
        } else {
            redirectUrl += "&error=" + URLEncoder.encode("Failed to update theater", "UTF-8");
        }
        response.sendRedirect(redirectUrl);

    } catch (Exception e) {
        response.sendRedirect("AdminServlet?action=showDashboard&tab=theaters&error=" + 
            URLEncoder.encode("Error: " + e.getMessage(), "UTF-8"));
    } finally {
        try {
            if (conn != null && !conn.isClosed()) {
                conn.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
     * 
     */

    private void updateMovie(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        try {
            int movieId = Integer.parseInt(request.getParameter("movieId"));
            String title = request.getParameter("title");
            String description = request.getParameter("description");
            String genre = request.getParameter("genre");
            String duration = request.getParameter("duration");          
            Date releaseDate = Date.valueOf(request.getParameter("releaseDate"));
            String status = request.getParameter("status");
            BigDecimal rating = new BigDecimal(request.getParameter("Rating"));
            String ImageUrl = request.getParameter("ImageUrl");
            double price = Double.parseDouble(request.getParameter("price"));
          
            
            Movie movie = new Movie();
            movie.setMovieId(movieId);
            movie.setTitle(title);
            movie.setGenre(genre);
            movie.setDuration(duration);
            movie.setReleaseDate(releaseDate); // Now using proper SQL Date
            movie.setDescription(description);
            movie.setPrice(price);
            movie.setStatus(status);
            movie.setImageUrl(ImageUrl);
            movie.setRating(rating);
   
            
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
        // Load dashboard data
        loadDashboardData(request);
        
        // Get messages from parameters
        String message = request.getParameter("message");
        String error = request.getParameter("error");
        String tab = request.getParameter("tab");
        
        if (message != null) {
            request.setAttribute("message", URLDecoder.decode(message, "UTF-8"));
        }
        if (error != null) {
            request.setAttribute("error", URLDecoder.decode(error, "UTF-8"));
        }
        if (tab != null) {
            request.setAttribute("activeTab", tab);
        }

        // Forward to dashboard
        RequestDispatcher dispatcher = request.getRequestDispatcher("/admin_dashboard.jsp");
        dispatcher.forward(request, response);
        
    } catch (Exception e) {
        e.printStackTrace();
        if (!response.isCommitted()) {
            response.sendRedirect("error.jsp?message=" + URLEncoder.encode(e.getMessage(), "UTF-8"));
        }
    }
}

private void loadDashboardData(HttpServletRequest request) throws SQLException {
    Connection conn = DatabaseConnection.getConnection();
    
    // Initialize DAOs
    TheatreDao theatreDao = new TheatreDao(conn);
    MovieDao movieDao = new MovieDao();
    ShowtimeDao showtimeDao = new ShowtimeDao();
    BookingDao bookingDao = new BookingDao(conn);
    UserDao userDao = new UserDao();

    try {
        // Load basic entities
        List<Theatre> theaters = theatreDao.getAllTheatres();
        List<Movie> movies = movieDao.getAllMovies();
        List<Showtime> shows = showtimeDao.getAllShowtimes();
        List<Booking> bookings = bookingDao.getAllBookings();
        List<User> users = userDao.selectAllUsers();

        // Calculate statistics
        double totalRevenue = bookings.stream()
            .mapToDouble(Booking::getTotalAmount)
            .sum();
            
        long totalBookings = bookings.size();
        long activeMovies = movies.stream()
            .filter(m -> "ACTIVE".equals(m.getStatus()))
            .count();
        long activeTheaters = theaters.stream()
            .filter(t -> "ACTIVE".equals(t.getStatus()))
            .count();

        // Set attributes for JSP
        request.setAttribute("theaters", theaters);
        request.setAttribute("movies", movies);
        request.setAttribute("shows", shows);
        request.setAttribute("bookings", bookings);
        request.setAttribute("users", users);
        
        // Set statistics
        request.setAttribute("totalRevenue", totalRevenue);
        request.setAttribute("totalBookings", totalBookings);
        request.setAttribute("activeMovies", activeMovies);
        request.setAttribute("activeTheaters", activeTheaters);
        
        // Debug logging
        System.out.println("Dashboard data loaded successfully:");
        System.out.println("Theaters: " + theaters.size());
        System.out.println("Movies: " + movies.size());
        System.out.println("Shows: " + shows.size());
        System.out.println("Bookings: " + bookings.size());
        System.out.println("Users: " + users.size());

    } catch (SQLException e) {
        System.err.println("Error loading dashboard data: " + e.getMessage());
        throw e;
    } finally {
        if (conn != null && !conn.isClosed()) {
            conn.close();
        }
    }
}

    private void addMovie(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        try {
            String title = request.getParameter("title");
            String description = request.getParameter("description");
            String genre = request.getParameter("genre");
            String duration = request.getParameter("duration");
            Date releaseDate = Date.valueOf(request.getParameter("releaseDate"));
            double price = Double.parseDouble(request.getParameter("price"));
            BigDecimal rating = new BigDecimal(request.getParameter("Rating"));
            String ImageUrl = request.getParameter("ImageUrl");
           
            Movie movie = new Movie();
            movie.setTitle(title);
            movie.setDescription(description);
            movie.setGenre(genre);
            movie.setDuration(duration);
            movie.setReleaseDate(releaseDate); 
            movie.setStatus("ACTIVE");
            movie.setPrice(price);
            movie.setImageUrl(ImageUrl);
            movie.setRating(rating);
            
          
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
    
    
    
//add theatres
    
    private void addTheater(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException, SQLException {
        String name = request.getParameter("theaterName");
        String location = request.getParameter("location");
        int totalSeats = Integer.parseInt(request.getParameter("totalSeats"));
        
        Theatre theatre = new Theatre();
        theatre.setName(name);
        theatre.setLocation(location);
        theatre.setTotal_seats(totalSeats);
        theatre.setStatus("ACTIVE");
        
        theatreDao.addTheatre(theatre);
        
        response.sendRedirect("AdminServlet?action=listTheaters");
    }
    
    //update theatre
    
    private void updateTheater(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
    try {
        int theaterId = Integer.parseInt(request.getParameter("theaterId"));
        String name = request.getParameter("name");
        String location = request.getParameter("location");
        int totalSeats = Integer.parseInt(request.getParameter("totalSeats"));
        String status = request.getParameter("status");

        Theatre theatre = new Theatre(theaterId, name, location, totalSeats);
        theatre.setStatus(status);

        TheatreDao theatreDao = new TheatreDao(DatabaseConnection.getConnection());
        boolean success = theatreDao.updateTheatre(theatre);

        if (success) {
        	
            // Instead of forwarding, redirect to the admin dashboard
            response.sendRedirect("AdminServlet?action=showDashboard&tab=theaters");
        } else {
            response.sendRedirect("AdminServlet?action=listTheaters&error=UpdateFailed");
        }
    } catch (Exception e) {
        response.sendRedirect("AdminServlet?action=listTheaters&error=" + URLEncoder.encode(e.getMessage(), "UTF-8"));
    }
}

    
    private void deleteTheater(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("theaterId"));
        boolean isDeleted = false; // Initialize isDeleted
        
        try {
            // Ensure you have an instance of TheatreDao
            TheatreDao theatreDao = new TheatreDao(connection);
            isDeleted = theatreDao.deleteTheatre(id); // Call instance method
        } catch (SQLException e) {
            e.printStackTrace(); // Log the exception
            request.getSession().setAttribute("error", "An error occurred while deleting the theater.");
        }
        
        if (isDeleted) {
            request.getSession().setAttribute("message", "Theater deleted successfully.");
        } else {
            request.getSession().setAttribute("error", "Failed to delete theater.");
        }
        
        // Redirect to the servlet with updated data or display the updated list
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

    @Override
    public void destroy() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            System.err.println("Error closing connection: " + e.getMessage());
        }
        // Clean up other resources
        userDao = null;
        movieDao = null;
        theatreDao = null;
        showtimeDao = null;
        bookingDao = null;
    }
}
