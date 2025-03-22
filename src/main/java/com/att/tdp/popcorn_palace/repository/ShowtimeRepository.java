package com.att.tdp.popcorn_palace.repository;

import com.att.tdp.popcorn_palace.model.Movie;
import com.att.tdp.popcorn_palace.model.Showtime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShowtimeRepository extends JpaRepository<Showtime, Long> {

    // Custom method to check if a showtime exists for a given theater, where the start time is before the given time 
    // and the end time is after the given time. Returns true if such a showtime exists, otherwise false.
    boolean existsByTheaterAndStartTimeBeforeAndEndTimeAfter(
        String theater, 
        java.time.LocalDateTime endTime, 
        java.time.LocalDateTime startTime
    );

    // Custom method to delete all showtimes associated with a specific movie. 
    // This method will cascade the delete operation for all associated showtimes of the given movie.
    void deleteByMovie(Movie movie);

     // Custom method to find all showtimes for a specific movie. It returns a list of Showtime objects for the provided Movie.
    public List<Showtime> findByMovie(Movie movie);
}
