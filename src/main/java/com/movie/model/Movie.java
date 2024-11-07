package com.movie.model;

public class Movie {

	
	
	    private int movieId;
	    private String title;
	    private String genre;
	    private int duration; // in minutes

	    
	    
	    public Movie() {
			super();
			// TODO Auto-generated constructor stub
		}
	    
	    
		public Movie(int movieId, String title, String genre, int duration) {
			super();
			this.movieId = movieId;
			this.title = title;
			this.genre = genre;
			this.duration = duration;
		}


		// Getters and Setters
	    public int getMovieId() { return movieId; }
	    public void setMovieId(int movieId) { this.movieId = movieId; }

	    public String getTitle() { return title; }
	    public void setTitle(String title) { this.title = title; }

	    public String getGenre() { return genre; }
	    public void setGenre(String genre) { this.genre = genre; }

	    public int getDuration() { return duration; }
	    public void setDuration(int duration) { this.duration = duration; }


		@Override
		public String toString() {
			return "Movie [movieId=" + movieId + ", title=" + title + ", genre=" + genre + ", duration=" + duration
					+ "]";
		}
	
	
}
