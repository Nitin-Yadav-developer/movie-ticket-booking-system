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


@WebServlet(name = "MovieServlet", urlPatterns = {"/MovieServlet", "/MovieServlet/*"})
public class MovieServlet extends HttpServlet {
    private MovieDao movieDao;
    private Gson gson;

    public void init() {
        movieDao = new MovieDao();
        gson = new Gson();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws IOException {
        try {
            String action = request.getParameter("action");
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            
            List<Movie> movies;
            if ("search".equals(action)) {
                String query = request.getParameter("query");
                movies = movieDao.searchMovies(query);
            } else {
                movies = movieDao.getAllMovies();
            }
            
            System.out.println("Movies found: " + movies.size()); // Add logging
            String jsonMovies = gson.toJson(movies);
            System.out.println("JSON response: " + jsonMovies); // Add logging
            response.getWriter().write(jsonMovies);
            
        } catch (Exception e) {
            System.err.println("Error in MovieServlet: " + e.getMessage());
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"error\": \"" + e.getMessage() + "\"}");
        }
    }
}