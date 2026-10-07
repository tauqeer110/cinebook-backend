package com.abbas.cinebook.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.abbas.cinebook.dto.BookingRequestDTO;
import com.abbas.cinebook.dto.BookingResponseDTO;
import com.abbas.cinebook.entity.Booking;
import com.abbas.cinebook.entity.BookingSeat;
import com.abbas.cinebook.entity.Seat;
import com.abbas.cinebook.entity.Show;
import com.abbas.cinebook.entity.ShowSeat;
import com.abbas.cinebook.entity.User;
import com.abbas.cinebook.enums.BookingStatus;
import com.abbas.cinebook.enums.SeatStatus;
import com.abbas.cinebook.exception.BadRequestException;
import com.abbas.cinebook.exception.ConflictException;
import com.abbas.cinebook.exception.ResourceNotFoundException;
import com.abbas.cinebook.repository.BookingRepository;
import com.abbas.cinebook.repository.BookingSeatRepository;
import com.abbas.cinebook.repository.ShowRepository;
import com.abbas.cinebook.repository.ShowSeatRepository;
import com.abbas.cinebook.repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@Service
@Transactional
@AllArgsConstructor
public class BookingService {

    private final BookingRepository bookingRepository;
    private final ShowRepository showRepository;
    private final UserRepository userRepository;

    private final BookingSeatRepository bookingSeatRepository;
    private final PricingService pricingService;
    private final ShowSeatRepository showSeatRepository;
    

    public BookingResponseDTO getBooking(int bookingId) {

        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking Does Not Exists"));

        int showId = booking.getShow().getId();

        List<BookingSeat> bookingSeats = bookingSeatRepository.findByBookingId(bookingId);
        
        double totalPrice = 0;
        List<String> seatNumbers = new ArrayList<>();

        for (BookingSeat bookingSeat : bookingSeats) {

            totalPrice += bookingSeat.getPrice();
            seatNumbers.add(bookingSeat.getSeat().getSeatNumber());
        }

         
        return createBookingResponse(
                booking,
                showId,
                totalPrice,
                seatNumbers
        );
    }

    public BookingResponseDTO cancelBooking(int bookingId) {

        // 1. Fetch the booking
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking " + bookingId + " Does Not Exist"));

        // 2. Prevent duplicate cancellations
        if (booking.getBookingStatus() == BookingStatus.CANCELLED) {
                throw new ConflictException("Booking has already been cancelled, cannot cancel twice");
        }

        // 3. Update the booking status
        booking.setBookingStatus(BookingStatus.CANCELLED);

        // 4. Fetch the seats linked to this booking
        List<BookingSeat> bookingSeats = bookingSeatRepository.findByBookingId(bookingId);

        double totalPrice = 0;
        List<String> seatNumbers = new ArrayList<>();

        // 5. Release seats back to inventory
        for (BookingSeat bookingSeat : bookingSeats) {

                int showId = booking.getShow().getId();
                int seatId = bookingSeat.getSeat().getId();

                ShowSeat showSeat = showSeatRepository.findByShowIdAndSeatId(showId, seatId)
                        .orElseThrow(() -> new ResourceNotFoundException("Seat mapping not found for seat ID: " + bookingSeat.getSeat().getId()));

                // Mark the seat as AVAILABLE again
                showSeat.setSeatStatus(SeatStatus.AVAILABLE);

                totalPrice += bookingSeat.getPrice();
                seatNumbers.add(bookingSeat.getSeat().getSeatNumber());

        }

        // 7. Return the response
        return createBookingResponse(
                booking,
                booking.getShow().getId(),
                totalPrice,
                seatNumbers
        );
    }

    public BookingResponseDTO createBooking(BookingRequestDTO dto) {

        Show show = getShow(dto.getShowId());
        User user = getUser(dto.getUserId());

        List<Seat> seats = validateAndLockSeats(dto.getSeatIds(), show);

        Booking booking = createNewBooking(show, user);

        double totalPrice = 0;
        List<String> seatNumbers = new ArrayList<>();
        List<BookingSeat> bookingSeats = new ArrayList<>();

        for (Seat seat : seats) {

            double price = pricingService.calculatePrice(seat, show);

            BookingSeat bookingSeat = createBookingSeat(booking, seat, price);

            totalPrice += price;
            seatNumbers.add(seat.getSeatNumber());
            bookingSeats.add(bookingSeat);
        }

        bookingSeatRepository.saveAll(bookingSeats);

        return createBookingResponse(
                booking,
                show.getId(),
                totalPrice,
                seatNumbers
        );
    }

    private Show getShow(int showId) {

        return showRepository.findById(showId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Show " + showId + " Does Not Exist"
                        )
                );
    }

    private User getUser(int userId) {

        return userRepository.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User " + userId + " Not Found"
                        )
                );
    }

    private List<Seat> validateAndLockSeats(List<Integer> seatIds, Show show) {

        List<Seat> seats = new ArrayList<>();

        for (int seatId : seatIds) {

                // 1. Fetch the ShowSeat record (which has @Version)
                ShowSeat showSeat = showSeatRepository.findByShowIdAndSeatId(show.getId(), seatId)
                        .orElseThrow(() -> new ResourceNotFoundException("Seat " + seatId + " is not mapped to this show"));

                // 2. Validate screen match
                validateSeatScreen(showSeat.getSeat(), show);

                // 3. Check availability
                if (showSeat.getSeatStatus() != SeatStatus.AVAILABLE) {
                    throw new ConflictException("Seat " + showSeat.getSeat().getSeatNumber() + " is already booked");
                }
        
                // 4. CRITICAL: Modify the entity so Hibernate triggers the version check on commit
                showSeat.setSeatStatus(SeatStatus.BOOKED);
                showSeatRepository.save(showSeat);

                seats.add(showSeat.getSeat());
        }

        return seats;
    }

    private void validateSeatScreen(Seat seat, Show show) {

        if (seat.getScreen().getId() != show.getScreen().getId()) {

            throw new BadRequestException(
                    "Seat " + seat.getId()
                            + " Does Not Belong to The Show's Screen"
            );
        }
    }

    private Booking createNewBooking(Show show, User user) {

        Booking booking = new Booking();

        booking.setBookingTime(LocalDateTime.now());
        booking.setBookingStatus(BookingStatus.CONFIRMED);
        booking.setShow(show);
        booking.setUser(user);

        return bookingRepository.save(booking);
    }

    private BookingSeat createBookingSeat(
            Booking booking,
            Seat seat,
            double price) {

        BookingSeat bookingSeat = new BookingSeat();

        bookingSeat.setPrice(price);
        bookingSeat.setSeat(seat);
        bookingSeat.setBooking(booking);

        return bookingSeat;
    }

    private BookingResponseDTO createBookingResponse(
            Booking booking,
            int showId,
            double totalPrice,
            List<String> seatNumbers) {

        BookingResponseDTO response = new BookingResponseDTO();

        response.setBookingStatus(
                booking.getBookingStatus()
        );

        response.setBookingId(booking.getId());
        response.setShowId(showId);
        response.setTotalPrice(totalPrice);
        response.setSeatNumbers(seatNumbers);

        return response;
    }
}
