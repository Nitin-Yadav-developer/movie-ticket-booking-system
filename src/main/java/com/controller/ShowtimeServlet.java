package com.controller;

import com.showtime.dao.ShowtimeDao;
import com.showtime.model.Showtime;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet(urlPatterns = {"/showtime", "/showtime/*"})
public class ShowtimeServlet extends HttpServlet {
    private ShowtimeDao showtimeDao;

    @Override
    public void init() throws ServletException {
        // Initialize DAO without passing connection
        showtimeDao = new ShowtimeDao();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        try {
            String pathInfo = request.getPathInfo();
            if (pathInfo == null || pathInfo.equals("/")) {
                listShowtimes(request, response);
                return;
            }

            switch (pathInfo) {
                case "/list":
                    listShowtimes(request, response);
                    break;
                case "/view":
                    viewShowtime(request, response);
                    break;
                case "/movie":
                    listMovieShowtimes(request, response);
                    break;
                default:
                    response.sendError(HttpServletResponse.SC_NOT_FOUND);
                    break;
            }
        } catch (SQLException ex) {
            request.setAttribute("error", "Database error: " + ex.getMessage());
            request.getRequestDispatcher("/error.jsp").forward(request, response);
        } catch (Exception ex) {
            request.setAttribute("error", "Server error: " + ex.getMessage());
            request.getRequestDispatcher("/error.jsp").forward(request, response);
        }
    }

    private void listShowtimes(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        try {
            List<Showtime> showtimes;
            String movieId = request.getParameter("movieId");
            
            if (movieId != null && !movieId.trim().isEmpty()) {
                // If movieId is provided, get showtimes for that movie
                showtimes = showtimeDao.getShowtimesByMovie(Integer.parseInt(movieId));
            } else {
                // Otherwise get all showtimes
                showtimes = showtimeDao.getAllShowtimes();
            }
            
            System.out.println("Number of showtimes retrieved: " + (showtimes != null ? showtimes.size() : 0));
            
            if (showtimes != null && !showtimes.isEmpty()) {
                System.out.println("First showtime: " + showtimes.get(0).toString());
            }
            
            String sortBy = request.getParameter("sort");
            
            if (showtimes != null && !showtimes.isEmpty() && sortBy != null) {
                switch (sortBy) {
                    case "movie":
                        showtimes.sort((s1, s2) -> s1.getMovieTitle().compareTo(s2.getMovieTitle()));
                        break;
                    case "theatre":
                        showtimes.sort((s1, s2) -> s1.getTheatreName().compareTo(s2.getTheatreName()));
                        break;
                    case "date":
                        // Default sorting by date is already handled by SQL query
                        break;
                }
            }

            request.setAttribute("showtimes", showtimes);
            if (showtimes == null || showtimes.isEmpty()) {
                request.setAttribute("message", "No upcoming showtimes available.");
            }
        } catch (SQLException e) {
            System.err.println("Database error in listShowtimes: " + e.getMessage());
            e.printStackTrace();
            request.setAttribute("error", "Database error: Please ensure all required tables are properly set up.");
        } catch (NumberFormatException e) {
            System.err.println("Invalid movie ID: " + e.getMessage());
            request.setAttribute("error", "Invalid movie ID provided");
        }
        request.getRequestDispatcher("/showtime.jsp").forward(request, response);
    }

    private void viewShowtime(HttpServletRequest request, HttpServletResponse response) 
            throws SQLException, ServletException, IOException {
        try {
            int id = Integer.parseInt(request.getParameter("id"));
            Showtime showtime = showtimeDao.getShowtime(id);
            if (showtime == null) {
                request.setAttribute("error", "Showtime not found");
                request.getRequestDispatcher("/showtime.jsp").forward(request, response);
                return;
            }
            request.setAttribute("showtime", showtime);
            request.getRequestDispatcher("/showtime-detail.jsp").forward(request, response);
        } catch (NumberFormatException e) {
            request.setAttribute("error", "Invalid showtime ID");
            request.getRequestDispatcher("/showtime.jsp").forward(request, response);
        }
    }

    private void listMovieShowtimes(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException, SQLException {
        String movieId = request.getParameter("id");
        if (movieId != null && !movieId.trim().isEmpty()) {
            try {
                List<Showtime> showtimes = showtimeDao.getShowtimesByMovie(Integer.parseInt(movieId));
                if (showtimes.isEmpty()) {
                    request.setAttribute("message", "No upcoming showtimes available for this movie.");
                }
                request.setAttribute("showtimes", showtimes);
                request.getRequestDispatcher("/showtime.jsp").forward(request, response);
            } catch (NumberFormatException e) {
                request.setAttribute("error", "Invalid movie ID");
                request.getRequestDispatcher("/movies.jsp").forward(request, response);
            }
        } else {
            response.sendRedirect(request.getContextPath() + "/movies.jsp");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String action = request.getPathInfo();
        
        try {
            switch (action) {
                case "/add":
                    addShowtime(request, response);
                    break;
                case "/update":
                    updateShowtime(request, response);
                    break;
                case "/delete":
                    deleteShowtime(request, response);
                    break;
                default:
                    response.sendRedirect(request.getContextPath() + "/showtime/list");
                    break;
            }
        } catch (SQLException ex) {
            throw new ServletException(ex);
        }
    }

    private void addShowtime(HttpServletRequest request, HttpServletResponse response) 
            throws SQLException, IOException {
        // Add implementation
        response.sendRedirect(request.getContextPath() + "/showtime/list");
    }

    private void updateShowtime(HttpServletRequest request, HttpServletResponse response) 
            throws SQLException, IOException {
        // Update implementation
        response.sendRedirect(request.getContextPath() + "/showtime/list");
    }

    private void deleteShowtime(HttpServletRequest request, HttpServletResponse response) 
            throws SQLException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        showtimeDao.deleteShowtime(id);
        response.sendRedirect(request.getContextPath() + "/showtime/list");
    }

    @Override
    public void destroy() {
        // Clean up resources if needed
        showtimeDao = null;
    }
}

