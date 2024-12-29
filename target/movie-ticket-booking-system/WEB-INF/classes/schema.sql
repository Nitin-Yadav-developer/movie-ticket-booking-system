CREATE DATABASE IF NOT EXISTS movie_ticket_booking_system;
USE movie_ticket_booking_system;

CREATE TABLE IF NOT EXISTS showtimes (
    showtimeId INT AUTO_INCREMENT PRIMARY KEY,
    movieId INT NOT NULL,
    theatreId INT NOT NULL,
    showtime TIMESTAMP NOT NULL,
    showDate DATE NOT NULL
);

-- Insert 10 sample data entries
INSERT INTO showtimes (movieId, theatreId, showtime, showDate) VALUES
(1, 1, '2023-10-01 10:00:00', '2023-10-01'),
(2, 1, '2023-10-01 13:00:00', '2023-10-01'),
(3, 2, '2023-10-01 16:00:00', '2023-10-01'),
(4, 2, '2023-10-01 19:00:00', '2023-10-01'),
(5, 3, '2023-10-02 10:00:00', '2023-10-02'),
(6, 3, '2023-10-02 13:00:00', '2023-10-02'),
(7, 4, '2023-10-02 16:00:00', '2023-10-02'),
(8, 4, '2023-10-02 19:00:00', '2023-10-02'),
(9, 5, '2023-10-03 10:00:00', '2023-10-03'),
(10, 5, '2023-10-03 13:00:00', '2023-10-03');
