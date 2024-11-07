package com.showtime.model;


import java.sql.Timestamp;

public class Showtime {
	
	
	    private int showtimeId;
	    private int movieId;
	    private int theatreId;
	    private Timestamp showtime;
	    

	    public Showtime() {
			super();
			// TODO Auto-generated constructor stub
		}
	    
	    
		public Showtime(int showtimeId, int movieId, int theatreId, Timestamp showtime) {
			super();
			this.showtimeId = showtimeId;
			this.movieId = movieId;
			this.theatreId = theatreId;
			this.showtime = showtime;
		}


		// Getters and Setters
	    public int getShowtimeId() { return showtimeId; }
	    public void setShowtimeId(int showtimeId) { this.showtimeId = showtimeId; }

	    public int getMovieId() { return movieId; }
	    public void setMovieId(int movieId) { this.movieId = movieId; }

	    public int getTheatreId() { return theatreId; }
	    public void setTheatreId(int theatreId) { this.theatreId = theatreId; }

	   


		


		public Timestamp getShowtime() {
			return showtime;
		}


		public void setShowtime(Timestamp showtime) {
			this.showtime = showtime;
		}


		@Override
		public String toString() {
			return "Showtime [showtimeId=" + showtimeId + ", movieId=" + movieId + ", theatreId=" + theatreId
					+ ", showtime=" + showtime + "]";
		}


		
	    
	    
	}


