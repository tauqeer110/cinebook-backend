package com.abbas.cinebook.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.abbas.cinebook.dto.BookingResponseDTO;
import com.abbas.cinebook.dto.UserRequestDTO;
import com.abbas.cinebook.dto.UserResponseDTO;
import com.abbas.cinebook.entity.Booking;
import com.abbas.cinebook.entity.BookingSeat;
import com.abbas.cinebook.entity.User;
import com.abbas.cinebook.exception.ConflictException;
import com.abbas.cinebook.exception.ResourceNotFoundException;
import com.abbas.cinebook.repository.BookingRepository;
import com.abbas.cinebook.repository.BookingSeatRepository;
import com.abbas.cinebook.repository.UserRepository;

import lombok.AllArgsConstructor;

@Service 
@AllArgsConstructor 
public class UserService {
    
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final BookingSeatRepository bookingSeatRepository;

    private final PasswordEncoder passwordEncoder;

    public UserResponseDTO createUser(UserRequestDTO userRequestDTO) {

        boolean isUserExists = userRepository.existsByEmail(userRequestDTO.getEmail());

        if(isUserExists) throw new ConflictException("User Already Exists");

        User user = new User();

        user.setEmail(userRequestDTO.getEmail());
        user.setName(userRequestDTO.getName());
        user.setPassword(
            passwordEncoder.encode(
                userRequestDTO.getPassword())
        );

        User svedUser = userRepository.save(user);

        UserResponseDTO userResponseDTO = new UserResponseDTO();

        userResponseDTO.setId(svedUser.getId());
        userResponseDTO.setEmail(svedUser.getEmail());
        userResponseDTO.setName(svedUser.getName());

        return userResponseDTO;
    }

    public List<BookingResponseDTO> getBookings(int userId) {

        userRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("User Does Not Exists"));
        
        List<Booking> bookings = bookingRepository.findByUserId(userId);

        List<BookingResponseDTO> bookingResponseDTOs = new ArrayList<>();

        
        for (Booking booking : bookings) {

            BookingResponseDTO bookingResponseDTO = createBookingResponseDTO(booking);
            bookingResponseDTOs.add(bookingResponseDTO);

        }

        return bookingResponseDTOs;

    }

    public BookingResponseDTO createBookingResponseDTO(Booking booking) {

        double totalPrice = 0;
        List<String> seatNumbers = new ArrayList<>();

        List<BookingSeat> bookingSeats = bookingSeatRepository.findByBookingId(booking.getId());

        for (BookingSeat bookingSeat : bookingSeats) {

            totalPrice += bookingSeat.getPrice();
            seatNumbers.add(bookingSeat.getSeat().getSeatNumber());

        }

        BookingResponseDTO bookingResponseDTO = new BookingResponseDTO();

        bookingResponseDTO.setBookingId(booking.getId());
        bookingResponseDTO.setBookingStatus(booking.getBookingStatus());
        bookingResponseDTO.setShowId(booking.getShow().getId());
        bookingResponseDTO.setTotalPrice(totalPrice);
        bookingResponseDTO.setSeatNumbers(seatNumbers);

        return bookingResponseDTO;
    }
}
