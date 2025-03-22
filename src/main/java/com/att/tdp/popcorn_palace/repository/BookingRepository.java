package com.att.tdp.popcorn_palace.repository;

import com.att.tdp.popcorn_palace.model.Booking;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookingRepository extends JpaRepository<Booking, UUID> {
    // Custom query to check if a seat is already booked for a given showtime
    boolean existsByShowtimeIdAndSeatNumber(long showtimeId, int seatNumber);
}