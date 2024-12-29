<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
    <title>Book Tickets</title>
    <link href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css" rel="stylesheet">
    <style>
        .screen {
            background: #333;
            height: 70px;
            width: 100%;
            margin: 15px 0;
            color: white;
            line-height: 70px;
            font-size: 25px;
            text-align: center;
        }
        .seat {
            width: 35px;
            height: 35px;
            margin: 5px;
            border: none;
            border-radius: 5px;
            cursor: pointer;
            position: relative;
        }
        .available {
            background-color: #4CAF50;
        }
        .selected {
            background-color: #2196F3;
        }
        .booked {
            background-color: #f44336;
            cursor: not-allowed;
        }
        .seat-container {
            margin: 20px auto;
            max-width: 700px;
        }
        .row-label {
            width: 30px;
            display: inline-block;
            text-align: center;
            margin-right: 10px;
        }
        .seat-number {
            font-size: 12px;
            position: absolute;
            top: 50%;
            left: 50%;
            transform: translate(-50%, -50%);
            color: #333;
        }
    </style>
</head>
<body>
    <div class="container mt-5">
        <div class="row">
            <div class="col-md-8">
                <h2>Select Seats</h2>
                <div class="screen">SCREEN</div>
                <div class="seat-container" id="seatsContainer">
                    <!-- Seats will be generated here -->
                </div>
                <div class="text-center mt-3">
                    <div class="d-inline-block mr-3">
                        <button class="seat available mr-2" disabled></button>Available
                    </div>
                    <div class="d-inline-block mr-3">
                        <button class="seat selected mr-2" disabled></button>Selected
                    </div>
                    <div class="d-inline-block">
                        <button class="seat booked mr-2" disabled></button>Booked
                    </div>
                </div>
            </div>
            <div class="col-md-4">
                <div class="card">
                    <div class="card-body">
                        <h5 class="card-title">Booking Summary</h5>
                        <div class="booking-details">
                            <p><strong>Movie:</strong> ${showtime.movieTitle}</p>
                            <p><strong>Theatre:</strong> ${showtime.theatreName}</p>
                            <p><strong>Date:</strong> <fmt:formatDate value="${showtime.showDate}" pattern="EEE, MMM dd, yyyy"/></p>
                            <p><strong>Time:</strong> <fmt:formatDate value="${showtime.showtime}" pattern="hh:mm a"/></p>
                            <p><strong>Price per Ticket:</strong> $${showtime.ticketPrice}</p>
                            <p><strong>Selected Seats:</strong> <span id="selectedSeatsDisplay">None</span></p>
                            <p><strong>Total Seats:</strong> <span id="totalSeatsDisplay">0</span></p>
                            <p><strong>Total Amount:</strong> $<span id="totalAmount">0.00</span></p>
                            
                            <form action="${pageContext.request.contextPath}/payment" method="POST" id="bookingForm">
                                <input type="hidden" name="showtimeId" value="${showtime.showtime_id}">
                                <input type="hidden" name="movieTitle" value="${showtime.movieTitle}">
                                <input type="hidden" name="theatreId" value="${showtime.theatre_id}">
                                <input type="hidden" name="theatreName" value="${showtime.theatreName}">
                                <input type="hidden" name="showDate" value="<fmt:formatDate value="${showtime.showDate}" pattern="yyyy-MM-dd"/>">
                                <input type="hidden" name="showtime" value="<fmt:formatDate value="${showtime.showtime}" pattern="HH:mm:ss"/>">
                                <input type="hidden" name="selectedSeats" id="selectedSeatsInput">
                                <input type="hidden" name="totalSeats" id="totalSeatsInput">
                                <input type="hidden" name="ticketPrice" value="${showtime.ticketPrice}">
                                <input type="hidden" name="totalAmount" id="totalAmountInput">
                                <button type="submit" class="btn btn-primary btn-block" id="confirmButton" disabled>
                                    Proceed to Payment
                                </button>
                            </form>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <script>
        const ROWS = 8;
        const COLS = 10;
        const TICKET_PRICE = ${showtime.ticketPrice};
        let selectedSeats = [];

        function initializeSeats() {
            const container = document.getElementById('seatsContainer');
            for (let i = 0; i < ROWS; i++) {
                const row = document.createElement('div');
                row.className = 'text-center';
                
                // Add row label (A, B, C, etc.)
                const rowLabel = document.createElement('span');
                rowLabel.className = 'row-label';
                rowLabel.textContent = String.fromCharCode(65 + i);
                row.appendChild(rowLabel);

                for (let j = 0; j < COLS; j++) {
                    const seat = document.createElement('button');
                    seat.className = 'seat available';
                    const seatNumber = String.fromCharCode(65 + i) + (j + 1);
                    seat.dataset.row = String.fromCharCode(65 + i);
                    seat.dataset.col = j + 1;
                    
                    // Add seat number inside the button
                    const seatLabel = document.createElement('span');
                    seatLabel.className = 'seat-number';
                    seatLabel.textContent = seatNumber;
                    seat.appendChild(seatLabel);
                    
                    seat.addEventListener('click', () => toggleSeat(seat));
                    row.appendChild(seat);
                }
                container.appendChild(row);
            }
        }

        function toggleSeat(seat) {
            // Get the actual seat number (e.g., "A1", "B2")
            const seatNumber = seat.querySelector('.seat-number').textContent;
            
            if (seat.classList.contains('available')) {
                if (selectedSeats.length >= 10) {
                    alert('Maximum 10 seats can be selected at once');
                    return;
                }
                seat.classList.remove('available');
                seat.classList.add('selected');
                selectedSeats.push(seatNumber);
            } else if (seat.classList.contains('selected')) {
                seat.classList.remove('selected');
                seat.classList.add('available');
                selectedSeats = selectedSeats.filter(s => s !== seatNumber);
            }
            
            // Sort seats naturally (A1, A2, B1, B2, etc.)
            selectedSeats.sort((a, b) => {
                const rowA = a.charAt(0);
                const rowB = b.charAt(0);
                const numA = parseInt(a.substring(1));
                const numB = parseInt(b.substring(1));
                return rowA === rowB ? numA - numB : rowA.localeCompare(rowB);
            });
            
            updateBookingSummary();
            updatePaymentFormFields();
        }

        function updateBookingSummary() {
            const totalAmount = selectedSeats.length * TICKET_PRICE;
            
            // Format seats display
            const seatsDisplay = selectedSeats.length > 0 ? selectedSeats.join(', ') : 'None';
            
            // Update display elements
            document.getElementById('selectedSeatsDisplay').textContent = seatsDisplay;
            document.getElementById('totalSeatsDisplay').textContent = selectedSeats.length;
            document.getElementById('totalAmount').textContent = totalAmount.toFixed(2);
            
            // Update hidden form inputs
            document.getElementById('selectedSeatsInput').value = selectedSeats.join(',');
            document.getElementById('totalSeatsInput').value = selectedSeats.length;
            document.getElementById('totalAmountInput').value = totalAmount.toFixed(2);
            
            // Enable/disable confirm button
            document.getElementById('confirmButton').disabled = selectedSeats.length === 0;
            
            // Debug output
            console.log('Selected Seats Array:', selectedSeats);
            console.log('Seats Display String:', seatsDisplay);
            console.log('Total Seats:', selectedSeats.length);
        }
        
        function updatePaymentFormFields() {
            document.getElementById('selectedSeatsInput').value = JSON.stringify(selectedSeats);
            document.getElementById('totalSeatsInput').value = selectedSeats.length;
            document.getElementById('totalAmountInput').value = calculateTotal();
        }
        
        // Initialize seats when page loads
        document.addEventListener('DOMContentLoaded', initializeSeats);
    </script>
</body>
</html>
