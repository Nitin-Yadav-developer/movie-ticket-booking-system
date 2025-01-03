package com.showtime.model;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;

public class Showtime {
    // Match exact database column names
    private int showtime_id;
    private int movie_id;
    private int theatre_id;
    private Date show_date;
    private Time show_time;
    private Double price;
    private int available_seats;
    private String status;
    private Timestamp created_at;

    // Display fields (not in database)
    private String movieTitle;
    private String theatreName;
    private int totalSeats;

    // Constructor for creating new showtime
    public Showtime(int i, int j, int k, Date date, Timestamp timestamp, String string, String string2, int l, int m, double d) {
        this.status = "ACTIVE"; // Set default status
    }

    
    
	public Showtime() {
		super();
		this.showtime_id = showtime_id;
		this.movie_id = movie_id;
		this.theatre_id = theatre_id;
		this.show_date = show_date;
		this.show_time = show_time;
		this.price = price;
		this.available_seats = available_seats;
		this.status = status;
		this.created_at = created_at;
		this.movieTitle = movieTitle;
		this.theatreName = theatreName;
		this.totalSeats = totalSeats;
	}



	public int getShowtime_id() {
		return showtime_id;
	}

	public void setShowtime_id(int showtime_id) {
		this.showtime_id = showtime_id;
	}

	public int getMovie_id() {
		return movie_id;
	}

	public void setMovie_id(int movie_id) {
		this.movie_id = movie_id;
	}

	public int getTheatre_id() {
		return theatre_id;
	}

	public void setTheatre_id(int theatre_id) {
		this.theatre_id = theatre_id;
	}

	public Date getShow_date() {
		return show_date;
	}

	public void setShow_date(Date date) {
		this.show_date = date;
	}

	public Time getShow_time() {
		return show_time;
	}

	public void setShow_time(Time show_time) {
		this.show_time = show_time;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public int getAvailable_seats() {
		return available_seats;
	}

	public void setAvailable_seats(int available_seats) {
		this.available_seats = available_seats;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Timestamp getCreated_at() {
		return created_at;
	}

	public void setCreated_at(Timestamp created_at) {
		this.created_at = created_at;
	}

	public String getMovieTitle() {
		return movieTitle;
	}

	public void setMovieTitle(String movieTitle) {
		this.movieTitle = movieTitle;
	}

	public String getTheatreName() {
		return theatreName;
	}

	public void setTheatreName(String theatreName) {
		this.theatreName = theatreName;
	}

	public int getTotalSeats() {
		return totalSeats;
	}

	public void setTotalSeats(int totalSeats) {
		this.totalSeats = totalSeats;
	}



	@Override
	public String toString() {
		return "Showtime [showtime_id=" + showtime_id + ", movie_id=" + movie_id + ", theatre_id=" + theatre_id
				+ ", show_date=" + show_date + ", show_time=" + show_time + ", price=" + price + ", available_seats="
				+ available_seats + ", status=" + status + ", created_at=" + created_at + ", movieTitle=" + movieTitle
				+ ", theatreName=" + theatreName + ", totalSeats=" + totalSeats + "]";
	}



	
    

    
}


