package com.booking.model;

import java.sql.Date;

public class Booking {

	
	
	
	    private int bookingId;
	    private int userId;
	    private int showtimeId;
	    private int numberOfSeats;
	    private Date bookingdate;

	    public Booking() {
			super();
			// TODO Auto-generated constructor stub
		}
	    
	    
		public Booking(int bookingId, int userId, int showtimeId,int numberofSeats, Date bookingdate) {
			super();
			this.bookingId = bookingId;
			this.userId = userId;
			this.showtimeId = showtimeId;
			this.numberOfSeats = numberofSeats;
			this.bookingdate=bookingdate;
		}


		// Getters and Setters
	    public int getBookingId() { return bookingId; }
	    public void setBookingId(int bookingId) { this.bookingId = bookingId; }

	    public int getUserId() { return userId; }
	    public void setUserId(int userId) { this.userId = userId; }

	    public int getShowtimeId() { return showtimeId; }
	    public void setShowtimeId(int showtimeId) { this.showtimeId = showtimeId; }

	    public int getNumberOfSeats() { return numberOfSeats; }
	    public void setNumberOfSeats(int numberOfSeats) { this.numberOfSeats = numberOfSeats; }


		

		public Date getBookingdate() {
			return bookingdate;
		}


		public void setBookingdate(Date bookingdate) {
			this.bookingdate = bookingdate;
		}


		@Override
		public String toString() {
			return "Booking [bookingId=" + bookingId + ", userId=" + userId + ", showtimeId=" + showtimeId
					+ ", numberOfSeats=" + numberOfSeats + ", bookingdate=" + bookingdate + "]";
		}


		

}

