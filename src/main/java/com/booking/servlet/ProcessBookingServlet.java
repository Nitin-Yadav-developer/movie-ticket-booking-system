package com.booking.servlet;

import java.io.IOException;
import java.text.SimpleDateFormat;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;



import com.showtime.model.Showtime;

@WebServlet("/ProcessBooking")
public class ProcessBookingServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        if (session.getAttribute("userId") == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        try {
            // Create a Showtime object to pass to booking.jsp
            Showtime showtime = new Showtime();
            showtime.setShowtime_id(Integer.parseInt(request.getParameter("showtimeId")));
            showtime.setMovieTitle(request.getParameter("movieTitle"));
            showtime.setTheatreName(request.getParameter("theatreName"));
            showtime.setTheatre_id(Integer.parseInt(request.getParameter("theatreId")));
            showtime.setTicketPrice(Double.parseDouble(request.getParameter("ticketPrice")));
            
            // Convert string dates to java.util.Date
            String showDateStr = request.getParameter("showDate");
            String showtimeStr = request.getParameter("showtime");
            
            if (showDateStr != null && !showDateStr.isEmpty()) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                showtime.setShowDate(dateFormat.parse(showDateStr));
            }
            
            if (showtimeStr != null && !showtimeStr.isEmpty()) {
                SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
                showtime.setShowtime(timeFormat.parse(showtimeStr));
            }
            
            // Store showtime data in request attribute
            request.setAttribute("showtime", showtime);
            
            // Forward to booking.jsp
            request.getRequestDispatcher("booking.jsp").forward(request, response);
            
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error processing dates: " + e.getMessage());
            response.sendRedirect("error.jsp");
        }
    }
}
