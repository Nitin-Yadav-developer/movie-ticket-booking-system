package com.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.text.SimpleDateFormat;

import com.booking.dao.BookingDao;
import com.booking.model.Booking;
import com.util.DatabaseConnection;

@WebServlet("/GenerateTicket")
public class GenerateTicketServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        try {
            HttpSession session = request.getSession();
            String bookingId = request.getParameter("bookingId");

            if (bookingId == null) {
                throw new ServletException("Missing booking ID");
            }

            // Get booking details from database
            BookingDao bookingDao = new BookingDao(DatabaseConnection.getConnection());
            Booking booking = bookingDao.getBooking(Integer.parseInt(bookingId));
            
            if (booking != null) {
                // Set all required attributes in session
                session.setAttribute("bookingId", booking.getBookingId());
                session.setAttribute("movieTitle", booking.getMovieTitle());
                session.setAttribute("theatreName", booking.getTheatreName());
                session.setAttribute("theatreLocation", booking.getTheatreLocation()); // Add this line
                session.setAttribute("bookingDate", booking.getBookingDate());
                session.setAttribute("selectedSeats", booking.getSeatsBooked());
                session.setAttribute("totalAmount", booking.getTotalAmount());
                session.setAttribute("totalSeats", booking.getTotalSeats());
                session.setAttribute("showDate", booking.getBookingDate());
                session.setAttribute("showtime", new SimpleDateFormat("hh:mm a").format(booking.getBookingDate()));

                request.setAttribute("booking", booking);
                request.getRequestDispatcher("/ticket.jsp").forward(request, response);
            } else {
                throw new Exception("Booking not found");
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Error generating ticket: " + e.getMessage());
            request.getRequestDispatcher("/error.jsp").forward(request, response);
        }
    }
}
