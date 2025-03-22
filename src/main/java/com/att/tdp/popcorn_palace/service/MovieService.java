package com.att.tdp.popcorn_palace.service;

import com.att.tdp.popcorn_palace.model.Movie;
import com.att.tdp.popcorn_palace.repository.MovieRepository;
import org.springframework.stereotype.Service;
import com.att.tdp.popcorn_palace.exception.MovieNotFoundException;
import java.util.List;
import java.util.Optional;

@Service
public class MovieService {

    private final MovieRepository movieRepository;

    public MovieService(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    public List<Movie> getAllMovies() {
        return movieRepository.findAll();
    }

    public Movie addMovie(Movie movie) {
        // Check if a movie with the same title already exists
        Optional<Movie> existingMovie = movieRepository.findByTitle(movie.getTitle());
    
        // If the movie already exists, throw an exception or return a message
        if (existingMovie.isPresent()) {
            throw new RuntimeException("Movie with title " + movie.getTitle() + " already exists.");
        }
    
        // Save the new movie if it doesn't already exist
        return movieRepository.save(movie);
    }
    
    public Movie updateMovie(String movieTitle, Movie updatedMovie) {
        Optional<Movie> movieOptional = movieRepository.findByTitle(movieTitle);
        if (movieOptional.isPresent()) {
            Movie movie = movieOptional.get();
            movie.setTitle(updatedMovie.getTitle());
            movie.setGenre(updatedMovie.getGenre());
            movie.setDuration(updatedMovie.getDuration());
            movie.setRating(updatedMovie.getRating());
            movie.setReleaseYear(updatedMovie.getReleaseYear());
            return movieRepository.save(movie);
        }
        throw new MovieNotFoundException("Movie with title " + movieTitle + " not found");
    }
    public Movie getMovieById(long id) {
        return movieRepository.findById(id)
                .orElseThrow(() -> new MovieNotFoundException("Movie not found with ID: " + id));  
    }

    // Delete a movie by title
    public boolean deleteMovie(String movieTitle) {
        return movieRepository.findByTitle(movieTitle)
            .map(movie -> {
                movieRepository.delete(movie);
                return true;
            })
            .orElseThrow(() -> new MovieNotFoundException("Movie with title " + movieTitle + " not found"));
    }
}
