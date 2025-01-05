package com.movie.model;

import java.math.BigDecimal;

import java.sql.Date;
import java.sql.Timestamp;

public class Movie {
    

	private int movieId;
    private String title;
    private String description;
    private String genre;
    private String duration;
    private Date releaseDate;
    private String status;
    private BigDecimal rating;
    private String imageUrl;
    private double price;
    private Timestamp createdAt;
    
    
	public Movie() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
	public Movie(int movieId, String title, String description, String genre, String duration, Date releaseDate,
			String status, BigDecimal rating, String imageUrl, double price, Timestamp createdAt) {
		super();
		this.movieId = movieId;
		this.title = title;
		this.description = description;
		this.genre = genre;
		this.duration = duration;
		this.releaseDate = releaseDate;
		this.status = status;
		this.rating = rating;
		this.imageUrl = imageUrl;
		this.price = price;
		this.createdAt = createdAt;
	}


	public int getMovieId() {
		return movieId;
	}


	public void setMovieId(int movieId) {
		this.movieId = movieId;
	}


	public String getTitle() {
		return title;
	}


	public void setTitle(String title) {
		this.title = title;
	}


	public String getDescription() {
		return description;
	}


	public void setDescription(String description) {
		this.description = description;
	}


	public String getGenre() {
		return genre;
	}


	public void setGenre(String genre) {
		this.genre = genre;
	}


	public String getDuration() {
		return duration;
	}


	public void setDuration(String duration) {
		this.duration = duration;
	}


	public Date getReleaseDate() {
		return releaseDate;
	}


	public void setReleaseDate(Date releaseDate) {
		this.releaseDate = releaseDate;
	}


	public String getStatus() {
		return status;
	}


	public void setStatus(String status) {
		this.status = status;
	}


	public BigDecimal getRating() {
		return rating;
	}


	public void setRating(BigDecimal rating) {
		this.rating = rating;
	}


	public String getImageUrl() {
		return imageUrl;
	}


	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}


	public double getPrice() {
		return price;
	}


	public void setPrice(double price) {
		this.price = price;
	}


	public Timestamp getCreatedAt() {
		return createdAt;
	}


	public void setCreatedAt(Timestamp createdAt) {
		this.createdAt = createdAt;
	}

    public double getRatingAsDouble() {
        return rating != null ? rating.doubleValue() : 0.0;
    }

	@Override
	public String toString() {
		return "Movie [movieId=" + movieId + ", title=" + title + ", description=" + description + ", genre=" + genre
				+ ", duration=" + duration + ", releaseDate=" + releaseDate + ", status=" + status + ", rating="
				+ rating + ", imageUrl=" + imageUrl + ", price=" + price + ", createdAt=" + createdAt + "]";
	}

    
    

}
