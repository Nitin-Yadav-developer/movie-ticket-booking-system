package com.theatre.model;

public class Theatre {

	
	
	
	    private int theatreId;
	    private String name;
	    private String location;
	    private int totalseats;
	    
	    

	    public Theatre() {
			super();
			// TODO Auto-generated constructor stub
		}
	    
	    
		

		public Theatre(int theatreId, String name, String location, int totalseats) {
			super();
			this.theatreId = theatreId;
			this.name = name;
			this.location = location;
			this.totalseats = totalseats;
		}


		// Getters and Setters
	    public int getTheatreId() { return theatreId; }
	    public void setTheatreId(int theatreId) { this.theatreId = theatreId; }

	    public String getName() { return name; }
	    public void setName(String name) { this.name = name; }

	    public String getLocation() { return location; }
	    public void setLocation(String location) { this.location = location; }


		public int getToatalseats() {
			return totalseats;
		}




		public void setToatalseats(int toatalseats) {
			this.totalseats = toatalseats;
		}



		

		@Override
		public String toString() {
			return "Theatre [theatreId=" + theatreId + ", name=" + name + ", location=" + location + ", toatalseats="
					+ totalseats + "]";
		}




		


		
	    
	    
	}
