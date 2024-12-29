package com.controller;


import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import com.movie.dao.MovieDao;
import com.movie.model.Movie;
import com.google.gson.Gson;
import java.io.IOException;
import java.util.List;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;


@WebServlet(name = "MovieServlet", urlPatterns = {"/MovieServlet", "/movies/*"})
public class MovieServlet extends HttpServlet {
    private MovieDao movieDao;
    private Gson gson;

    public void init() {
        movieDao = new MovieDao();
        gson = new Gson();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws IOException {
        // Set headers before any error might occur
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "GET");
        response.setContentType("application/json;charset=UTF-8");

        try {
            String action = request.getParameter("action");
            List<Movie> movies;
            
            if ("search".equals(action)) {
                String query = request.getParameter("query");
                movies = movieDao.searchMovies(query != null ? query : "");
            } else {
                movies = movieDao.getAllMovies();
            }
            
            // Log the response for debugging
            String jsonMovies = gson.toJson(movies);
            System.out.println("Sending JSON response: " + jsonMovies);
            
            response.getWriter().write(jsonMovies);
            
        } catch (Exception e) {
            System.err.println("Error in MovieServlet: " + e.getMessage());
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "An error occurred while processing your request");
            errorResponse.put("details", e.getMessage());
            response.getWriter().write(gson.toJson(errorResponse));
        }
    }
}