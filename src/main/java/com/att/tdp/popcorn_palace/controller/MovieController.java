package com.att.tdp.popcorn_palace.controller;

import com.att.tdp.popcorn_palace.model.Movie;
import com.att.tdp.popcorn_palace.service.MovieService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.util.List;

// This class will handle HTTP requests
@RestController
@RequestMapping("/movies")
@Validated
public class MovieController {

    private final MovieService movieService;

    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    @GetMapping("/all")
    public List<Movie> getAllMovies() {
        return movieService.getAllMovies();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.OK)  
    public Movie addMovie(@Valid @RequestBody Movie movie) {
        return movieService.addMovie(movie);
    }

    @PostMapping("/update/{movieTitle}")
    public ResponseEntity<Movie> updateMovie(@PathVariable String movieTitle, @Valid @RequestBody Movie updatedMovie) {
        Movie movie = movieService.updateMovie(movieTitle, updatedMovie);
        if (movie == null) {
            return ResponseEntity.notFound().build();  // 404 Not Found if movie doesn't exist
        }
        return ResponseEntity.ok(null);  
    }


    @DeleteMapping("/{movieTitle}")
    public ResponseEntity<String> deleteMovie(@PathVariable String movieTitle) {
        boolean deleted = movieService.deleteMovie(movieTitle);
        if (deleted) {
            return ResponseEntity.ok(null);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
    
}
