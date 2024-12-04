package com.controller;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import com.user.dao.UserDao;
import com.user.model.User;

@WebServlet("/Userservlet")
public class Userservlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private UserDao userDao;

    @Override
    public void init() throws ServletException {
        super.init();
        userDao = new UserDao();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String action = request.getParameter("action");
        
        if ("logout".equals(action)) {
            HttpSession session = request.getSession(false);
            if (session != null) {
                session.invalidate();
            }
            response.sendRedirect(request.getContextPath() + "/index.jsp");
        } else if (action == null) {
            response.sendRedirect("index.jsp");
        }
        // Add any GET request handling here
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String action = request.getParameter("action");
        if ("register".equals(action)) {
            registerUser(request, response);
        } else if ("login".equals(action)) {
            loginUser(request, response);
        } else if ("updateProfile".equals(action)) {
            updateUserProfile(request, response);
        }
    }

    private void registerUser(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        try {
            // Get form parameters
            String username = request.getParameter("username");
            String name = request.getParameter("name");
            String email = request.getParameter("email");
            String country = request.getParameter("country");
            String address = request.getParameter("address");
            String password = request.getParameter("password");

            // Debug print
            System.out.println("Received registration request:");
            System.out.println("Username: " + username);
            System.out.println("Email: " + email);

            // Validate required fields
            if (username == null || name == null || email == null || 
                country == null || address == null || password == null ||
                username.trim().isEmpty() || name.trim().isEmpty() || 
                email.trim().isEmpty() || country.trim().isEmpty() || 
                address.trim().isEmpty() || password.trim().isEmpty()) {
                request.setAttribute("error", "All fields are required");
                request.getRequestDispatcher("/register.jsp").forward(request, response);
                return;
            }

            // Create user object
            User user = new User(0, username, name, email, country, address, password);
            
            try {
                userDao.insertUser(user);
                response.sendRedirect("login.jsp?registered=true");
            } catch (Exception e) {
                request.setAttribute("error", "Registration failed: " + e.getMessage());
                request.getRequestDispatcher("/register.jsp").forward(request, response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Registration failed: " + e.getMessage());
            request.getRequestDispatcher("/register.jsp").forward(request, response);
        }
    }

    private void loginUser(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        try {
            String email = request.getParameter("email");
            String password = request.getParameter("password");

            // Debug logs
            System.out.println("Login attempt - Email: " + email);
            
            // Validate input
            if (email == null || password == null || 
                email.trim().isEmpty() || password.trim().isEmpty()) {
                request.setAttribute("error", "Email and password are required");
                request.getRequestDispatcher("/login.jsp").forward(request, response);
                return;
            }

            User user = userDao.selectUserByEmailAndPassword(email, password);
            if (user != null) {
                // Create session and set attributes
                HttpSession session = request.getSession(true);
                session.setAttribute("user", user);
                session.setAttribute("userId", user.getUserId());
                session.setAttribute("username", user.getUsername());
                
                // Debug log
                System.out.println("User logged in successfully: " + user.getUsername());
                
                // Redirect to home page
                response.sendRedirect("index.jsp");
            } else {
                // Debug log
                System.out.println("Login failed for email: " + email);
                
                request.setAttribute("error", "Invalid email or password");
                request.getRequestDispatcher("/login.jsp").forward(request, response);
            }
        } catch (Exception e) {
            System.out.println("Login error: " + e.getMessage());
            e.printStackTrace();
            request.setAttribute("error", "Login failed: " + e.getMessage());
            request.getRequestDispatcher("/login.jsp").forward(request, response);
        }
    }

    private void updateUserProfile(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute("user");
        
        if (currentUser == null) {
            response.sendRedirect("login.jsp");
            return;
        }
        

        try {
            // Update user object with new values
            currentUser.setName(request.getParameter("name"));
            currentUser.setEmail(request.getParameter("email"));
            currentUser.setCountry(request.getParameter("country"));
            currentUser.setAddress(request.getParameter("address"));
            
            // Update in database
            userDao.updateUser(currentUser);
            
            // Update session
            session.setAttribute("user", currentUser);
            
            // Redirect with success message
            response.sendRedirect("userProfile.jsp?updated=true");
            
        } catch (Exception e) {
            request.setAttribute("error", "Update failed: " + e.getMessage());
            request.getRequestDispatcher("/userProfile.jsp").forward(request, response);
        }
    }
}
