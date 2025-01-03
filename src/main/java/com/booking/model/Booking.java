package com.booking.model;

import java.sql.Timestamp;

public class Booking {
    private int bookingId;
    private int showtimeId;
    private Integer userId;    // Changed to Integer to allow null
    private String seatsBooked; // Changed to String to store JSON
    private Timestamp bookingDate;
    private int theatreId;    // Added new field
    private int totalSeats;   // Added new field
    private String theatreLocation; // Add this field
    
    // Additional fields for display purposes
    private String movieTitle;
    private String theatreName;
    private double ticketPrice;
    private double totalAmount;
    private String booking_status;
    // Default constructor
    public Booking() {}
    
    // Updated constructor with new fields
    

    // Getters and Setters
    public int getBookingId() { return bookingId; }
    public Booking(int bookingId, int showtimeId, Integer userId, String seatsBooked, Timestamp bookingDate,
			int theatreId, int totalSeats, String movieTitle, String theatreName, double ticketPrice,
			double totalAmount, String booking_status) {
		super();
		this.bookingId = bookingId;
		this.showtimeId = showtimeId;
		this.userId = userId;
		this.seatsBooked = seatsBooked;
		this.bookingDate = bookingDate;
		this.theatreId = theatreId;
		this.totalSeats = totalSeats;
		this.movieTitle = movieTitle;
		this.theatreName = theatreName;
		this.ticketPrice = ticketPrice;
		this.totalAmount = totalAmount;
		this.booking_status = booking_status;
	}

	public void setBookingId(int bookingId) { this.bookingId = bookingId; }

    public int getShowtimeId() { return showtimeId; }
    public void setShowtimeId(int showtimeId) { this.showtimeId = showtimeId; }

    public Integer getUserId() { return userId; }
    public void setUserId(Integer userId) { this.userId = userId; }

    public String getSeatsBooked() { return seatsBooked; }
    public void setSeatsBooked(String seatsBooked) { this.seatsBooked = seatsBooked; }

    public Timestamp getBookingDate() { return bookingDate; }
    public void setBookingDate(Timestamp bookingDate) { this.bookingDate = bookingDate; }

    public int getTheatreId() { return theatreId; }
    public void setTheatreId(int theatreId) { this.theatreId = theatreId; }

    public int getTotalSeats() { return totalSeats; }
    public void setTotalSeats(int totalSeats) { this.totalSeats = totalSeats; }

    public String getMovieTitle() { return movieTitle; }
    public void setMovieTitle(String movieTitle) { this.movieTitle = movieTitle; }

    public String getTheatreName() { return theatreName; }
    public void setTheatreName(String theatreName) { this.theatreName = theatreName; }

    public double getTicketPrice() { return ticketPrice; }
    public void setTicketPrice(double ticketPrice) { this.ticketPrice = ticketPrice; }

    public double getTotalAmount() { return totalAmount; }
    public void setTotalAmount(double totalAmount) { this.totalAmount = totalAmount; }

    public String getTheatreLocation() { return theatreLocation; }
    public void setTheatreLocation(String theatreLocation) { this.theatreLocation = theatreLocation; }

	public String getBooking_status() {
		return booking_status;
	}

	public void setBooking_status(String booking_status) {
		this.booking_status = booking_status;
	}

	@Override
	public String toString() {
		return "Booking [bookingId=" + bookingId + ", showtimeId=" + showtimeId + ", userId=" + userId
				+ ", seatsBooked=" + seatsBooked + ", bookingDate=" + bookingDate + ", theatreId=" + theatreId
				+ ", totalSeats=" + totalSeats + ", movieTitle=" + movieTitle + ", theatreName=" + theatreName
				+ ", ticketPrice=" + ticketPrice + ", totalAmount=" + totalAmount + ", booking_status=" + booking_status
				+ "]";
	}
	
}

