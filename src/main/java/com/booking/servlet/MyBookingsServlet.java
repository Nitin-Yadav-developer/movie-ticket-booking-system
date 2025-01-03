package com.booking.servlet;


import com.booking.dao.BookingDao;
import com.booking.model.Booking;
import com.util.DatabaseConnection;


import java.io.IOException;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;


@WebServlet("/MyBookingsServlet")
public class MyBookingsServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        if (session.getAttribute("user") == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        try (Connection conn = DatabaseConnection.getConnection()) {
            BookingDao bookingDao = new BookingDao(conn);
            
            // Get userId from session
            com.user.model.User user = (com.user.model.User) session.getAttribute("user");
            int userId = user.getUser_id();
            
            // Get all bookings for the user including movie details
            List<Booking> bookings = bookingDao.getBookingsByUserId(userId);
            request.setAttribute("bookings", bookings);
            
            // Forward to the bookings page
            request.getRequestDispatcher("/mybookings.jsp").forward(request, response);
            
        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("error", "Error retrieving bookings: " + e.getMessage());
            request.getRequestDispatcher("/error.jsp").forward(request, response);
        }
    }
}
