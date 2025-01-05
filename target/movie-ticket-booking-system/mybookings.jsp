<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
    <title>My Bookings</title>
    <link href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css" rel="stylesheet">
    <style>
        .booking-card {
            margin-bottom: 20px;
            box-shadow: 0 2px 4px rgba(0,0,0,0.1);
        }
        .booking-header {
            background-color: #f8f9fa;
            padding: 15px;
            border-bottom: 1px solid #dee2e6;
        }
        .booking-body {
            padding: 15px;
        }
        .badge-upcoming {
            background-color: #28a745;
            color: white;
        }
        .badge-completed {
            background-color: #6c757d;
            color: white;
        }
    </style>
</head>
<body>
    <%@ include file="navbar.jsp" %>
    
    <div class="container mt-5 pt-4">
        <h2 class="mb-4">My Bookings</h2>
        
        <c:choose>
            <c:when test="${empty sessionScope.user}">
                <div class="alert alert-warning">
                    Please <a href="login.jsp">login</a> to view your bookings.
                </div>
            </c:when>
            <c:when test="${empty bookings}">
                <div class="alert alert-info">
                    You haven't made any bookings yet. 
                    <a href="index.jsp">Browse movies</a>
                </div>
            </c:when>
            <c:otherwise>
                <div class="table-responsive">
                    <table class="table table-hover">
                        <thead class="thead-dark">
                            <tr>
                                <th>Booking ID</th>
                                <th>Movie</th>
                                <th>Theatre</th>
                                <th>Seats</th>
                                <th>Amount</th>
                                <th>Booking Date</th>
                                <th>Status</th>
                                <th>Actions</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach items="${bookings}" var="booking">
                                <tr>
                                    <td>${booking.bookingId}</td>
                                    <td>${booking.movieTitle}</td>
                                    <td>${booking.theatreName}</td>
                                    <td>
                                        <span class="badge badge-info">
                                            ${booking.totalSeats} seats
                                        </span>
                                        <small class="d-block text-muted">
                                            ${booking.seatsBooked}
                                        </small>
                                    </td>
                                    <td>
                                        <fmt:formatNumber value="${booking.totalAmount}" 
                                            type="currency" currencySymbol="₹"/>
                                    </td>
                                    <td>
                                        <fmt:formatDate value="${booking.bookingDate}" 
                                            pattern="dd MMM yyyy, hh:mm a"/>
                                    </td>
                                    <td>
                                        <span class="badge badge-${booking.booking_status eq 'CONFIRMED' ? 'success' : 'secondary'}">
                                            ${booking.booking_status}
                                        </span>
                                    </td>
                                    <td>
                                        <form action="${pageContext.request.contextPath}/GenerateTicket" 
                                              method="GET" class="d-inline">
                                            <input type="hidden" name="bookingId" value="${booking.bookingId}">
                                            <input type="hidden" name="movieTitle" value="${booking.movieTitle}">
                                            <input type="hidden" name="theatreName" value="${booking.theatreName}">
                                            <input type="hidden" name="selectedSeats" value="${booking.seatsBooked}">
                                            <input type="hidden" name="totalAmount" value="${booking.totalAmount}">
                                            <button type="submit" class="btn btn-sm btn-primary">
                                                <i class="fas fa-ticket-alt"></i> View Ticket
                                            </button>
                                        </form>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
            </c:otherwise>
        </c:choose>
    </div>

    <script src="https://code.jquery.com/jquery-3.5.1.min.js"></script>
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.bundle.min.js"></script>
</body>
</html>
