package com.att.tdp.popcorn_palace.model;

import java.util.List;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;

// The Movie entity represents a movie in the system and is mapped to a database table
@Entity
public class Movie {

    // Unique identifier for each movie in the database. 
    // The @Id annotation marks this field as the primary key, 
    // and @GeneratedValue(strategy = GenerationType.IDENTITY) indicates that the id will be automatically generated by the database.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Title is required")
    @Size(max = 255, message = "Title must be at most 255 characters")
    private String title;

    @NotBlank(message = "Genre is required")
    @Size(max = 100, message = "Genre must be at most 100 characters")
    private String genre;

    @Min(value = 1, message = "Duration must be at least 1 minute")
    @Max(value = 500, message = "Duration must be at most 500 minutes")
    private int duration;

    @NotNull
    @Min(value = 0, message = "Min rating is 0")
    @Max(value = 10, message = "Max rating is 10")
    private double rating;

    @Min(value = 1888, message = "Release year must be after 1888") 
    @Max(value = 2025, message = "Release year must be realistic")
    private int releaseYear;

    // A movie can have multiple showtimes. 
    // The @OneToMany annotation indicates a one-to-many relationship with the Showtime entity.
    // CascadeType.ALL means that any operation on a Movie (like delete or persist) will also affect the related showtimes.
    // orphanRemoval = true ensures that when a Showtime is removed from the list, it will be deleted from the database as well.
    @OneToMany(mappedBy = "movie", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Showtime> showtimes;  


    public Movie() {}

    
    public Movie(String title, String genre, int duration, double rating, int releaseYear) {
        this.title = title;
        this.genre = genre;
        this.duration = duration;
        this.rating = rating;
        this.releaseYear = releaseYear;

    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getGenre() { return genre; }
    public void setGenre(String genre) { this.genre = genre; }

    public int getDuration() { return duration; }
    public void setDuration(int duration) { this.duration = duration; }

    public double getRating() { return rating; }
    public void setRating(double rating) { this.rating = rating; }

    public int getReleaseYear() { return releaseYear; }
    public void setReleaseYear(int releaseYear) { this.releaseYear = releaseYear; }

}
