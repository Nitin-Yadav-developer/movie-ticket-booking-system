package com.booking.servlet;
import com.booking.model.Booking;
import com.util.DatabaseConnection;

import jakarta.servlet.ServletException;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Timestamp;
import java.sql.SQLException;
import com.booking.dao.BookingDao;


@WebServlet("/BookingServlet")
public class BookingServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false);  // Start transaction

            // Get parameters and validate
            int showtimeId = Integer.parseInt(request.getParameter("showtime_id"));
            int userId = Integer.parseInt(request.getParameter("user_id"));
            String seatsBooked = request.getParameter("selected_seats"); // JSON string
            int theatreId = Integer.parseInt(request.getParameter("theatre_id"));
            int totalSeats = Integer.parseInt(request.getParameter("total_seats"));
            double totalAmount = Double.parseDouble(request.getParameter("total_amount"));
            
            // Create booking object with all required fields
            Booking booking = new Booking();
            booking.setShowtimeId(showtimeId);
            booking.setUserId(userId);
            booking.setSeatsBooked(seatsBooked);
            booking.setTheatreId(theatreId);
            booking.setTotalSeats(totalSeats);
            booking.setBookingDate(new Timestamp(System.currentTimeMillis()));
            booking.setTotalAmount(totalAmount);
            
            // Save booking
            BookingDao bookingDao = new BookingDao(conn);
            
            if (bookingDao.addBooking(booking)) {
                conn.commit();
                HttpSession session = request.getSession();
                session.setAttribute("lastBookingId", booking.getBookingId());
                response.sendRedirect("MyBookingsServlet");
            } else {
                throw new ServletException("Booking failed");
            }
            
        } catch (NumberFormatException e) {
            if (conn != null) {
                try {
                    conn.rollback();  // Rollback on error
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            request.setAttribute("error", "Invalid input parameters");
            request.getRequestDispatcher("booking.jsp").forward(request, response);
        } catch (Exception e) {
            if (conn != null) {
                try {
                    conn.rollback();  // Rollback on error
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            request.setAttribute("error", "Error processing booking: " + e.getMessage());
            request.getRequestDispatcher("error.jsp").forward(request, response);
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        // Handle get request - maybe show booking form
        HttpSession session = request.getSession();
        if (session.getAttribute("user") == null) {
            response.sendRedirect("login.jsp");
            return;
        }
        
        String showtimeId = request.getParameter("showtime_id");
        if (showtimeId == null || showtimeId.trim().isEmpty()) {
            response.sendRedirect("movies.jsp");
            return;
        }
        
        request.getRequestDispatcher("booking.jsp").forward(request, response);
    }
}
