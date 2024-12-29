package com.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Timestamp;

import com.booking.dao.BookingDao;
import com.booking.model.Booking;
import com.util.DatabaseConnection;

@WebServlet("/processPayment")
public class ProcessPaymentServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        try {
            HttpSession session = request.getSession();
            
            // Validate payment info
            if (!validatePaymentDetails(request.getParameter("cardNumber"),
                                     request.getParameter("expiryDate"),
                                     request.getParameter("cvv"))) {
                throw new Exception("Invalid payment details");
            }

            // Create and save booking
            Booking booking = new Booking();
            booking.setShowtimeId(Integer.parseInt(session.getAttribute("showtimeId").toString()));
            booking.setUserId((Integer) session.getAttribute("userId"));
            booking.setSeatsBooked(session.getAttribute("selectedSeats").toString());
            booking.setTheatreId(Integer.parseInt(session.getAttribute("theatreId").toString()));
            booking.setTotalSeats(Integer.parseInt(session.getAttribute("totalSeats").toString()));
            booking.setBookingDate(new Timestamp(System.currentTimeMillis()));
            
            // Save booking to database
            BookingDao bookingDao = new BookingDao(DatabaseConnection.getConnection());
            if (bookingDao.addBooking(booking)) {
                session.setAttribute("bookingId", booking.getBookingId());
                response.sendRedirect(request.getContextPath() + "/booking-success.jsp");
            } else {
                throw new Exception("Booking failed");
            }
            
        } catch (Exception e) {
            request.setAttribute("error", "Payment failed: " + e.getMessage());
            request.getRequestDispatcher("/payment.jsp").forward(request, response);
        }
    }

    private boolean validatePaymentDetails(String cardNumber, String expiryDate, String cvv) {
        return cardNumber != null && cardNumber.matches("\\d{16}") &&
               expiryDate != null && expiryDate.matches("(0[1-9]|1[0-2])/\\d{2}") &&
               cvv != null && cvv.matches("\\d{3}");
    }
}
