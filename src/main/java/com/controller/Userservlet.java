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
            boolean isAdminLogin = "on".equals(request.getParameter("adminLogin"));

            User user = userDao.selectUserByEmailAndPassword(email, password);
            
            if (user != null) {
                if (isAdminLogin && !user.isAdmin()) {
                    request.setAttribute("error", "You don't have admin privileges");
                    request.getRequestDispatcher("/login.jsp").forward(request, response);
                    return;
                }

                HttpSession session = request.getSession(true);
                session.setAttribute("user", user);
                session.setAttribute("userId", user.getUser_id());
                session.setAttribute("username", user.getUsername());
                session.setAttribute("isAdmin", user.isAdmin());

                if (user.isAdmin()) {
                    response.sendRedirect("AdminServlet?action=showDashboard");
                } else {
                    response.sendRedirect("index.jsp");
                }
            } else {
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
        try {
            int user_id = Integer.parseInt(request.getParameter("user_id"));
            String name = request.getParameter("name");
            String email = request.getParameter("email");
            String country = request.getParameter("country");
            String address = request.getParameter("address");
            
            User currentUser = userDao.selectuser(user_id);
            if (currentUser != null) {
                currentUser.setName(name);
                currentUser.setEmail(email);
                currentUser.setCountry(country);
                currentUser.setAddress(address);
                
                if (userDao.updateUser(currentUser)) {
                    request.getSession().setAttribute("user", currentUser);
                    response.sendRedirect("userProfile.jsp?updated=true");
                    return;
                }
            }
            throw new ServletException("User update failed");
        } catch (Exception e) {
            request.setAttribute("error", "Error updating profile: " + e.getMessage());
            request.getRequestDispatcher("/userProfile.jsp").forward(request, response);
        }
    }
}
