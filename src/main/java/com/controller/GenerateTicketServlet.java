package com.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

import com.booking.dao.BookingDao;
import com.booking.model.Booking;
import com.util.DatabaseConnection;

@WebServlet("/GenerateTicket")
public class GenerateTicketServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        try {
            HttpSession session = request.getSession();
            Integer bookingId = (Integer) session.getAttribute("bookingId");
            
            if (bookingId == null) {
                throw new Exception("No booking ID found");
            }

            // Get booking details from database
            BookingDao bookingDao = new BookingDao(DatabaseConnection.getConnection());
            Booking booking = bookingDao.getBooking(bookingId);
            
            if (booking != null) {
                request.setAttribute("booking", booking);
                request.getRequestDispatcher("/ticket.jsp").forward(request, response);
            } else {
                throw new Exception("Booking not found");
            }
        } catch (Exception e) {
            response.sendRedirect("error.jsp");
        }
    }
}
