package com.showtime.model;

import java.util.Date;



public class Showtime {
    private int showtime_id;    // Changed from showtimeId
    private int movie_id;       // Changed from movieId
    private int theatre_id;     // Changed from theatreId
    private Date showDate;      // Moved before showtime to match schema
    private Date showtime;
    private String movieTitle;
    private String theatreName;
    private int totalSeats;
    private int availableSeats;
    private double ticketPrice;

    public Showtime() {
        super();
    }

    public Showtime(int showtime_id, int movie_id, int theatre_id, Date showDate, 
    		Date showtime, String movieTitle, String theatreName, 
                    int totalSeats, int availableSeats, double ticketPrice) {
        this.showtime_id = showtime_id;
        this.movie_id = movie_id;
        this.theatre_id = theatre_id;
        this.showDate = showDate;
        this.showtime = showtime;
        this.movieTitle = movieTitle;
        this.theatreName = theatreName;
        this.totalSeats = totalSeats;
        this.availableSeats = availableSeats;
        this.ticketPrice = ticketPrice;
    }

    // Updated getters and setters
    public int getShowtime_id() { return showtime_id; }
    public void setShowtime_id(int showtime_id) { this.showtime_id = showtime_id; }

    public int getMovie_id() { return movie_id; }
    public void setMovie_id(int movie_id) { this.movie_id = movie_id; }

    public int getTheatre_id() { return theatre_id; }
    public void setTheatre_id(int theatre_id) { this.theatre_id = theatre_id; }

    public Date getShowDate() { return showDate; }
    public void setShowDate(Date date) { this.showDate = date; }

    public Date getShowtime() { return showtime; }
    public void setShowtime(Date showtime) { this.showtime = showtime; }

    public String getMovieTitle() { return movieTitle; }
    public void setMovieTitle(String movieTitle) { this.movieTitle = movieTitle; }

    public String getTheatreName() { return theatreName; }
    public void setTheatreName(String theatreName) { this.theatreName = theatreName; }

    public int getTotalSeats() { return totalSeats; }
    public void setTotalSeats(int totalSeats) { this.totalSeats = totalSeats; }

    public int getAvailableSeats() { return availableSeats; }
    public void setAvailableSeats(int availableSeats) { this.availableSeats = availableSeats; }

    public double getTicketPrice() { return ticketPrice; }
    public void setTicketPrice(double ticketPrice) { this.ticketPrice = ticketPrice; }

    @Override
    public String toString() {
        return "Showtime [showtime_id=" + showtime_id + ", movie_id=" + movie_id + 
               ", theatre_id=" + theatre_id + ", showDate=" + showDate + 
               ", showtime=" + showtime + ", movieTitle='" + movieTitle + '\'' + 
               ", theatreName='" + theatreName + '\'' + ", totalSeats=" + totalSeats + 
               ", availableSeats=" + availableSeats + ", ticketPrice=" + ticketPrice + "]";
    }
}


