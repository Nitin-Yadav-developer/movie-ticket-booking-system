<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Showtimes</title>
    <link href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css" rel="stylesheet">
    <style>
        .card {
            transition: transform 0.3s;
        }
        
        
        .card:hover {
            transform: translateY(-5px);
            box-shadow: 0 4px 8px rgba(0,0,0,0.2);
        }
        .showtime-date {
            color: #007bff;
            font-weight: bold;
        }
        
        
        
        .showtime-time {
            color: #28a745;
        }
        
        
        .error-message {
            color: #dc3545;
            padding: 10px;
            margin: 10px 0;
            border-radius: 4px;
            background-color: #f8d7da;
        }
        .movie-title {
            font-size: 1.2em;
            font-weight: bold;
            color: #333;
        }
        .theatre-name {
            color: #666;
            font-style: italic;
        }
    </style>
</head>
<body>
    <!-- Add Navigation Bar -->
    <nav class="navbar navbar-expand-lg navbar-light bg-light mb-4">
        <div class="container">
            <a class="navbar-brand" href="${pageContext.request.contextPath}/">Movie Ticket Booking</a>
            <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarNav">
                <span class="navbar-toggler-icon"></span>
            </button>
            <div class="collapse navbar-collapse" id="navbarNav">
                <ul class="navbar-nav ml-auto">
                    <li class="nav-item">
                        <a class="nav-link" href="${pageContext.request.contextPath}/">Home</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="${pageContext.request.contextPath}/movies.jsp">Movies</a>
                    </li>
                    <li class="nav-item active">
                        <a class="nav-link" href="${pageContext.request.contextPath}/showtime/list">Showtimes</a>
                    </li>
                    
                </ul>
            </div>
        </div>
    </nav>

    <div class="container">
        <h1 class="my-4 text-center">Movie Showtimes</h1>
        
        <!-- Add this debug section -->
        <% 
        System.out.println("Showtimes attribute: " + request.getAttribute("showtimes"));
        System.out.println("Message attribute: " + request.getAttribute("message"));
        System.out.println("Error attribute: " + request.getAttribute("error"));
        %>
        
        <div class="row mb-4">
            <div class="col-md-6">
                <select class="form-control" id="sortOption" onchange="sortShowtimes()">
                    <option value="date">Sort by Date</option>
                    <option value="movie">Sort by Movie</option>
                    <option value="theatre">Sort by Theatre</option>
                </select>
            </div>
        </div>
        
        <c:if test="${not empty message}">
            <div class="alert alert-info">${message}</div>
        </c:if>
        
        <c:if test="${not empty error}">
            <div class="alert alert-danger">${error}</div>
        </c:if>

        <div class="row">
            <c:choose>
                <c:when test="${not empty requestScope.showtimes}">
                    <c:forEach var="showtime" items="${requestScope.showtimes}">
                        <div class="col-md-4 mb-4">
                            <div class="card h-100">
                                <div class="card-body">
                                    <h5 class="movie-title">${showtime.movieTitle}</h5>
                                    <p class="theatre-name">Theatre: ${showtime.theatreName}</p>
                                    <p class="card-text showtime-date">
                                        <fmt:formatDate value="${showtime.show_date}" pattern="EEEE, MMMM d, yyyy"/>
                                    </p>
                                    <p class="card-text showtime-time">
                                        Time: <fmt:formatDate value="${showtime.show_time}" pattern="h:mm a"/>
                                    </p>
                                    <p class="card-text">Price: $${showtime.price}</p>
                                    <p class="card-text">Available Seats: ${showtime.available_seats}</p>
                                    <p class="card-text">Status: ${showtime.status}</p>
                                </div>
                                <div class="card-footer">
                                    <form action="${pageContext.request.contextPath}/ProcessBooking" method="post" 
                                          onsubmit="return validateBooking(${showtime.available_seats})">
                                        <input type="hidden" name="showtimeId" value="${showtime.showtime_id}">
                                        <input type="hidden" name="movieTitle" value="${showtime.movieTitle}">
                                        <input type="hidden" name="theatreName" value="${showtime.theatreName}">
                                        <input type="hidden" name="ticketPrice" value="${showtime.price}">
                                        <input type="hidden" name="showDate" value="<fmt:formatDate value="${showtime.show_date}" pattern="yyyy-MM-dd"/>">
                                        <input type="hidden" name="theatreId" value="${showtime.theatre_id}">
                                        <input type="hidden" name="show_time" value="<fmt:formatDate value="${showtime.show_time}" pattern="HH:mm:ss"/>">
                                        <button type="submit" class="btn btn-primary btn-block">Book Now</button>
                                    </form>
                                </div>
                            </div>
                        </div>
                    </c:forEach>
                </c:when>
                <c:otherwise>
                    <div class="col-12">
                        <div class="alert alert-info text-center">
                            <h4>No showtimes available</h4>
                            <p>Please check back later for updated showtimes.</p>
                        </div>
                    </div>
                </c:otherwise>
            </c:choose>
        </div>
    </div>

    <!-- Scripts -->
    <script src="https://code.jquery.com/jquery-3.5.1.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@4.5.2/dist/js/bootstrap.bundle.min.js"></script>
    <script>
        // Wait for document to be ready
        $(document).ready(function() {
            // Initialize sort select with current value if any
            const urlParams = new URLSearchParams(window.location.search);
            const sortParam = urlParams.get('sort');
            if (sortParam) {
                $('#sortOption').val(sortParam);
            }

            // Add event listener to sort dropdown
            $('#sortOption').on('change', function() {
                sortShowtimes();
            });
        });

        function sortShowtimes() {
            try {
                const sort = document.getElementById('sortOption').value;
                if (sort) {
                    window.location.href = '${pageContext.request.contextPath}/showtime/list?sort=' + encodeURIComponent(sort);
                }
            } catch (error) {
                console.error('Error during sorting:', error);
            }
        }

        function validateBooking(availableSeats) {
            try {
                if (!availableSeats || availableSeats <= 0) {
                    alert('Sorry, this showtime is sold out');
                    return false;
                }
                return confirm('Do you want to proceed with the booking?');
            } catch (error) {
                console.error('Error during booking validation:', error);
                return false;
            }
        }
    </script>
</body>
</html>
