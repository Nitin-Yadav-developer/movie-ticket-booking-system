<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page import="com.movie.model.Movie" %>
<%@ page import="com.theatre.model.Theatre" %>
<%@ page import="com.showtime.model.Showtime" %>
<%@ page import="com.booking.model.Booking" %>
<%@ page import="java.util.List" %>

<!DOCTYPE html>
<html>
<head>
    <title>Admin Dashboard</title>
    <link href="css/styles.css" rel="stylesheet">
    <link href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.1/css/all.min.css" rel="stylesheet">
</head>

<body>
    <%@ include file="navbar.jsp" %>
    
    <div class="container-fluid mt-4">
        <!-- Error/Success Messages -->
        <c:if test="${not empty error}">
            <div class="alert alert-danger alert-dismissible fade show" role="alert">
                ${error}
                <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
        </c:if>
        
        <!-- Debug Info (remove in production) -->
        <div class="d-none">
            <p>Movies: ${movies.size()}</p>
            <p>Theaters: ${theaters.size()}</p>
            <p>Shows: ${shows.size()}</p>
            <p>Users: ${users.size()}</p>
            Total Bookings: ${totalBookings}
            Total Revenue: ${totalRevenue}
        </div>

        <div class="row">
            <!-- Sidebar -->
            <div class="col-md-2 sidebar">
                <h4>Dashboard</h4>
                <div class="list-group">
                    <a href="#overview" class="list-group-item active" data-toggle="tab">
                        <i class="fas fa-chart-line"></i> Overview
                    </a>
                    <a href="#movies" class="list-group-item" data-toggle="tab" onclick="loadMovies()">
                        <i class="fas fa-film"></i> Movies
                    </a>
                    <a href="#shows" class="list-group-item" data-toggle="tab">
                        <i class="fas fa-calendar"></i> Shows
                    </a>
                    <a href="#bookings" class="list-group-item" data-toggle="tab">
                        <i class="fas fa-ticket-alt"></i> Bookings
                    </a>
                    <a href="#users" class="list-group-item" data-toggle="tab">
                        <i class="fas fa-users"></i> Users
                    </a>
                    <a href="#theaters" class="list-group-item" data-toggle="tab">
                        <i class="fas fa-building"></i> Theaters
                    </a>
                </div>
            </div>

            <!-- Main Content -->
            <div class="col-md-10">
                <div class="tab-content">
                    <!-- Overview Tab -->
                    <div class="tab-pane fade show active" id="overview">
                        <div class="row">
                            <div class="col-md-3">
                                <div class="card bg-primary text-white">
                                    <div class="card-body">
                                        <h5>Total Bookings</h5>
                                        <h3>${totalBookings}</h3>
                                    </div>
                                </div>
                            </div>
                            <div class="col-md-3">
                                <div class="card bg-success text-white">
                                    <div class="card-body">
                                        <h5>Revenue</h5>
                                        <h3>$${totalRevenue}</h3>
                                    </div>
                                </div>
                            </div>
                            <!-- Add more statistics cards -->
                        </div>
                    </div>

                    <!-- Movies Tab -->
                    <div class="tab-pane fade" id="movies">
                        <div class="d-flex justify-content-between mb-3">
                            <h3>Movie Management</h3>
                            <button class="btn btn-primary" data-toggle="modal" data-target="#addMovieModal">
                                <i class="fas fa-plus"></i> Add New Movie
                            </button>
                        </div>
                        
                        <!-- Add this to the movies tab content section -->
                        <div id="moviesLoading" style="display:none;" class="text-center my-3">
                            <div class="spinner-border text-primary" role="status">
                                <span class="sr-only">Loading...</span>
                            </div>
                        </div>
                        
                        <%
                            List<Movie> moviesList = (List<Movie>) request.getAttribute("movies");
                            if (moviesList != null && !moviesList.isEmpty()) {
                        %>
                            <table class="table table-striped">
                                <thead>
                                    <tr>
                                        <th>ID</th>
                                        <th>Title</th>
                                        <th>Genre</th>
                                        <th>Duration</th>
                                        <th>Release Date</th>
                                        <th>Price</th>
                                        <th>Status</th>
                                        <th>Actions</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <% for(Movie movie : moviesList) { %>
                                        <tr>
                                            <td><%= movie.getMovieId() %></td>
                                            <td><%= movie.getTitle() %></td>
                                            <td><%= movie.getGenre() %></td>
                                            <td><%= movie.getDuration() %></td>
                                            <td><fmt:formatDate value="<%= movie.getReleaseDate() %>" pattern="yyyy-MM-dd"/></td>
                                            <td>₹<%= String.format("%.2f", movie.getPrice()) %></td>
                                            <td>
                                                <span class="badge badge-<%= movie.getStatus().equals("ACTIVE") ? "success" : "danger" %>">
                                                    <%= movie.getStatus() %>
                                                </span>
                                            </td>
                                            <td>
                                                <button class="btn btn-sm btn-info" onclick="editMovie(<%= movie.getMovieId() %>)">
                                                    <i class="fas fa-edit"></i>
                                                </button>
                                                <button class="btn btn-sm btn-danger" onclick="deleteMovie(<%= movie.getMovieId() %>)">
                                                    <i class="fas fa-trash"></i>
                                                </button>
                                            </td>
                                        </tr>
                                    <% } %>
                                </tbody>
                            </table>
                        <% } else { %>
                            <div class="alert alert-info">
                                <i class="fas fa-info-circle"></i> No movies found in the database.
                            </div>
                        <% } %>
                        <div class="debug-info" style="display: none;">
                            <p>Movies count: ${movies.size()}</p>
                            <c:if test="${not empty movies}">
                                <p>First movie: ${movies[0].title}</p>
                            </c:if>
                        </div>
                    </div>

                    <!-- Shows Tab -->
                    <div class="tab-pane fade" id="shows">
                        <div class="d-flex justify-content-between mb-3">
                            <h3>Show Management</h3>
                            <button class="btn btn-primary" data-toggle="modal" data-target="#addShowModal">
                                <i class="fas fa-plus"></i> Add New Show
                            </button>
                        </div>
                        
                        <c:choose>
                            <c:when test="${not empty shows}">
                                <!-- Update the shows table columns to match the Showtime model properties -->
                                <table class="table table-striped">
                                    <thead>
                                        <tr>
                                            <th>ID</th>
                                            <th>Movie</th>
                                            <th>Theater</th>
                                            <th>Date</th>
                                            <th>Time</th>
                                            <th>Price</th>
                                            <th>Available Seats</th>
                                            <th>Actions</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <c:forEach items="${shows}" var="show">
                                            <tr>
                                                <td>${show.showtime_id}</td>
                                                <td>${show.movieTitle}</td>
                                                <td>${show.theatreName}</td>
                                                <td>${show.show_date}</td>
                                                <td>${show.show_time}</td>
                                                <td>$${show.price}</td>
                                                <td>${show.available_seats}</td>
                                                <td>
                                                    <button class="btn btn-sm btn-info" onclick="editShow(${show.showtime_id})">
                                                        <i class="fas fa-edit"></i>
                                                    </button>
                                                    <button class="btn btn-sm btn-danger" onclick="deleteShow(${show.showtime_id})">
                                                        <i class="fas fa-trash"></i>
                                                    </button>
                                                </td>
                                            </tr>
                                        </c:forEach>
                                    </tbody>
                                </table>
                            </c:when>
                            <c:otherwise>
                                <div class="alert alert-info">No shows found</div>
                            </c:otherwise>
                        </c:choose>
                    </div>

                    <!-- Bookings Tab -->
                    <div class="tab-pane fade" id="bookings">
                        <h3>Booking Management</h3>
                        <div class="booking-filters mb-3">
                            <div class="row">
                                <div class="col-md-4">
                                    <input type="text" class="form-control" placeholder="Search bookings..." id="bookingSearch">
                                </div>
                                <div class="col-md-3">
                                    <select class="form-control" id="statusFilter">
                                        <option value="">All Status</option>
                                        <option value="CONFIRMED">Confirmed</option>
                                        <option value="PENDING">Pending</option>
                                        <option value="CANCELLED">Cancelled</option>
                                    </select>
                                </div>
                            </div>
                        </div>
                        
                        <!-- Debug Info -->
                        <div class="d-none">
                            <p>Total Bookings Available: ${bookings.size()}</p>
                        </div>
                        
                        <div class="table-responsive">
                            <c:choose>
                                <c:when test="${not empty bookings}">
                                    <table class="table table-striped">
                                        <thead>
                                            <tr>
                                                <th>Booking ID</th>
                                                <th>User ID</th>
                                                <th>Movie</th>
                                                <th>Show Date</th>
                                                <th>Seats</th>
                                                <th>Total Amount</th>
                                                <th>Status</th>
                                                <th>Actions</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            <c:forEach items="${bookings}" var="booking">
                                                <tr>
                                                    <td>${booking.bookingId}</td>
                                                    <td>${booking.userId}</td>
                                                    <td>${booking.movieTitle}</td>
                                                    <td><fmt:formatDate value="${booking.bookingDate}" pattern="yyyy-MM-dd HH:mm" /></td>
                                                    <td>${booking.seatsBooked}</td>
                                                    <td>₹${booking.totalAmount}</td>
                                                    <td>
                                                        <span class="badge badge-${booking.booking_status == 'CONFIRMED' ? 'success' : 
                                                            (booking.booking_status == 'PENDING' ? 'warning' : 'danger')}">
                                                            ${booking.booking_status}
                                                        </span>
                                                    </td>
                                                    <td>
                                                        <div class="btn-group">
                                                            <button class="btn btn-sm btn-info" onclick="viewBooking(${booking.bookingId})">
                                                                <i class="fas fa-eye"></i>
                                                            </button>
                                                            <button class="btn btn-sm btn-warning" onclick="updateBookingStatus(${booking.bookingId})">
                                                                <i class="fas fa-edit"></i>
                                                            </button>
                                                            <button class="btn btn-sm btn-danger" onclick="cancelBooking(${booking.bookingId})">
                                                                <i class="fas fa-times"></i>
                                                            </button>
                                                        </div>
                                                    </td>
                                                </tr>
                                            </c:forEach>
                                        </tbody>
                                    </table>
                                </c:when>
                                <c:otherwise>
                                    <div class="alert alert-info">
                                        <i class="fas fa-info-circle"></i> No bookings found.
                                    </div>
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </div>

                    <!-- Users Tab -->
                    <div class="tab-pane fade" id="users">
                        <h3>User Management</h3>
                        <table class="table table-striped">
                            <thead>
                                <tr>
                                    <th>ID</th>
                                    <th>Username</th>
                                    <th>Email</th>
                                    <th>Role</th>
                                    <!-- Removed createdAt column -->
                                    <th>Actions</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach items="${users}" var="user">
                                    <tr>
                                        <td>${user.user_id}</td>
                                        <td>${user.username}</td>
                                        <td>${user.email}</td>
                                        <td>${user.role}</td>
                                        <!-- Removed createdAt column -->
                                        <td>
                                            <form action="AdminServlet" method="post" style="display:inline;">
                                                <input type="hidden" name="action" value="deleteUser">
                                                <input type="hidden" name="userId" value="${user.user_id}">
                                                <button type="submit" class="btn btn-sm btn-danger"
                                                        onclick="return confirm('Are you sure?')">
                                                    <i class="fas fa-trash"></i>
                                                </button>
                                            </form>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                    </div>

                    <!-- Theaters Tab -->
                    <div class="tab-pane fade" id="theaters">
                        <div class="d-flex justify-content-between mb-3">
                            <h3>Theater Management</h3>
                            <button class="btn btn-primary" data-toggle="modal" data-target="#addTheaterModal">
                                <i class="fas fa-plus"></i> Add New Theater
                            </button>
                        </div>
                        <table class="table table-striped">
                            <thead>
                                <tr>
                                    <th>ID</th>
                                    <th>Name</th>
                                    <th>Location</th>
                                    <th>Total Seats</th>
                                    <th>Actions</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach items="${theaters}" var="theater">
                                    <tr></tr>
                                        <td>${theater.theatre_id}</td>
                                        <td>${theater.name}</td>
                                        <td>${theater.location}</td>
                                        <td>${theater.total_seats}</td>
                                        <td>
                                            <button class="btn btn-sm btn-info" onclick="editTheater(${theater.theatre_id})">
                                                <i class="fas fa-edit"></i>
                                            </button>
                                            <form action="AdminServlet" method="post" style="display:inline;">
                                                <input type="hidden" name="action" value="deleteTheater">
                                                <input type="hidden" name="theaterId" value="${theater.theatre_id}">
                                                <button type="submit" class="btn btn-sm btn-danger"
                                                        onclick="return confirm('Are you sure?')">
                                                    <i class="fas fa-trash"></i>
                                                </button>
                                            </form>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <!-- Add Movie Modal -->
    <div class="modal fade" id="addMovieModal">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title">Add New Movie</h5>
                    <button type="button" class="close" data-dismiss="modal">&times;</button>
                </div>
                <div class="modal-body">
                    <form action="AdminServlet" method="post">
                        <input type="hidden" name="action" value="addMovie">
                        <div class="form-group">
                            <label>Title</label>
                            <input type="text" name="title" class="form-control" required>
                        </div>
                        <div class="form-group">
                            <label>Genre</label>
                            <input type="text" name="genre" class="form-control" required>
                        </div>
                        <div class="form-group">
                            <label>Duration (minutes)</label>
                            <input type="number" name="duration" class="form-control" required>
                        </div>
                        <div class="form-group">
                            <label>Release Date</label>
                            <input type="date" name="releaseDate" class="form-control" required>
                        </div>
                        <button type="submit" class="btn btn-primary">Add Movie</button>
                    </form>
                </div>
            </div>
        </div>
    </div>

    <!-- Add Theater Modal -->
    <div class="modal fade" id="addTheaterModal">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title">Add New Theater</h5>
                    <button type="button" class="close" data-dismiss="modal">&times;</button>
                </div>
                <div class="modal-body">
                    <form action="AdminServlet" method="post">
                        <input type="hidden" name="action" value="addTheater">
                        <div class="form-group">
                            <label>Theater Name</label>
                            <input type="text" name="theaterName" class="form-control" required>
                        </div>
                        <div class="form-group">
                            <label>Location</label>
                            <input type="text" name="location" class="form-control" required>
                        </div>
                        <div class="form-group">
                            <label>Total Seats</label>
                            <input type="number" name="totalSeats" class="form-control" required>
                        </div>
                        <button type="submit" class="btn btn-primary">Add Theater</button>
                    </form>
                </div>
            </div>
        </div>
    </div>

    <!-- Add Show Modal -->
    <div class="modal fade" id="addShowModal">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title">Add New Show</h5>
                    <button type="button" class="close" data-dismiss="modal">&times;</button>
                </div>
                <div class="modal-body">
                    <form action="AdminServlet" method="post">
                        <input type="hidden" name="action" value="addShow">
                        <div class="form-group">
                            <label>Movie</label>
                            <select name="movieId" class="form-control" required>
                                <c:forEach items="${movies}" var="movie">
                                    <option value="${movie.movieId}">${movie.title}</option>
                                </c:forEach>
                            </select>
                        </div>
                        <div class="form-group">
                            <label>Theater</label>
                            <select name="theaterId" class="form-control" required>
                                <c:forEach items="${theaters}" var="theater">
                                    <option value="${theater.theatre_id}">${theater.name}</option>
                                </c:forEach>
                            </select>
                        </div>
                        <div class="form-group">
                            <label>Show Date</label>
                            <input type="date" name="showDate" class="form-control" required>
                        </div>
                        <div class="form-group">
                            <label>Show Time</label>
                            <input type="time" name="showTime" class="form-control" required>
                        </div>
                        <div class="form-group">
                            <label>Ticket Price</label>
                            <input type="number" name="ticketPrice" step="0.01" class="form-control" required>
                        </div>
                        <button type="submit" class="btn btn-primary">Add Show</button>
                    </form>
                </div>
            </div>
        </div>
    </div>

    <script src="https://code.jquery.com/jquery-3.5.1.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@4.5.2/dist/js/bootstrap.bundle.min.js"></script>
    <script src="js/admin.js"></script>
    <script>
        // Add JavaScript functions for CRUD operations
        function editMovie(movieId) {
            // Implement edit movie logic
            console.log('Editing movie:', movieId);
        }
        
        function deleteMovie(movieId) {
            if (confirm('Are you sure you want to delete this movie?')) {
                window.location.href = 'AdminServlet?action=deleteMovie&movieId=' + movieId;
            }
        }
        
        function editShow(showId) {
            // Implement edit show logic
            console.log('Editing show:', showId);
        }
        
        function deleteShow(showId) {
            if (confirm('Are you sure you want to delete this show?')) {
                window.location.href = 'AdminServlet?action=deleteShow&showId=' + showId;
            }
        }

        // Add tab switching logic
        $(document).ready(function() {
            // Show active tab on page load
            var activeTab = localStorage.getItem('activeAdminTab') || '#overview';
            $('a[href="' + activeTab + '"]').tab('show');
            
            // Store the currently active tab
            $('a[data-toggle="tab"]').on('shown.bs.tab', function (e) {
                localStorage.setItem('activeAdminTab', $(e.target).attr('href'));
            });
            
            // Add this to load movies when the movies tab is shown
            $('a[href="#movies"]').on('shown.bs.tab', function (e) {
                loadMovies();
            });
        });

        function loadMovies() {
            $('#moviesLoading').show();
            $.ajax({
                url: 'AdminServlet',
                type: 'GET',
                data: {
                    action: 'listMovies'
                },
                success: function(response) {
                    $('#moviesLoading').hide();
                    console.log('Movies loaded successfully');
                    $('#movies').html(response);
                },
                error: function(xhr, status, error) {
                    $('#moviesLoading').hide();
                    console.error('Error loading movies:', error);
                    alert('Error loading movies. Please try again.');
                }
            });
        }

        // Booking Management Functions
        function viewBooking(bookingId) {
            // Implement view booking details
            console.log('Viewing booking:', bookingId);
        }

        function updateBookingStatus(bookingId) {
            const newStatus = prompt('Enter new status (CONFIRMED/PENDING/CANCELLED):');
            if (newStatus) {
                $.ajax({
                    url: 'AdminServlet',
                    type: 'POST',
                    data: {
                        action: 'updateBooking',
                        bookingId: bookingId,
                        status: newStatus.toUpperCase()
                    },
                    success: function(response) {
                        location.reload();
                    },
                    error: function(xhr, status, error) {
                        alert('Error updating booking status: ' + error);
                    }
                });
            }
        }

        function cancelBooking(bookingId) {
            if (confirm('Are you sure you want to cancel this booking?')) {
                updateBookingStatus(bookingId, 'CANCELLED');
            }
        }

        // Add this to your existing document.ready function
        $(document).ready(function() {
            // ...existing ready code...
            
            // Booking search functionality
            $('#bookingSearch').on('keyup', function() {
                const value = $(this).val().toLowerCase();
                $('#bookings tbody tr').filter(function() {
                    $(this).toggle($(this).text().toLowerCase().indexOf(value) > -1)
                });
            });

            // Booking status filter
            $('#statusFilter').on('change', function() {
                const value = $(this).val().toLowerCase();
                if (value === '') {
                    $('#bookings tbody tr').show();
                } else {
                    $('#bookings tbody tr').each(function() {
                        const status = $(this).find('td:eq(7)').text().toLowerCase();
                        $(this).toggle(status.indexOf(value) > -1);
                    });
                }
            });
        });
    </script>
</body>
</html>
