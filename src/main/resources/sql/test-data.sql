-- 1. Theater
INSERT INTO theater (name, location) VALUES
('PVR Forum Mall', 'Bangalore'),
('INOX Garuda Mall', 'Bangalore');

-- 2. Screen (depends on theater)
INSERT INTO screen (theater_id, screen_number, total_seats) VALUES
(1, 1, 100),
(1, 2, 80),
(2, 1, 120);

-- 3. Movie
INSERT INTO movie (title, duration) VALUES
('Inception', 148),
('Interstellar', 169),
('The Dark Knight', 152);

-- 4. Shows (depends on movie, screen)
INSERT INTO shows (movie_id, screen_id, show_time) VALUES
(1, 1, '2026-09-28 18:00:00'),
(2, 2, '2026-09-28 21:00:00'),
(3, 3, '2026-09-29 19:00:00');

-- 5. Seat (depends on screen)
INSERT INTO seat (screen_id, seat_number, seat_type) VALUES
(1, 'A1', 'REGULAR'),
(1, 'A2', 'REGULAR'),
(1, 'B1', 'PREMIUM');

-- 6. User
INSERT INTO user (name, email, password) VALUES
('Abbas', 'abbas@test.com', 'hashed_pw_1'),
('Test User', 'test@test.com', 'hashed_pw_2');

-- 7. Booking (depends on shows, user)
INSERT INTO booking (show_id, user_id, status, booking_time) VALUES
(1, 1, 'CONFIRMED', NOW());

-- 8. Booking_seat (depends on booking, seat)
INSERT INTO booking_seat (booking_id, seat_id, price) VALUES
(1, 1, 250.00),
(1, 2, 250.00);