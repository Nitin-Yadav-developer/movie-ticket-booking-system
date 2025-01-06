<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page import="com.movie.dao.MovieDao"%>
<%@ page import="com.movie.model.Movie"%>
<%@ page import="java.util.List"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>  <!-- Add this line -->
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Movies - Movie Ticket Booking</title>
    
    
    
    
    <!-- CSS Files -->
    <link href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css" rel="stylesheet">
    <link href="css/styles.css" rel="stylesheet">
</head>
<body>

    <!-- Include Navigation -->
    <jsp:include page="navbar.jsp" />

    <!-- Movies Header Section -->
    <section class="movies-header py-5">
        <div class="container">
            <div class="row align-items-center">
                <div class="col-md-6">
                    <h1>Movies</h1>
                </div>
                <div class="col-md-6">
                    <form action="movies.jsp" method="GET" class="search-box">
                        <input type="text" name="search" class="form-control" placeholder="Search movies..." 
                               value="${param.search}">
                        <button type="submit" class="btn btn-primary">
                            <i class="fas fa-search"></i> Search
                        </button>
                    </form>
                </div>
            </div>
        </div>
    </section>

    <!-- Movies Filter Section -->
    <section class="filters-section py-3">
        <div class="container">
            <div class="genre-filters text-center">
                <button class="btn btn-outline-primary mx-1 genre-filter active" onclick="filterMovies('all')">All</button>
                <button class="btn btn-outline-primary mx-1 genre-filter" onclick="filterMovies('action')">Action</button>
                <button class="btn btn-outline-primary mx-1 genre-filter" onclick="filterMovies('comedy')">Comedy</button>
                <button class="btn btn-outline-primary mx-1 genre-filter" onclick="filterMovies('drama')">Drama</button>
                <button class="btn btn-outline-primary mx-1 genre-filter" onclick="filterMovies('thriller')">Thriller</button>
            </div>
        </div>
    </section>

    <!-- Movies Grid Section -->
    <section class="movies-grid py-5">
        <div class="container">
            <div class="row" id="moviesContainer">
                <%
                MovieDao movieDao = new MovieDao();
                List<Movie> movies = movieDao.getAllMovies();
                
                if (movies.isEmpty()) {
                %>
                    <div class="col-12">
                        <div class="alert alert-info">No movies available</div>
                    </div>
                <%
                } else {
                    for (Movie movie : movies) {
                %>
                    <div class="col-md-3 col-sm-6 mb-4 movie-item" data-genre="<%= movie.getGenre().toLowerCase() %>">
                        <div class="movie-card">
                            <div class="image-container">
                                <img src="<%= movie.getImageUrl() != null ? movie.getImageUrl() : "images/default-movie.jpg" %>" 
                                     alt="<%= movie.getTitle() %>" 
                                     class="card-img-top"
                                     onerror="this.src='images/default-movie.jpg'">
                            </div>
                            <div class="movie-info">
                                <h5 class="card-title"><%= movie.getTitle() %></h5>
                                <p class="movie-description"><%= movie.getDescription() %></p>
                                <div class="movie-rating mb-2">
                                    <%= generateStarRating(movie.getRatingAsDouble()) %>
                                </div>
                                <p class="genre-badge">
                                    <span class="badge badge-secondary"><%= movie.getGenre() %></span>
                                </p>
                                <p class="movie-duration"><i class="fas fa-clock"></i> <%= movie.getDuration() %></p>
                                <p class="movie-status"><%= movie.getStatus() %></p>
                                <p class="movie-release">
                                    Released: 
                                    <% if (movie.getReleaseDate() != null) { %>
                                        <fmt:formatDate value="<%= movie.getReleaseDate() %>" pattern="dd MMM yyyy"/>
                                    <% } else { %>
                                        Not Available
                                    <% } %>
                                </p>
                                <p class="movie-price">₹<%= String.format("%.2f", movie.getPrice()) %></p>
                                <a href="${pageContext.request.contextPath}/showtime/list?movieId=<%= movie.getMovieId() %>" 
                                   class="btn btn-primary btn-block">View Showtimes</a>
                            </div>
                        </div>
                    </div>
                <%
                    }
                }
                %>
            </div>
        </div>
    </section>

    <!-- Add this helper function at the bottom of your JSP -->
    <%!
    private String generateStarRating(double rating) {
        int stars = (int) Math.round(rating)/2;  // Remove the division by 2
        StringBuilder html = new StringBuilder("<span class='stars'>");
        for (int i = 1; i <= 5; i++) {
            html.append(i <= stars ? "★" : "☆");
        }
        html.append("</span><span class='rating-value'>")
            .append(String.format("%.1f", rating))
            .append("/10</span>");
        return html.toString();
    }
    %>

    <!-- Keep only the filtering JavaScript -->
    <script>
        // ...existing filterMovies function...
        function filterMovies(genre) {
            // Update active button state
            document.querySelectorAll('.genre-filter').forEach(button => {
                button.classList.remove('active');
                if(button.textContent.toLowerCase() === genre.toLowerCase()) {
                    button.classList.add('active');
                }
            });

            // Get all movie cards
            const movieCards = document.querySelectorAll('.movie-card');
            
            movieCards.forEach(card => {
                const cardGenre = card.querySelector('.badge-secondary').textContent.trim().toLowerCase();
                const parentCol = card.closest('.col-md-3');
                
                if (genre.toLowerCase() === 'all') {
                    parentCol.style.display = '';
                } else {
                    parentCol.style.display = cardGenre === genre.toLowerCase() ? '' : 'none';
                }
            });

            // Show "No movies found" message if no movies are visible
            const visibleMovies = document.querySelectorAll('.col-md-3[style=""]').length;
            const noMoviesMessage = document.querySelector('.alert-info');
            
            if (noMoviesMessage) {
                if (visibleMovies === 0) {
                    noMoviesMessage.style.display = 'block';
                    noMoviesMessage.textContent = `No ${genre} movies found.`;
                } else {
                    noMoviesMessage.style.display = 'none';
                }
            }
        }
    </script>
</body>
</html>