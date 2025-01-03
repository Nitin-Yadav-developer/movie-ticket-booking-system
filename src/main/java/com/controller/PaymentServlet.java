package com.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Timestamp;

@WebServlet("/payment")
public class PaymentServlet extends HttpServlet {
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        try {
            HttpSession session = request.getSession();
            if (session.getAttribute("user") == null) {
                response.sendRedirect(request.getContextPath() + "/login.jsp");
                return;
            }

            // Get parameters and verify
            System.out.println("Session Attributes Before Processing:");
            System.out.println("Show Date: " + session.getAttribute("bookingShowDate"));
            System.out.println("Show Time: " + session.getAttribute("show_time"));
            System.out.println("Movie Title: " + session.getAttribute("bookingMovieTitle"));
            System.out.println("Theatre Name: " + session.getAttribute("bookingTheatreName"));

            // Process form data
            String selectedSeats = request.getParameter("selectedSeats");
            int totalSeats = Integer.parseInt(request.getParameter("totalSeats"));
            double totalAmount = Double.parseDouble(request.getParameter("totalAmount"));

            // Store payment details in session
            session.setAttribute("paymentSelectedSeats", selectedSeats);
            session.setAttribute("paymentTotalSeats", totalSeats);
            session.setAttribute("paymentTotalAmount", totalAmount);

            // Verify final session state
            System.out.println("Final Session State:");
            System.out.println("Selected Seats: " + selectedSeats);
            System.out.println("Total Amount: " + totalAmount);
            System.out.println("Show Date: " + session.getAttribute("bookingShowDate"));
            System.out.println("Show Time: " + session.getAttribute("show_time"));

            request.getRequestDispatcher("/payment.jsp").forward(request, response);
            
        } catch (Exception e) {
            System.err.println("Error in PaymentServlet: " + e.getMessage());
            e.printStackTrace();
            request.setAttribute("error", "Error processing payment: " + e.getMessage());
            request.getRequestDispatcher("/error.jsp").forward(request, response);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        // Redirect GET requests back to booking
        response.sendRedirect(request.getContextPath() + "/booking.jsp");
    }
}
