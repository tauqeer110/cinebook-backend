# CineBook

A movie ticket booking backend built with Spring Boot. It handles theaters, screens, shows, per-show pricing, and multi-seat bookings, and prevents double-booking when many users book the same seat at once.

## Tech Stack

Java 17, Spring Boot, Spring Data JPA (Hibernate), MySQL, Bean Validation, Spring Security (BCrypt password hashing only), Swagger UI (springdoc), Lombok, Maven.

## Features

- Create theaters, screens, movies, and shows
- Set show prices per seat type (REGULAR, PREMIUM, VIP)
- List seats of a screen
- Book multiple seats in one request, view a booking, cancel a booking
- User signup with BCrypt-hashed passwords
- Request validation and consistent error responses

## API Overview

| Method | Endpoint | Purpose |
|--------|----------|---------|
| POST | `/api/movies` | Create a movie |
| POST | `/api/theaters` | Create a theater |
| POST | `/api/screens` | Create a screen in a theater |
| POST | `/api/shows` | Create a show (movie + screen + time) |
| POST | `/api/shows/{showId}/pricing` | Set or update prices by seat type |
| GET | `/api/shows` | List shows that have pricing set |
| GET | `/api/shows/{showId}` | Get one show with its prices |
| GET | `/api/screens/{screenId}/seats` | List seats of a screen |
| POST | `/api/bookings` | Book one or more seats for a show |
| GET | `/api/bookings/{bookingId}` | View a booking |
| PUT | `/api/bookings/{bookingId}/cancel` | Cancel a booking |
| POST | `/api/users` | Sign up a user |
| GET | `/api/users/{userId}/bookings` | List a user's bookings |

Errors: `400` invalid input, `404` resource not found, `409` conflict (duplicate record or seat already booked).

## Design Decisions

- **Layered structure:** controller, service, repository. Request and response DTOs are used everywhere, so entities are never exposed.
- **Validation:** Bean Validation on request DTOs, with wrapper types (`Integer`, `Double`) so a missing field is different from an invalid one. `@Valid` covers request bodies and `@Validated` covers path variables.
- **Error handling:** one `@RestControllerAdvice` turns custom exceptions and validation failures into clean JSON responses without leaking internals.
- **Pricing:** stored per show and seat type, because a show already captures the movie, screen, theater, and time. Each booked seat stores its price at booking time, so later price changes do not alter old bookings.
- **Duplicate protection:** application-level checks give friendly `409` errors, and database unique constraints act as the last line of defense.
- **Concurrency:** a seat is claimed with a single conditional `UPDATE ... WHERE status = <available>`, and the number of affected rows decides who wins. The booking runs in one transaction. Tested with 20 parallel requests for the same seat: 1 booking succeeded, 19 got `409`.

## Run Locally

1. Install Java 17 and MySQL 8.
2. Create the database: `CREATE DATABASE movie_booking;`
3. Set your MySQL root password as an environment variable:
   - Windows: `setx DB_PASSWORD "your_password"` (restart your terminal or IDE afterwards)
   - Linux/macOS: `export DB_PASSWORD=your_password`
4. Start the app: `./mvnw spring-boot:run` (Windows: `mvnw.cmd spring-boot:run`)
5. Open Swagger UI: http://localhost:8080/swagger-ui/index.html

Tables are created automatically on startup. Optional sample data is in `src/main/resources/sql/test-data.sql` (run it manually in MySQL).

## Try the Concurrency Test

Use a seat that is still available, then fire 20 parallel booking requests:

```bash
URL=http://localhost:8080/api/bookings
BODY='{"showId":1,"userId":1,"seatIds":[3]}'

for i in $(seq 1 20); do
  curl -s -o /dev/null -w "%{http_code}\n" -X POST "$URL" \
    -H "Content-Type: application/json" -d "$BODY" &
done | sort | uniq -c
```

Expected output: one `201` and nineteen `409`.

## Current Limitations and Next Steps

- Endpoints are currently open. JWT authentication is planned.
- Planned: Redis seat holds with expiry and read caching, Kafka booking events, idempotency and rate limiting, Docker Compose with a load test, and a simple Vue.js UI.
