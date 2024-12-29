package com.booking.servlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import com.booking.dao.BookingDao;
import com.booking.model.Booking;
import com.util.DatabaseConnection;

@WebServlet("/MyBookingsServlet")
public class MyBookingsServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        Object userObj = session.getAttribute("user");
        
        if (userObj == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            BookingDao bookingDao = new BookingDao(conn);
            
            // Get userId from the User object
            com.user.model.User user = (com.user.model.User) userObj;
            int userId = user.getUserId(); // Assuming you have getUserId() method in User class
            
            List<Booking> bookings = bookingDao.getBookingsByUserId(userId);
            request.setAttribute("bookings", bookings);
            request.setAttribute("now", new java.util.Date());
            
            request.getRequestDispatcher("/mybookings.jsp").forward(request, response);
            
        } catch (Exception e) {
            e.printStackTrace();
            // Set error message and forward to error page
            request.setAttribute("errorMessage", "Error retrieving bookings: " + e.getMessage());
            request.getRequestDispatcher("/error.jsp").forward(request, response);
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
}
