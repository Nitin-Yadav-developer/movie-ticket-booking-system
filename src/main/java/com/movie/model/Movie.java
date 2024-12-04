package com.movie.model;


public class Movie {
    private int id;
    private String title;
    private String description;
    private String genre;
    private double rating;
    private String imageUrl;
    private double price;
    
	public Movie() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Movie(int id, String title, String description, String genre, double rating, String imageUrl, double price) {
		super();
		this.id = id;
		this.title = title;
		this.description = description;
		this.genre = genre;
		this.rating = rating;
		this.imageUrl = imageUrl;
		this.price = price;
	}
	

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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

	@Override
	public String toString() {
		return "Movie [id=" + id + ", title=" + title + ", description=" + description + ", genre=" + genre
				+ ", rating=" + rating + ", imageUrl=" + imageUrl + ", price=" + price + "]";
	}
    
	
    
    
    // Add constructors, getters, and setters
}