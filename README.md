# Movie Ticket Booking System
 
  This repository hosts the code for an Online Movie Ticket Booking System built in Java. 
  This application allows users to browse movies, check available showtimes, and book tickets online. The project follows a structured MVC architecture and is designed to provide a seamless booking experience.
  A web-based application that allows users to browse movies and book tickets online. Built with Java Servlets, JSP, and MySQL.

## Features
   ### Both Admin and User Features

                 - User Authentication
                    - Register new account
                    - Login/Logout
                    - Profile management
                    - Update Profile
                    
                 - Admin Authentication
                   - Admin Login
                   - Admin Dashboard for management of movies, shows, theatres, users, and bookings

                - Movie Management
                  - Browse available movies
                  - Search movies
                  - View movie details
                  - Real-time seat availability

                - Booking System
                  - Select show time and seats
                  - Book multiple tickets
                  - View booking history
                  - Ticket download
                  
                - Showtime Management
                  - Browse showtime
                  - Book ticket for that Showtime
                  - Browse Particular movie showtime
                  
                - Theatre Management 
                 - Add a new Theatre
                 - Delete a Theatre 
                 - Update a theatre
                

## Technology Stack

   ### Frontend
    - JSP (JavaServer Pages)
    - HTML5
    - CSS3
    - JavaScript
    - Bootstrap 4.5.2

   ### Backend
    - Java Model classes
    - Java DAO classes
    - Java Servlets
    - JDBC
    - MySQL 
            

   ### Tools & Libraries
    - Maven
    - jQuery
    - Font Awesome
    - MySQL Connector/J

## Prerequisites

    - JDK 11 or higher
    - Apache Tomcat 10.x or higher
    - MySQL 8.0
    - Maven 5.x
    - IDE (Eclipse/IntelliJ IDEA)

# Database Setup

   ### 1. Create a new MySQL database:
    -Follow schema.sql
                   
   ### 2.Create the required tables:
    -Follow schema.sql



# Installation & Setup
   ### 1.Clone the repository:
   !git clone https://github.com/Nitin-Yadav-developer/movie-ticket-booking-system.git

  ### 2.Configure database connection:

    -Open src/main/java/com/util/Constants.java
    -Update the database URL, username, and password according to yours

 ### 3.Build the project:
    mvn clean install
 ### 4.Deploy to Tomcat:

    Copy the WAR file from target/movie-ticket-booking-system.war to Tomcat's webapps directory
    Start Tomcat server
 ### 5.Access the application:
    http://localhost:8080/movie-ticket-booking-system
 # Project Structure
       movie-ticket-booking-system/
       ├── src/
       │   ├── main/
       │   │   ├── java/
       │   │   │   ├── com/
       │   │   │   │   ├── controller/
       │   │   │   │   ├── booking/
       │   │   │   │   |  |___ model/
       │   │   │   │   |  |___ dao/
       │   │   │   │   |___ User/
       │   │   │   │   |  |__ model/
       │   │   │   │   |  |__ dao/
       │   │   │   │   ├── theatre/
       │   │   │   │   |  |___ model/
       │   │   │   │   |  |___ dao/
       │   │   │   │   ├── showtime/
       │   │   │   │   |  |___ model/
       │   │   │   │   |  |___ dao/
       │   │   │   │   ├── movie/
       │   │   │   │   |  |___ model/
       │   │   │   │   |  |___ dao/
       │   │   │   │   └── filter/
       │   │   ├── webapp/
       │   │   │   ├── css/
       │   │   │   ├── js/
       │   │   │   ├── WEB-INF/
       │   │   │   └── *.jsp
       │   │   └── resources/
       │   └── test/
       ├── pom.xml
       └── README.md
## User Login, Booking a ticket and Download Ticket.
 ### Click on login at top right of the page.
   ![Screenshot 2025-01-03 144750](https://github.com/user-attachments/assets/13a71504-bdea-4cc8-9058-533f5e4e4374)
 ### Login page will appear Enter your email and password.
   ![Screenshot 2025-01-03 141311](https://github.com/user-attachments/assets/1e516cc6-85f8-4ee8-b9bd-3f60d3420ca5)
 ### Then select the movie which you want to book.
   ![Screenshot 2025-01-03 141018](https://github.com/user-attachments/assets/d5a2588b-718c-4d7c-b5a2-8695a7392a86)
 ### Then all the showtimes will appear related to that movie, Select any of them which you want. 
   -click Book Now
   ![Screenshot 2025-01-03 141026](https://github.com/user-attachments/assets/14825502-77cb-4e14-a709-99f31eda5224)
 ### Then select the seats which you want to book.
   ![Screenshot 2025-01-03 141044](https://github.com/user-attachments/assets/94be0877-876b-4357-92c7-0355ab37ba63)
 ### Then do the payment.
   ![Screenshot 2025-01-03 141103](https://github.com/user-attachments/assets/83527b5f-1cf8-4dec-b024-7ee7cab04c6a)
 ### After that booking succesful message will appear and your ticket will be genrated.
   ![Screenshot 2025-01-03 141109](https://github.com/user-attachments/assets/42ba3c2d-8827-47ff-a422-991831dbda7f)
 ### Download Your Ticket.
   user can download his ticket from my booking section also if he lost his ticket and also can see his past bookings.
   ![Screenshot 2025-01-03 141120](https://github.com/user-attachments/assets/5df0196b-ad3f-4ae4-8dd2-4ebb61c0607c)
 ### Sample Downloaded Ticket
   ![high-quality-image (8)](https://github.com/user-attachments/assets/30ed1b9b-bdf8-4f61-be99-f0bedf3e7c88)
## Admin Login and Management.
  ### Admin Login.
   ![Screenshot 2025-01-03 141325](https://github.com/user-attachments/assets/9a2fa5f0-6442-4689-84b1-8e8216b4145b)
  ### Admin Dashboard.
   ![Screenshot 2025-01-03 151934](https://github.com/user-attachments/assets/f7b42c4d-200c-4654-904f-2dda7f23c274)
  ### Movie Management.
   ![Screenshot 2025-01-03 141347](https://github.com/user-attachments/assets/f20c1ed8-a5f0-4bd4-b832-ce49d3576a97)
  ### Show Management.
   ![Screenshot 2025-01-03 141354](https://github.com/user-attachments/assets/0253cae9-77d7-4628-aecf-5a53964b6fe6)
  ### Booking Management
   ![Screenshot 2025-01-03 141403](https://github.com/user-attachments/assets/51216da3-68b7-499a-9d4c-1656ee4ca1f1)
  ### User Management
   ![Screenshot 2025-01-03 141410](https://github.com/user-attachments/assets/f8cae0e6-0335-4d74-a754-f8ed71de8f29)
  ### Theatre Management
   ![Screenshot 2025-01-03 141421](https://github.com/user-attachments/assets/efa1f417-5108-46bc-a3e4-c4e0102f3a7d)

## All other  pages.
 ### Homepage.
   ![Screenshot 2025-01-03 140859](https://github.com/user-attachments/assets/f068a6e4-4356-4458-ae2f-fb83bfd43a13)
 ### Movies Page
   ![Screenshot 2025-01-03 140658](https://github.com/user-attachments/assets/76c42a3c-23db-4cb7-96ff-f90568c665b1)
 ### Showtimes Page
   ![Screenshot 2025-01-03 140922](https://github.com/user-attachments/assets/aa5d2409-42ec-410b-9832-392dba0979b9)
 ### User Profile page.
   ![Screenshot 2025-01-03 140942](https://github.com/user-attachments/assets/b10b376f-44c3-43ef-a63d-80791311f886)
 ### My Bookings.
   ![Screenshot 2025-01-03 140954](https://github.com/user-attachments/assets/2a11c437-34d7-41b8-af46-114ee62742e5)
