package com.booking.servlet;
//showtime to booking.jsp bridge
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.sql.Date;
import java.sql.Time;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/ProcessBooking")
public class ProcessBookingServlet extends HttpServlet {
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        try {
            // Get session and check if user is logged in
            HttpSession session = request.getSession();
            if (session.getAttribute("user") == null) {
                response.sendRedirect(request.getContextPath() + "/login.jsp");
                return;
            }

            // Get parameters
            int showtimeId = Integer.parseInt(request.getParameter("showtimeId"));
            String movieTitle = request.getParameter("movieTitle");
            String theatreName = request.getParameter("theatreName");
            double ticketPrice = Double.parseDouble(request.getParameter("ticketPrice"));
            int theatreId = Integer.parseInt(request.getParameter("theatreId"));

            // Get and parse show date
            String showDateStr = request.getParameter("showDate");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            java.util.Date parsedDate = sdf.parse(showDateStr);
            Date showDate = new Date(parsedDate.getTime());

            // Get and parse show time
            String showTimeStr = request.getParameter("show_time");
            if (showTimeStr != null && !showTimeStr.isEmpty()) {
                SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
                java.util.Date parsedTime = timeFormat.parse(showTimeStr);
                Time showTime = new Time(parsedTime.getTime());
                session.setAttribute("show_time", showTime);
                System.out.println("Show time set in session: " + showTime);
            }

            // Store booking details in session
            session.setAttribute("bookingShowtimeId", showtimeId);
            session.setAttribute("bookingMovieTitle", movieTitle);
            session.setAttribute("bookingTheatreName", theatreName);
            session.setAttribute("bookingTicketPrice", ticketPrice);
            session.setAttribute("bookingShowDate", showDate);
            session.setAttribute("bookingTheatreId", theatreId);

            // Debug log
            System.out.println("Session attributes set:");
            System.out.println("Movie Title: " + session.getAttribute("bookingMovieTitle"));
            System.out.println("Show Date: " + session.getAttribute("bookingShowDate"));
            System.out.println("Show Time: " + session.getAttribute("show_time"));

            // Redirect to booking page
            response.sendRedirect(request.getContextPath() + "/booking.jsp");

        } catch (ParseException e) {
            request.setAttribute("error", "Error processing dates: " + e.getMessage());
            request.getRequestDispatcher("/error.jsp").forward(request, response);
        } catch (Exception e) {
            System.err.println("Error in ProcessBookingServlet: " + e.getMessage());
            e.printStackTrace();
            request.setAttribute("error", "Error processing booking: " + e.getMessage());
            request.getRequestDispatcher("/error.jsp").forward(request, response);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        // Redirect GET requests to showtime list
        response.sendRedirect(request.getContextPath() + "/showtime/list");
    }
}
