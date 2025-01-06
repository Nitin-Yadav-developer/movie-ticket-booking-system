<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page import="com.movie.dao.MovieDao"%>
<%@ page import="com.movie.model.Movie"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.stream.Collectors"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Movie Ticket Booking System</title>
    
    <!-- CSS Files -->
    <link href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css" rel="stylesheet">
    <link href="css/styles.css" rel="stylesheet">
    <link rel="shortcut icon" href="${pageContext.request.contextPath}/images/favicon.ico" type="image/x-icon">
</head>


<body>
    <!-- Navigation -->
    <nav class="navbar navbar-expand-lg navbar-light bg-light fixed-top">
        <div class="container">
            <a class="navbar-brand" href="#">Movie Ticket Booking</a>
            <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarNav">
                <span class="navbar-toggler-icon"></span>
            </button>
            <div class="collapse navbar-collapse" id="navbarNav">
                <ul class="navbar-nav ml-auto">
                    <li class="nav-item active">
                        <a class="nav-link" href="index.jsp">Home</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="movies.jsp">Movies</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="${pageContext.request.contextPath}/showtime/list">Showtimes</a>
                    </li>
                    <c:choose>
                        <c:when test="${sessionScope.user != null}">
                            <li class="nav-item">
                                <a class="nav-link" href="userProfile.jsp">Profile</a>
                            </li>
                            <li class="nav-item">
                                <a class="nav-link" href="Userservlet?action=logout">Logout</a>
                            </li>
                        </c:when>
                        
                        
                        
                        <c:otherwise>
                            <li class="nav-item">
                                <a class="nav-link" href="login.jsp">Login</a>
                            </li>
                            <li class="nav-item">
                                <a class="nav-link" href="register.jsp">Register</a>
                            </li>
                        </c:otherwise>
                    </c:choose>
                </ul>
            </div>
        </div>
    </nav>
   
   

    <!-- Hero Section -->
    <section class="hero-section">
        <div class="container">
            <div class="row align-items-center">
                <div class="col-md-6">
                    <h1>Book Your Movie Tickets</h1>
                    <div class="search-box">
                        <input type="text"  class="form-control" placeholder="Search movies...">
                        <button class="btn btn-primary">Search</button>
                    </div>
                </div>
            </div>
        </div>
    </section>
    
    
    
   
    <!-- Movies Section -->
    <section class="movies-section py-5">
        <div class="container">
            <h2 class="section-title mb-4">Now Showing</h2>
            <!-- Genre Filter Buttons -->
            <div class="genre-filters mb-4 text-center">
                <button class="btn btn-outline-primary mx-1 genre-filter active" onclick="filterMovies('all')">All</button>
                <button class="btn btn-outline-primary mx-1 genre-filter" onclick="filterMovies('action')">Action</button>
                <button class="btn btn-outline-primary mx-1 genre-filter" onclick="filterMovies('comedy')">Comedy</button>
                <button class="btn btn-outline-primary mx-1 genre-filter" onclick="filterMovies('drama')">Drama</button>
            </div>
    
            <!-- Movies Grid -->
            <div class="row" id="moviesContainer">
                <%
                try {
                    MovieDao movieDao = new MovieDao();
                    List<Movie> recentMovies = movieDao.getRecentMovies(10); // Get last 10 movies
                    
                    if (recentMovies.isEmpty()) {
                %>
                        <div class="col-12">
                            <div class="alert alert-info">No movies available</div>
                        </div>
                <%
                    } else {
                        for (Movie movie : recentMovies) {
                            if (movie != null && movie.getTitle() != null) {
                %>
                            <div class="col-md-3 col-sm-6 mb-4 movie-item" data-genre="<%= movie.getGenre() != null ? movie.getGenre().toLowerCase() : "" %>">
                                <div class="movie-card">
                                    <div class="image-container">
                                        <img src="<%= movie.getImageUrl() != null ? movie.getImageUrl() : "images/default-movie.jpg" %>" 
                                             alt="<%= movie.getTitle() %>" 
                                            class="card-img-top"
                                            onerror="this.onerror=null; this.src='images/hero-bg.jpg';">
                                   </div>
          
           
                                   <div class="movie-info">
                                        <h5 class="card-title"><%= movie.getTitle() %></h5>
                                        <p class="movie-description"><%= movie.getDescription() != null ? movie.getDescription() : "No description available" %></p>
                                        <div class="movie-rating mb-2">
                                            <%= generateStarRating(movie.getRatingAsDouble()) %>
                                        </div>
                                        <div class="movie-details">
                                            <p class="movie-duration">
                                                <i class="fas fa-clock"></i> <%= movie.getDuration() != null ? movie.getDuration() : "TBA" %>
                                            </p>
                                            <p class="movie-status">
                                                <i class="fas fa-info-circle"></i> <%= movie.getStatus() != null ? movie.getStatus() : "ACTIVE" %>
                                            </p>
                                            <p class="movie-release">
                                                <i class="fas fa-calendar"></i> <%= movie.getReleaseDate() != null ? movie.getReleaseDate() : "Coming Soon" %>
                                            </p>
                                        </div>
                                        <p class="genre-badge">
                                            <span class="badge badge-secondary"><%= movie.getGenre() != null ? movie.getGenre() : "Unspecified" %></span>
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
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                %>
                    <div class="col-12">
                        <div class="alert alert-danger">Error loading movies: <%= e.getMessage() %></div>
                    </div>
                <%
                }
                %>
            </div>
        </div>
    </section>
    

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
    

    <!-- Footer -->
    <footer class="footer bg-dark text-white">
        <div class="container py-4">
            <div class="row">
                <div class="col-md-6">
                    <h5>Movie Ticket Booking</h5>
                    <p>Book your favorite movies easily</p>
                </div>
                <div class="col-md-6">
                    <h5>Contact Us</h5>
                    <p>Email: contact@example.com</p>
                </div>
            </div>
        </div>
    </footer>
    
    


    <!-- Scripts -->
    <script src="https://code.jquery.com/jquery-3.5.1.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@4.5.2/dist/js/bootstrap.bundle.min.js"></script>
   <!--  <script src="${pageContext.request.contextPath}/js/main.js"></script>  -->
    <script>
        function filterMovies(genre) {
            document.querySelectorAll('.genre-filter').forEach(button => {
                button.classList.remove('active');
                if(button.textContent.toLowerCase() === genre.toLowerCase()) {
                    button.classList.add('active');
                }
            });

            document.querySelectorAll('.movie-item').forEach(movieItem => {
                const movieGenre = movieItem.dataset.genre;
                if (genre.toLowerCase() === 'all' || movieGenre === genre.toLowerCase()) {
                    movieItem.style.display = '';
                } else {
                    movieItem.style.display = 'none';
                }
            });
        }
    </script>
</body>
</html>