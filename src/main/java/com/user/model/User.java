package com.user.model;

import java.sql.Timestamp;

public class User {
    // Rename id to userId to match database
    private int userId;
    private String username;
    private String name;
    private String email;
    private String country;
    private String address;
    private String password;
    private Timestamp createdAt;  // Add timestamp field

    public User() {
        super();
        // TODO Auto-generated constructor stub
    }

    // Update constructor
    public User(int userId, String username, String name, String email, String country, String address, String password) {
        this.userId = userId;
        this.username = username;
        this.name = name;
        this.email = email;
        this.country = country;
        this.address = address;
        this.password = password;
    }

    // Add getter/setter for userId and createdAt
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public boolean isValidEmail() {
        String emailPattern = "^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$";
        return this.email.matches(emailPattern);
    }

    public boolean isValidPassword() {
        return this.password.length() >= 6;
    }

    @Override
    public String toString() {
        return "User [userId=" + userId + ", username=" + username + ", name=" + name + ", email=" + email + ", country=" + country + ", address=" + address + ", password=" + password + "]";
    }

}
