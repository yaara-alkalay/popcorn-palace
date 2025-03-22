package com.att.tdp.popcorn_palace.controller;

import com.att.tdp.popcorn_palace.model.Booking;
import com.att.tdp.popcorn_palace.service.BookingService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/bookings")
public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public Map<String, String> bookTicket(@Valid @RequestBody Booking bookingRequest) {
        Booking savedBooking = bookingService.bookTicket(bookingRequest);
        Map<String, String> response = new HashMap<>();
        response.put("bookingId", savedBooking.getUuid().toString());  
        return response;
    }
}
