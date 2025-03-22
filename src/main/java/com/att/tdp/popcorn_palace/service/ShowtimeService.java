package com.att.tdp.popcorn_palace.service;

import com.att.tdp.popcorn_palace.model.Movie;
import com.att.tdp.popcorn_palace.model.Showtime;
import com.att.tdp.popcorn_palace.repository.MovieRepository;
import com.att.tdp.popcorn_palace.repository.ShowtimeRepository;
import com.att.tdp.popcorn_palace.exception.ShowtimeNotFoundException;
import com.att.tdp.popcorn_palace.exception.MovieNotFoundException;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class ShowtimeService {
    private final ShowtimeRepository showtimeRepository;
    private final MovieRepository movieRepository;

    public ShowtimeService(ShowtimeRepository showtimeRepository, MovieRepository movieRepository) {
        this.showtimeRepository = showtimeRepository;
        this.movieRepository = movieRepository;
    }

    public List<Showtime> getAllShowtimes() {
        return showtimeRepository.findAll();
    }

    public Showtime addShowtime(Showtime showtime) {
        // Fetch the Movie using the movieId
        Movie movie = movieRepository.findById(showtime.getMovieId())
                .orElseThrow(() ->  new MovieNotFoundException("Movie not found"));

        // Set the movie object in the Showtime
        showtime.setMovie(movie);

        // Check if the showtime overlaps
        boolean overlaps = showtimeRepository.existsByTheaterAndStartTimeBeforeAndEndTimeAfter(
                showtime.getTheater(), showtime.getEndTime(), showtime.getStartTime()
        );

        if (overlaps) {
            throw new RuntimeException("Showtime overlaps with an existing showtime in " + showtime.getTheater());
        }

        // Save and return the showtime
        return showtimeRepository.save(showtime);
    }


    public Showtime getShowtimeById(Long id) {
        Optional<Showtime> showtime = showtimeRepository.findById(id);
        if (showtime.isPresent()) {
            return showtime.get();
        } else {
            throw new ShowtimeNotFoundException("Showtime not found with ID: " + id);
        }
    }

    public Showtime updateShowtime(Long id, Showtime showtime) {
        Showtime existingShowtime = showtimeRepository.findById(id)
                .orElseThrow(() -> new ShowtimeNotFoundException("Showtime not found with ID: " + id));

        existingShowtime.setMovieId(showtime.getMovieId());  // Set movieId directly
        existingShowtime.setTheater(showtime.getTheater());
        existingShowtime.setStartTime(showtime.getStartTime());
        existingShowtime.setEndTime(showtime.getEndTime());
        existingShowtime.setPrice(showtime.getPrice());

        return showtimeRepository.save(existingShowtime);
    }

    public boolean deleteShowtime(Long id) {
        return showtimeRepository.findById(id).map(showtime->{showtimeRepository.delete(showtime);
        return true;})
        .orElseThrow(() -> new ShowtimeNotFoundException("Showtime not found with ID: " + id));
    }
}
