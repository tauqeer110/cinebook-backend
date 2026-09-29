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
import com.abbas.cinebook.exception.BadRequestException;
import com.abbas.cinebook.exception.ConflictException;
import com.abbas.cinebook.exception.ResourceNotFoundException;
import com.abbas.cinebook.repository.BookingRepository;
import com.abbas.cinebook.repository.BookingSeatRepository;
import com.abbas.cinebook.repository.SeatRepository;
import com.abbas.cinebook.repository.ShowRepository;
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
    private final SeatRepository seatRepository;
    private final BookingSeatRepository bookingSeatRepository;
    private final PricingService pricingService;

    public BookingResponseDTO createBooking(BookingRequestDTO dto)
            throws BadRequestException, ConflictException {

        Show show = getShow(dto.getShowId());
        User user = getUser(dto.getUserId());

        List<Seat> seats = validateAndGetSeats(
                dto.getSeatIds(),
                show
        );

        Booking booking = createBooking(show, user);

        double totalPrice = 0;
        List<String> seatNumbers = new ArrayList<>();

        for (Seat seat : seats) {

            double price = pricingService.calculatePrice(seat, show);

            createBookingSeat(
                    booking,
                    seat,
                    price
            );

            totalPrice += price;
            seatNumbers.add(seat.getSeatNumber());
        }

        return createBookingResponse(
                booking,
                show,
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

    private List<Seat> validateAndGetSeats(
            List<Integer> seatIds,
            Show show)
            throws BadRequestException, ConflictException {

        List<Seat> seats = new ArrayList<>();

        for (int seatId : seatIds) {

            Seat seat = getSeat(seatId);

            validateSeatScreen(seat, show);

            validateSeatAvailability(
                    seatId,
                    show
            );

            seats.add(seat);
        }

        return seats;
    }

    private Seat getSeat(int seatId) {

        return seatRepository.findById(seatId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Seat " + seatId + " Does Not Exist"
                        )
                );
    }

    private void validateSeatScreen(
            Seat seat,
            Show show)
            throws BadRequestException {

        if (seat.getScreen().getId() != show.getScreen().getId()) {

            throw new BadRequestException(
                    "Seat " + seat.getId()
                            + " Does Not Belong to The Show's Screen"
            );
        }
    }

    private void validateSeatAvailability(
            int seatId,
            Show show)
            throws ConflictException {

        boolean isBooked =
                bookingSeatRepository.existsBySeatIdAndBooking_ShowId(
                        seatId,
                        show.getId()
                );

        if (isBooked) {

            throw new ConflictException(
                    "Seat " + seatId + " Is Already Booked"
            );
        }
    }

    private Booking createBooking(
            Show show,
            User user) {

        Booking booking = new Booking();

        booking.setBookingTime(LocalDateTime.now());
        booking.setBookingStatus(BookingStatus.CONFIRMED);
        booking.setShow(show);
        booking.setUser(user);

        return bookingRepository.save(booking);
    }

    private void createBookingSeat(
            Booking booking,
            Seat seat,
            double price) {

        BookingSeat bookingSeat = new BookingSeat();

        bookingSeat.setPrice(price);
        bookingSeat.setSeat(seat);
        bookingSeat.setBooking(booking);

        bookingSeatRepository.save(bookingSeat);
    }

    private BookingResponseDTO createBookingResponse(
            Booking booking,
            Show show,
            double totalPrice,
            List<String> seatNumbers) {

        BookingResponseDTO response = new BookingResponseDTO();

        response.setBookingStatus(
                booking.getBookingStatus()
        );

        response.setShowId(show.getId());
        response.setTotalPrice(totalPrice);
        response.setSeatNumbers(seatNumbers);

        return response;
    }
}
