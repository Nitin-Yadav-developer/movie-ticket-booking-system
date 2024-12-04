# Movie Ticket Booking System
 ## This Project is underdevelopment.
  This repository hosts the code for an Online Movie Ticket Booking System built in Java. 
  This application allows users to browse movies, check available showtimes, and book tickets online. The project follows a structured MVC architecture and is designed to provide a seamless booking experience.
  A web-based application that allows users to browse movies and book tickets online. Built with Java Servlets, JSP, and MySQL.

## Features

                 - User Authentication
                    - Register new account
                    - Login/Logout
                    - Profile management

                - Movie Management
                  - Browse available movies
                  - Search movies
                  - View movie details
                  - Real-time seat availability

                - Booking System
                  - Select show time and seats
                  - Book multiple tickets
                  - View booking history

## Technology Stack

         - **Frontend**
             - JSP (JavaServer Pages)
             - HTML5
             - CSS3
             - JavaScript
             - Bootstrap 4.5.2

        - **Backend**
            - Java Servlets
            - JDBC
            - MySQL 8.0

       - **Tools & Libraries**
            - Maven
            - jQuery
           - Font Awesome
           - MySQL Connector/J

## Prerequisites

               - JDK 11 or higher
               - Apache Tomcat 9.0 or higher
               - MySQL 8.0
               - Maven 3.x
               - IDE (Eclipse/IntelliJ IDEA)

## Database Setup

         1. Create a new MySQL database:
                    -Follow schema.sql
                   
                   



# Installation & Setup
   ### 1.Clone the repository:
        git clone https://github.com/Nitin-Yadav-developer/movie-ticket-booking-system.git

  ### 2.Configure database connection:

                  -Open src/main/java/com/user/dao/UserDao.java
                  -Update the database URL, username, and password

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
       │   │   │   │   ├── user/
       │   │   │   │   └── util/
       │   │   ├── webapp/
       │   │   │   ├── css/
       │   │   │   ├── js/
       │   │   │   ├── WEB-INF/
       │   │   │   └── *.jsp
       │   │   └── resources/
       │   └── test/
       ├── pom.xml
       └── README.md
## Contact
## Nitin Yadav 

## Project Link: https://github.com/Nitin-Yadav-developer/movie-ticket-booking-system
