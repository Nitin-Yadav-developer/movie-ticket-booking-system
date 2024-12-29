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
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        if (session.getAttribute("user") == null) {
            response.sendRedirect("login.jsp");
            return;
        }
        request.getRequestDispatcher("/payment.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        try {
            HttpSession session = request.getSession();
            if (session.getAttribute("user") == null) {
                response.sendRedirect("login.jsp");
                return;
            }
            
            // Store booking details in session
            String[] params = {
                "showtimeId", "theatreId", "movieTitle", "theatreName", 
                "showDate", "showtime", "selectedSeats", "totalSeats", 
                "ticketPrice", "totalAmount"
            };
            
            for (String param : params) {
                String value = request.getParameter(param);
                if (value == null || value.trim().isEmpty()) {
                    throw new ServletException("Missing required parameter: " + param);
                }
                session.setAttribute(param, value);
            }
            
            // Forward to payment page
            request.getRequestDispatcher("/payment.jsp").forward(request, response);
            
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "An error occurred: " + e.getMessage());
            request.getRequestDispatcher("/booking.jsp").forward(request, response);
        }
    }
}
