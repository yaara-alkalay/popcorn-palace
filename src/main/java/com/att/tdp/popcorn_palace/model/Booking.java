package com.att.tdp.popcorn_palace.model;
import jakarta.validation.constraints.*;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.UUID;
import jakarta.persistence.*;


// The Booking entity represents a booking made for a particular showtime. 
// It maps to the 'booking' table in the database.
@Entity
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // Auto-generated long ID
    private long id;

    // The 'uuid' field represents a universally unique identifier for the booking.
    // This is used to ensure the booking ID is globally unique.
    @JsonProperty("bookingId")
    @Column(name = "uuid", columnDefinition = "UUID", updatable = false, nullable = false)
    private UUID uuid;

    // Many bookings can be associated with one showtime. 
    // The @ManyToOne annotation indicates the many-to-one relationship with the Showtime entity.
    // @JoinColumn defines the foreign key in the Booking table linking to the Showtime.
    @ManyToOne
    @JoinColumn(name = "showtimeId",referencedColumnName = "id", insertable = false, updatable = false)
    @JsonIgnore
    private Showtime showtime;

    @NotNull(message = "Showtime ID is required")
    @Column(name = "showtimeId")
    @JsonProperty("showtimeId")
    private long showtimeId;

    @Positive(message = "Seat number must be greater than zero")
    @JsonProperty("seatNumber")
    private int seatNumber;

    @NotBlank(message = "User ID is required")
    @JsonProperty("userId")
    private String userId;

    //Default constructor initializes a new UUID for the booking
    public Booking() {this.uuid = UUID.randomUUID();}

    public Booking(long showtimeId, String userId, int seatNumber) {
        this();
        this.showtimeId = showtimeId;
        this.userId = userId;
        this.seatNumber = seatNumber;
    }

    public UUID getUuid() {return uuid;}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Showtime getShowtime() {return showtime;}
    public void setShowtime(Showtime showtime) {this.showtime = showtime;}

    public String getUserId() {return userId;}
    public void setUserId(String userId) {this.userId = userId;}

    public int getSeatNumber() {return seatNumber;}
    public void setSeatNumber(int seatNumber) {this.seatNumber = seatNumber;}

    public long getShowtimeId() {return showtimeId;}
    public void setShowtimeId(long showtimeId) {this.showtimeId = showtimeId;}
}