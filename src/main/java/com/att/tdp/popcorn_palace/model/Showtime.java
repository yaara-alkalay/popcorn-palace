package com.att.tdp.popcorn_palace.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import jakarta.validation.constraints.*;
import com.att.tdp.popcorn_palace.validation.ValidShowtime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

// The Showtime entity represents a movie showtime. 
// It maps to the 'showtime' table in the database.
@Entity
@JsonPropertyOrder({ "id", "price", "movieId", "theater", "startTime", "endTime" })
@ValidShowtime // Custom validation to ensure the showtime is valid (start time < end time)
public class Showtime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty("id")
    private Long id;

    // The movie associated with this showtime.
    // @ManyToOne indicates a many-to-one relationship between Showtime and Movie.
    // @JoinColumn defines the foreign key column in the Showtime table.
    @ManyToOne
    @JoinColumn(name = "movieId", referencedColumnName = "id", insertable = false, updatable = false)
    @JsonIgnore // Ignores the 'movie' field when serializing to JSON to avoid circular references
    private Movie movie; 

    // List of bookings for this showtime. Each showtime can have multiple bookings.
    @OneToMany(mappedBy = "showtime", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Booking> bookings;

    @Positive(message = "Price must be greater than zero")
    @JsonProperty("price")  
    private double price;

    @Column(name = "movieId")
    private long movieId;

    @NotBlank(message = "Theater name is required")
    @JsonProperty("theater")
    private String theater;

    @NotNull(message = "Start time is required")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'")
    @JsonProperty("startTime")
    private LocalDateTime startTime;

    @NotNull(message = "End time is required")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'")
    @JsonProperty("endTime")
    private LocalDateTime endTime;


    public Showtime() {}

    public Showtime(long movieId, String theater, LocalDateTime startTime, LocalDateTime endTime, double price) {
        this.movieId = movieId;
        this.theater = theater;
        this.startTime = startTime;
        this.endTime = endTime;
        this.price = price;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public Movie getMovie() { return movie; }
    public void setMovie(Movie movie) { this.movie = movie; }

    public long getMovieId() {return movieId;}
    public void setMovieId(long movieId) {this.movieId = movieId;}

    public String getTheater() { return theater; }
    public void setTheater(String theater) { this.theater = theater; }

    public LocalDateTime getStartTime() { return startTime; }
    public void setStartTime(LocalDateTime startTime) { this.startTime = startTime; }

    public LocalDateTime getEndTime() { return endTime; }
    public void setEndTime(LocalDateTime endTime) { this.endTime = endTime; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    }
    

