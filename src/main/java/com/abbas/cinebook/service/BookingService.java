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
import com.abbas.cinebook.entity.User;
import com.abbas.cinebook.enums.BookingStatus;
import com.abbas.cinebook.enums.SeatStatus;
import com.abbas.cinebook.exception.BadRequestException;
import com.abbas.cinebook.exception.ConflictException;
import com.abbas.cinebook.exception.ResourceNotFoundException;
import com.abbas.cinebook.repository.BookingRepository;
import com.abbas.cinebook.repository.BookingSeatRepository;
import com.abbas.cinebook.repository.SeatRepository;
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
    private final SeatRepository seatRepository;
    

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
        List<Integer> seatIds = new ArrayList<>();


        for (BookingSeat bookingSeat : bookingSeats) {
                seatIds.add(bookingSeat.getSeat().getId());
                seatNumbers.add(bookingSeat.getSeat().getSeatNumber());
                totalPrice += bookingSeat.getPrice();
        }

        // 6. Bulk atomic release of seats back to AVAILABLE in a single SQL query
        if (!seatIds.isEmpty()) {
                showSeatRepository.updateSeatStatusConditionally(
                        booking.getShow().getId(),
                        seatIds,
                        SeatStatus.BOOKED,
                        SeatStatus.AVAILABLE
                );
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

        // 1. Fetch physical seats to validate they belong to this screen and calculate price
        List<Seat> seats = seatRepository.findAllById(dto.getSeatIds());

        if (seats.size() != dto.getSeatIds().size()) {
                throw new ResourceNotFoundException("One or more physical seats not found");
        }

        for (Seat seat : seats) {
                validateSeatScreen(seat, show);
        }

        // 2. ATOMIC CONDITIONAL UPDATE: Attempt to reserve all seats at once
        int updatedRows = showSeatRepository.updateSeatStatusConditionally(
                show.getId(),
                dto.getSeatIds(),
                SeatStatus.AVAILABLE,
                SeatStatus.BOOKED
        );

        // If even ONE seat was already taken, updatedRows will not match requested seat count
        if (updatedRows != dto.getSeatIds().size()) {
                // Rolling back automatically frees any seats updated in this query
                throw new ConflictException("One or more selected seats are no longer available");
        }

        // 3. Create and persist booking records
        Booking booking = createNewBooking(show, user);

        double totalPrice = 0;
        List<String> seatNumbers = new ArrayList<>();
        List<BookingSeat> bookingSeats = new ArrayList<>();

        for (Seat seat : seats) {
                
                double price = pricingService.calculatePrice(seat, show);
                bookingSeats.add(createBookingSeat(booking, seat, price));
                totalPrice += price;
                seatNumbers.add(seat.getSeatNumber());
        }

        bookingSeatRepository.saveAll(bookingSeats);

        return createBookingResponse(booking, show.getId(), totalPrice, seatNumbers);

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
