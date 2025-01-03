package com.theatre.model;

import java.sql.Timestamp;

public class Theatre {

    // Match exact database column names
    private int theatre_id;
    private String name;
    private String location;
    private int total_seats;
    private String status;
    private Timestamp created_at;

    public Theatre() {
        this.status = "ACTIVE"; // Set default status
    }

    public Theatre(int theatre_id, String name, String location, int total_seats) {
        this.theatre_id = theatre_id;
        this.name = name;
        this.location = location;
        this.total_seats = total_seats;
        this.status = "ACTIVE";
    }

    // Update getters/setters to match column names
    public int getTheatre_id() { return theatre_id; }
    public void setTheatre_id(int theatre_id) { this.theatre_id = theatre_id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public int getTotal_seats() { return total_seats; }
    public void setTotal_seats(int total_seats) { this.total_seats = total_seats; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Timestamp getCreated_at() { return created_at; }
    public void setCreated_at(Timestamp created_at) { this.created_at = created_at; }

    @Override
    public String toString() {
        return "Theatre [theatreId=" + theatre_id + ", name=" + name + ", location=" + location + ", total_seats="
                + total_seats + ", status=" + status + ", created_at=" + created_at + "]";
    }
}
