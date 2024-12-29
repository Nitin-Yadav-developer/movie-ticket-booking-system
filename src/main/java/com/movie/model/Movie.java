package com.movie.model;

import java.sql.Timestamp;

public class Movie {
    private int movieId;  // Changed from id to movieId
    private String title;
    private String description;
    private String genre;
    private double rating;
    private String imageUrl;
    private double price;
    private Timestamp createdAt;  // Add this field
    
	public Movie() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Movie(int movieId, String title, String description, String genre, double rating, String imageUrl, double price) {
		super();
		this.movieId = movieId;
		this.title = title;
		this.description = description;
		this.genre = genre;
		this.rating = rating;
		this.imageUrl = imageUrl;
		this.price = price;
	}

	public Movie(int movieId, String title, String description, String genre, 
                double rating, String imageUrl, double price, Timestamp createdAt) {
        this.movieId = movieId;
        this.title = title;
        this.description = description;
        this.genre = genre;
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

	public double getRating() {
		return rating;
	}

	public void setRating(double rating) {
        // Ensure rating is between 0 and 10
        this.rating = Math.min(Math.max(rating, 0), 10);
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
        // Ensure price is not negative
        this.price = Math.max(price, 0);
    }

	public Timestamp getCreatedAt() { 
		return createdAt; 
	}

	public void setCreatedAt(Timestamp createdAt) { 
		this.createdAt = createdAt; 
	}

	@Override
	public String toString() {
		return "Movie [movieId=" + movieId + ", title=" + title + ", description=" + description + ", genre=" + genre
				+ ", rating=" + rating + ", imageUrl=" + imageUrl + ", price=" + price + ", createdAt=" + createdAt + "]";
	}
    
	
    
    
    // Add constructors, getters, and setters
}