<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
    <title>My Bookings</title>
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css" rel="stylesheet">

    <link href="css/styles.css" rel="stylesheet">
    <link href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
    <%@ include file="navbar.jsp" %>
    
    <div class="container mt-5 pt-4">
        <div class="bookings-container">
            <h2>My Bookings History</h2>
            
            <% if(session.getAttribute("user") == null) { %>
                <div class="alert alert-warning">Please login to view your bookings.</div>
            <% } else { %>
                <c:if test="${empty bookings}">
                    <div class="alert alert-info">You haven't made any bookings yet.</div>
                </c:if>
                
                <c:if test="${not empty bookings}">
                    <div class="table-responsive">
                        <table class="table table-striped">
                            <thead>
                                <tr>
                                    <th>Booking ID</th>
                                    <th>Movie</th>
                                    <th>Seats</th>
                                    <th>Total Amount</th>
                                    <th>Booking Date</th>
                                    <th>Status</th>
                                    <th>Ticket</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach items="${bookings}" var="booking">
                                    <tr>
                                        <td>${booking.bookingId}</td>
                                        <td><c:out value="${booking.movieTitle}" default="N/A"/></td>
                                        <td><c:out value="${booking.seatsBooked}"/></td>
                                        <td><fmt:formatNumber value="${booking.totalAmount}" type="currency"/></td>
                                        <td>
                                            <fmt:formatDate value="${booking.bookingDate}" pattern="yyyy-MM-dd HH:mm"/>
                                        </td>
                                        <td>
                                            <span class="badge badge-success">Confirmed</span>
                                        </td>
                                        <td>
                                            <form action="${pageContext.request.contextPath}/GenerateTicket" method="GET">
                                                <input type="hidden" name="bookingId" value="${booking.bookingId}">
                                                <button type="submit" class="btn btn-sm btn-primary">
                                                    <i class="fas fa-download"></i> Download Ticket
                                                </button>
                                            </form>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                    </div>
                </c:if>
            <% } %>
        </div>
    </div>
    
    <script src="https://code.jquery.com/jquery-3.5.1.min.js"></script>
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.bundle.min.js"></script>
    
</body>
</html>
