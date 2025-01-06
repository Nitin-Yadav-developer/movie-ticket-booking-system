<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>  <!-- Add this line -->
<!DOCTYPE html>
<html>
<head>



    <meta charset="UTF-8">
    <title>Payment - Movie Ticket Booking</title>
    <link href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>

    <div class="container mt-5">
        <div class="row justify-content-center">
            <div class="col-md-6">
                <div class="card">
                    <div class="card-header">
                        <h3 class="text-center">Payment Details</h3>
                    </div>
                    <div class="card-body">
                        <div class="booking-summary mb-4">
                            <h5 class="card-title border-bottom pb-2">Booking Summary</h5>
                            <div class="row">
                                <div class="col-md-6">
                                    <p><strong>Movie:</strong> ${sessionScope.bookingMovieTitle}</p>
                                    <p><strong>Theatre:</strong> ${sessionScope.bookingTheatreName}</p>
                                    <p><strong>Date:</strong> <fmt:formatDate value="${sessionScope.bookingShowDate}" pattern="EEE, MMM dd, yyyy"/></p>
                                    <p><strong>Time:</strong> <fmt:formatDate value="${sessionScope.show_time}" pattern="hh:mm a"/></p>
                                </div>
                                <div class="col-md-6">
                                    <p><strong>Selected Seats:</strong> ${sessionScope.paymentSelectedSeats}</p>
                                    <p><strong>Number of Seats:</strong> ${sessionScope.paymentTotalSeats}</p>
                                    <p><strong>Price per Ticket:</strong> $${sessionScope.bookingTicketPrice}</p>
                                    <hr>
                                    <p class="h5 text-primary"><strong>Total Amount:</strong> $${sessionScope.paymentTotalAmount}</p>
                                </div>
                            </div>
                        </div>

                        <div class="payment-form">
                            <h5 class="card-title border-bottom pb-2">Payment Details</h5>
                            <c:if test="${not empty error}">
                                <div class="alert alert-danger">${error}</div>
                            </c:if>
                            <form action="${pageContext.request.contextPath}/processPayment" method="POST" 
                                  id="paymentForm" onsubmit="return validatePaymentForm()">
                                <!-- Add hidden fields for booking details -->
                                <input type="hidden" name="showtimeId" value="${sessionScope.bookingShowtimeId}">
                                <input type="hidden" name="userId" value="${sessionScope.user.user_id}">
                                <input type="hidden" name="selectedSeats" value='${sessionScope.paymentSelectedSeats}'>
                                <input type="hidden" name="totalSeats" value="${sessionScope.paymentTotalSeats}">
                                <input type="hidden" name="totalAmount" value="${sessionScope.paymentTotalAmount}">
                                <input type="hidden" name="theatreId" value="${sessionScope.bookingTheatreId}">
                                
                                <div class="form-group">
                                    <label>Card Number</label>
                                    <input type="text" class="form-control" name="cardNumber" id="cardNumber" 
                                           pattern="\d{16}" maxlength="16" required 
                                           placeholder="Enter 16-digit card number">
                                </div>
                                
                                <div class="form-row">
                                    <div class="form-group col-md-6">
                                        <label>Expiry Date</label>
                                        <input type="text" class="form-control" name="expiryDate" id="expiryDate"
                                               pattern="(0[1-9]|1[0-2])\/([0-9]{2})" placeholder="MM/YY" required>
                                    </div>
                                    <div class="form-group col-md-6">
                                        <label>CVV</label>
                                        <input type="password" class="form-control" name="cvv" id="cvv"
                                               pattern="\d{3}" maxlength="3" required>
                                    </div>
                                </div>
                                
                                <div class="text-center mt-4">
                                    <button type="submit" class="btn btn-primary btn-lg btn-block" id="payButton">
                                        Pay $${sessionScope.paymentTotalAmount}
                                    </button>
                                    <a href="javascript:history.back()" class="btn btn-secondary btn-block mt-2">
                                        Cancel Booking
                                    </a>
                                </div>
                            </form>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    
    <script src="https://code.jquery.com/jquery-3.5.1.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@4.5.2/dist/js/bootstrap.bundle.min.js"></script>
    <script>
        function validatePaymentForm() {
            // Get the selected seats and format as JSON array
            const selectedSeats = document.getElementsByName('selectedSeats')[0].value;
            try {
                // If seats are comma-separated, convert to JSON array
                if (selectedSeats.includes(',')) {
                    const seatsArray = selectedSeats.split(',').map(seat => seat.trim());
                    document.getElementsByName('selectedSeats')[0].value = JSON.stringify(seatsArray);
                } else {
                    // If single seat, make it an array
                    document.getElementsByName('selectedSeats')[0].value = JSON.stringify([selectedSeats]);
                }
                
                // Regular validation
                const cardNumber = document.getElementById('cardNumber').value;
                const expiryDate = document.getElementById('expiryDate').value;
                const cvv = document.getElementById('cvv').value;

                if (!/^\d{16}$/.test(cardNumber)) {
                    alert('Please enter a valid 16-digit card number');
                    return false;
                }
                if (!/^(0[1-9]|1[0-2])\/\d{2}$/.test(expiryDate)) {
                    alert('Please enter a valid expiry date (MM/YY)');
                    return false;
                }
                if (!/^\d{3}$/.test(cvv)) {
                    alert('Please enter a valid 3-digit CVV');
                    return false;
                }
                
                return true;
            } catch (e) {
                console.error('Error formatting seats:', e);
                alert('Error processing seats selection');
                return false;
            }
        }

        // Add input formatting for expiry date
        document.getElementById('expiryDate').addEventListener('input', function(e) {
            let value = e.target.value.replace(/\D/g, '');
            if (value.length >= 2) {
                value = value.substr(0,2) + '/' + value.substr(2);
            }
            e.target.value = value;
        });
    </script>
</body>
</html>
