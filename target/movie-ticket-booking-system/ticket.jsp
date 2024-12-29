<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
    <title>Your Ticket</title>
    <link href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css" rel="stylesheet">
    <style>
        .ticket {
            border: 2px dashed #007bff;
            padding: 20px;
            margin: 20px auto;
            max-width: 600px;
            background-color: #fff;
        }
        @media print {
            .no-print { display: none; }
            .ticket { border: 2px dashed #000; }
        }
    </style>
</head>
<body class="bg-light">
    <div class="container mt-5">
        <c:if test="${not empty errorMessage}">
            <div class="alert alert-danger">${errorMessage}</div>
        </c:if>
        
        <c:if test="${not empty booking}">
            <div class="ticket">
                <h2 class="text-center mb-4">Movie Ticket</h2>
                <div class="row">
                    <div class="col-md-6">
                        <p><strong>Booking ID:</strong> #${booking.bookingId}</p>
                        <p><strong>Movie:</strong> ${sessionScope.movieTitle}</p>
                        <p><strong>Theatre:</strong> ${sessionScope.theatreName}</p>
                        <p><strong>Date:</strong> ${sessionScope.showDate}</p>
                        <p><strong>Time:</strong> ${sessionScope.showtime}</p>
                    </div>
                    <div class="col-md-6">
                        <p><strong>Selected Seats:</strong> ${sessionScope.selectedSeats}</p>
                        <p><strong>Number of Seats:</strong> ${booking.totalSeats}</p>
                        <p><strong>Total Amount Paid:</strong> $${sessionScope.totalAmount}</p>
                        <p><strong>Booking Date:</strong> 
                            <fmt:formatDate value="${booking.bookingDate}" pattern="MMM dd, yyyy HH:mm"/>
                        </p>
                    </div>
                </div>
                <div class="text-center mt-4 no-print">
                    <button onclick="window.print()" class="btn btn-primary">
                        Print Ticket
                    </button>
                    <a href="${pageContext.request.contextPath}/index.jsp" class="btn btn-secondary ml-2">
                        Back to Home
                    </a>
                </div>
            </div>
        </c:if>
    </div>
</body>
</html>
