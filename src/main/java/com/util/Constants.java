package com.util;

public class Constants {
    // Database configuration
    public static final String DB_DRIVER = "com.mysql.cj.jdbc.Driver";
    public static final String DB_URL = "jdbc:mysql://localhost:3306/mtb";
    public static final String DB_USERNAME = "root";
    public static final String DB_PASSWORD = "Nitin@1513";

    // Movie status
    public static final String MOVIE_STATUS_ACTIVE = "ACTIVE";
    public static final String MOVIE_STATUS_INACTIVE = "INACTIVE";

    // Booking status
    public static final String BOOKING_STATUS_PENDING = "PENDING";
    public static final String BOOKING_STATUS_CONFIRMED = "CONFIRMED";
    public static final String BOOKING_STATUS_CANCELLED = "CANCELLED";

    // User roles
    public static final String ROLE_ADMIN = "ADMIN";
    public static final String ROLE_USER = "USER";

    // Common SQL queries
    public static final String SELECT_ALL_MOVIES = "SELECT * FROM movies";
    public static final String SELECT_ALL_USERS = "SELECT * FROM users";
    public static final String SELECT_ALL_BOOKINGS = "SELECT * FROM bookings";
    public static final String SELECT_ALL_THEATRES = "SELECT * FROM theatres";
    public static final String SELECT_ALL_SHOWTIMES = "SELECT * FROM showtimes";

    // Error messages
    public static final String ERROR_DB_CONNECTION = "Database connection failed";
    public static final String ERROR_DB_DRIVER = "Database driver not found";
    public static final String ERROR_INVALID_CREDENTIALS = "Invalid email or password";
    public static final String ERROR_REGISTRATION_FAILED = "Registration failed";
    public static final String ERROR_LOGIN_FAILED = "Login failed";
    public static final String ERROR_ACCESS_DENIED = "Access denied. Administrator privileges required.";
    public static final String ERROR_INVALID_INPUT = "Invalid input parameters";

    // Success messages
    public static final String SUCCESS_REGISTRATION = "Registration successful";
    public static final String SUCCESS_LOGIN = "Login successful";
    public static final String SUCCESS_BOOKING = "Booking confirmed successfully";
    public static final String SUCCESS_UPDATE = "Update successful";
    public static final String SUCCESS_DELETE = "Delete successful";

    // Session attributes
    public static final String SESSION_USER = "user";
    public static final String SESSION_USER_ID = "userId";
    public static final String SESSION_USERNAME = "username";
    public static final String SESSION_IS_ADMIN = "isAdmin";

    // Pagination
    public static final int ITEMS_PER_PAGE = 10;
    public static final int DEFAULT_PAGE_SIZE = 20;

    // Date formats
    public static final String DATE_FORMAT = "yyyy-MM-dd";
    public static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    // File paths and upload settings
    public static final String UPLOAD_DIRECTORY = "uploads";
    public static final String MOVIE_IMAGES_DIR = UPLOAD_DIRECTORY + "/movies";
    public static final int MAX_FILE_SIZE = 5 * 1024 * 1024; // 5MB

    // Default values
    public static final double DEFAULT_TICKET_PRICE = 10.0;
    public static final String DEFAULT_CURRENCY = "INR";
    public static final String DEFAULT_LANGUAGE = "en";

    // Private constructor to prevent instantiation
    private Constants() {
        // Private constructor to hide the implicit public one
    }
}
