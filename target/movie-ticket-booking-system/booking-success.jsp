<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>Booking Successful</title>
    <link href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
    <div class="container mt-5">
        <div class="card text-center">
            <div class="card-body">
                <i class="fas fa-check-circle text-success" style="font-size: 48px;"></i>
                <h2 class="card-title mt-3">Booking Successful!</h2>
                <p class="card-text">Your booking has been confirmed.</p>
                <p class="card-text">Booking ID: #${sessionScope.bookingId}</p>
                <div class="spinner-border text-primary" role="status">
                    <span class="sr-only">Loading...</span>
                </div>
                <p class="mt-3">Redirecting to your ticket in <span id="counter">3</span> seconds...</p>
            </div>
        </div>
    </div>

    <script>
        let count = 3;
        const counter = document.getElementById('counter');
        
        const countdown = setInterval(() => {
            count--;
            counter.textContent = count;
            if (count <= 0) {
                clearInterval(countdown);
                window.location.href = "${pageContext.request.contextPath}/GenerateTicket";
            }
        }, 1000);
    </script>
</body>
</html>