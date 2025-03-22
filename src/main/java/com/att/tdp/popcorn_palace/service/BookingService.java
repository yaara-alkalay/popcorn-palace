package com.att.tdp.popcorn_palace.service;

import com.att.tdp.popcorn_palace.model.Booking;
import com.att.tdp.popcorn_palace.model.Showtime;
import com.att.tdp.popcorn_palace.repository.BookingRepository;
import com.att.tdp.popcorn_palace.repository.ShowtimeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.att.tdp.popcorn_palace.exception.ShowtimeNotFoundException;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;
    private final ShowtimeRepository showtimeRepository;

    @Autowired
    public BookingService(BookingRepository bookingRepository, ShowtimeRepository showtimeRepository) {
        this.bookingRepository = bookingRepository;
        this.showtimeRepository = showtimeRepository;
    }

    public Booking bookTicket(Booking bookingRequest) {
        // Fetch the Showtime object using the showtimeId from the bookingRequest
        Showtime showtime = showtimeRepository.findById(bookingRequest.getShowtimeId())
                .orElseThrow(() -> new ShowtimeNotFoundException(null));

        // Check if the seat is already booked for the given showtime
        boolean isSeatBooked = bookingRepository.existsByShowtimeIdAndSeatNumber(bookingRequest.getShowtimeId(), bookingRequest.getSeatNumber());
        if (isSeatBooked) {
            throw new RuntimeException("Seat number " + bookingRequest.getSeatNumber() + " is already booked for this showtime.");
        }

        // Create and save the new booking
        bookingRequest.setShowtime(showtime); // Link the showtime to the booking
        return bookingRepository.save(bookingRequest); // Save the booking and return it
    }
}
