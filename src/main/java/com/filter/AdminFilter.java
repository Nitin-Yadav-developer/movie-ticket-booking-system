package com.filter;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import com.user.model.User;
import com.util.Constants;

import java.io.IOException;

@WebFilter(urlPatterns = {"/admin/*", "/AdminServlet", "/admin_dashboard.jsp"})
public class AdminFilter implements Filter {
    
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Initialization code if needed
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        HttpSession session = httpRequest.getSession(false);

        boolean isLoggedIn = session != null && session.getAttribute("user") != null;
        
        if (isLoggedIn) {
            User user = (User) session.getAttribute("user");
            if (user != null && user.isAdmin()) {
                // User is admin, allow the request to continue
                chain.doFilter(request, response);
                return;
            }
            // User is logged in but not an admin
            session.setAttribute("error", Constants.ERROR_ACCESS_DENIED);
        } else {
            // User is not logged in
            session.setAttribute("error", "Please log in as an administrator to access this area.");
        }

        // If not admin or not logged in, redirect to login page
        httpResponse.sendRedirect(httpRequest.getContextPath() + "/login.jsp");
    }

    @Override
    public void destroy() {
        // Cleanup code if needed
    }
}
