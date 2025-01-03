package com.user.model;

import java.sql.Timestamp;

public class User {
    // Match exact database column names
    private int user_id;
    private String username;
    private String name;
    private String email;
    private String country;
    private String address;
    private String password;
    private String role;
    private Timestamp created_at;

    public User() {
        super();
        // TODO Auto-generated constructor stub
    }

    // Update constructor to match field names
    public User(int user_id, String username, String name, String email, 
               String country, String address, String password) {
        this.user_id = user_id;
        this.username = username;
        this.name = name;
        this.email = email;
        this.country = country;
        this.address = address;
        this.password = password;
        this.role = "USER"; // Set default role
    }

    // Update getter/setter names to match fields
    public int getUser_id() { return user_id; }
    public void setUser_id(int user_id) { this.user_id = user_id; }

    public Timestamp getCreated_at() { return created_at; }
    public void setCreated_at(Timestamp created_at) { this.created_at = created_at; }

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

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public boolean isAdmin() {
        return "ADMIN".equals(role);
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
        return "User [user_id=" + user_id + ", username=" + username + ", name=" + name + ", email=" + email + ", country=" + country + ", address=" + address + ", password=" + password + "]";
    }

}
