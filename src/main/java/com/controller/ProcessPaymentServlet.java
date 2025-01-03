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
        HttpSession session = request.getSession();
        try {
            // Validate user is logged in
            if (session.getAttribute("user") == null) {
                response.sendRedirect(request.getContextPath() + "/login.jsp");
                return;
            }

            // Validate payment details
            String cardNumber = request.getParameter("cardNumber");
            String expiryDate = request.getParameter("expiryDate");
            String cvv = request.getParameter("cvv");

            if (!validatePaymentDetails(cardNumber, expiryDate, cvv)) {
                throw new Exception("Invalid payment details");
            }

            // Get and validate seats JSON
            String selectedSeats = request.getParameter("selectedSeats");
            if (selectedSeats == null || selectedSeats.trim().isEmpty()) {
                throw new Exception("No seats selected");
            }

            // Ensure it's valid JSON
            if (!selectedSeats.startsWith("[") || !selectedSeats.endsWith("]")) {
                selectedSeats = "[" + selectedSeats + "]";
            }

            // Create booking
            Booking booking = new Booking();
            booking.setShowtimeId(Integer.parseInt(request.getParameter("showtimeId")));
            booking.setUserId(Integer.parseInt(request.getParameter("userId")));
            booking.setSeatsBooked(selectedSeats); // Now properly formatted JSON
            booking.setTheatreId(Integer.parseInt(request.getParameter("theatreId")));
            booking.setTotalSeats(Integer.parseInt(request.getParameter("totalSeats")));
            booking.setTotalAmount(Double.parseDouble(request.getParameter("totalAmount")));
            booking.setBookingDate(new Timestamp(System.currentTimeMillis()));
            booking.setBooking_status("CONFIRMED");

            // Debug log
            System.out.println("Processing booking with seats: " + selectedSeats);

            // Save booking using BookingDao
            BookingDao bookingDao = new BookingDao(DatabaseConnection.getConnection());
            if (bookingDao.addBooking(booking)) {
                // Get the generated booking ID
                int generatedBookingId = bookingDao.getLastGeneratedBookingId(); // Add this method to BookingDao
                
                // Store booking details in session
                session.setAttribute("bookingId", generatedBookingId);
                session.setAttribute("bookingMovieTitle", request.getParameter("movieTitle"));
                session.setAttribute("bookingTheatreName", request.getParameter("theatreName"));
                session.setAttribute("bookingShowDate", request.getParameter("showDate"));
                session.setAttribute("paymentSelectedSeats", selectedSeats);
                session.setAttribute("paymentTotalSeats", request.getParameter("totalSeats"));
                session.setAttribute("paymentTotalAmount", request.getParameter("totalAmount"));
                session.setAttribute("theatreLocation", request.getParameter("theatreLocation"));
                
                response.sendRedirect(request.getContextPath() + "/booking-success.jsp");
            } else {
                throw new Exception("Failed to save booking");
            }

        } catch (Exception e) {
            System.err.println("Payment processing error: " + e.getMessage());
            e.printStackTrace();
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
