package com.att.tdp.popcorn_palace.controller;

import com.att.tdp.popcorn_palace.model.Movie;
import com.att.tdp.popcorn_palace.model.Showtime;
import com.att.tdp.popcorn_palace.service.MovieService;
import com.att.tdp.popcorn_palace.service.ShowtimeService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/showtimes")
public class ShowtimeController {
    private final ShowtimeService showtimeService;
    private final MovieService movieService;

   
    public ShowtimeController(ShowtimeService showtimeService, MovieService movieService) {
        this.showtimeService = showtimeService;
        this.movieService = movieService;
    }

    @PostMapping
    public ResponseEntity<?> addShowtime(@Valid @RequestBody Showtime showtime, BindingResult result) {
        if (result.hasErrors()) {
            // Extract validation errors and return a response
            List<String> errors = result.getAllErrors().stream()
                .map(error -> error.getDefaultMessage())
                .toList();
            return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
        }

        try {
            // Fetch the Movie entity based on the movieId
            Movie movie = movieService.getMovieById(showtime.getMovieId());
            if (movie == null) {
                return new ResponseEntity<>("Movie not found", HttpStatus.NOT_FOUND);
            }

            // Set the movie in the showtime object
            showtime.setMovie(movie);

            // Now the Showtime object has the actual Movie entity
            Showtime createdShowtime = showtimeService.addShowtime(showtime);
            return new ResponseEntity<>(createdShowtime, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Showtime> getShowtimeById(@PathVariable Long id) {
        Showtime showtime = showtimeService.getShowtimeById(id);
        if (showtime == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);  // 404 Not Found if not found
        }
        return new ResponseEntity<>(showtime, HttpStatus.OK);  // 200 OK
    }
    
    // Update a showtime 
    @PostMapping("/update/{id}")
    public ResponseEntity<?> updateShowtime(@PathVariable Long id, @Valid @RequestBody Showtime showtime, BindingResult result) {
        // Handle validation errors
        if (result.hasErrors()) {
            List<String> errors = result.getAllErrors().stream()
                .map(error -> error.getDefaultMessage())
                .collect(Collectors.toList());
            return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
        }

        Showtime updatedShowtime = showtimeService.updateShowtime(id, showtime);
        if (updatedShowtime == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);  
        }

        return new ResponseEntity<>(HttpStatus.OK);  
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteShowtime(@PathVariable Long id) {
        boolean deleted = showtimeService.deleteShowtime(id);
        if(deleted){
            return ResponseEntity.ok(null);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error: Showtime not found");
        }
    }

}
