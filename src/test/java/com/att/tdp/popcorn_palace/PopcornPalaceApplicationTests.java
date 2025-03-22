package com.att.tdp.popcorn_palace;

import com.att.tdp.popcorn_palace.model.Movie;
import com.att.tdp.popcorn_palace.model.Showtime;
import com.att.tdp.popcorn_palace.repository.MovieRepository;
import com.att.tdp.popcorn_palace.repository.ShowtimeRepository;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class PopcornPalaceApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private ShowtimeRepository showtimeRepository;


    private Movie movie;
    private Showtime showtime;

    @BeforeEach
    void setup() {
        movie = new Movie("Test Movie", "Action", 120, 9.0, 2025);
        movie = movieRepository.save(movie);

        showtime = new Showtime(movie.getId(), "Main Theater", 
            LocalDateTime.now().plusDays(1),
            LocalDateTime.now().plusDays(1).plusHours(2),
            50.0);
        showtime.setMovie(movie);
        showtime = showtimeRepository.save(showtime);
    }

    @Test
    void testGetAllMovies() throws Exception {
        mockMvc.perform(get("/movies/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Test Movie"));
    }

    @Test
    void testAddMovie() throws Exception {
        mockMvc.perform(post("/movies")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"title\":\"Another Movie\",\"genre\":\"Comedy\",\"duration\":90,\"rating\":7.5,\"releaseYear\":2023}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Another Movie"));
    }

    @Test
    void testDeleteMovie() throws Exception {
        mockMvc.perform(delete("/movies/{movieTitle}", movie.getTitle()))
                .andExpect(status().isOk());
    }

    @Test
    void testGetShowtimeById() throws Exception {
        mockMvc.perform(get("/showtimes/{showtimeId}", showtime.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.price").value(50.0));
    }

    @Test
    void testDeleteShowtime() throws Exception {
        mockMvc.perform(delete("/showtimes/{showtimeId}", showtime.getId()))
                .andExpect(status().isOk());
    }

    @Test
    void testBookTicket() throws Exception {
        String userId = "123e4567-e89b-12d3-a456-426614174000";
        mockMvc.perform(post("/bookings")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"showtimeId\":" + showtime.getId() + ", \"seatNumber\":5, \"userId\":\"" + userId + "\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.bookingId").exists());
    }

    @Test
    void testGetShowtimeById_NotFound() throws Exception {
        mockMvc.perform(get("/showtimes/{showtimeId}", 9999L))
                .andExpect(status().isNotFound());
    }

	@Test
	@Transactional
	void testAddShowtime_ConflictSameTheaterAndTime() throws Exception {
		// Add initial showtime
		Movie movie = movieRepository.save(new Movie("Matrix", "Action", 120, 9.0, 2021));

		Showtime showtime1 = new Showtime(movie.getId(), "Theater 1",
			LocalDateTime.parse("2025-04-01T10:00:00"),
			LocalDateTime.parse("2025-04-01T12:00:00"), 30.0);
		showtimeRepository.save(showtime1);

		// Try to add overlapping showtime
		mockMvc.perform(post("/showtimes")
			.contentType(MediaType.APPLICATION_JSON)
			.content("""
				{
				"movieId": %d,
				"price": 30.0,
				"theater": "Theater 1",
				"startTime": "2025-04-01T11:00:00",
				"endTime": "2025-04-01T13:00:00"
				}
				""".formatted(movie.getId())))
			.andExpect(status().isBadRequest());
	}

	@Test
	@Transactional
	void testAddShowtime_InvalidStartAndEndTime() throws Exception {
		Movie movie = movieRepository.save(new Movie("Backwards Time", "Sci-Fi", 100, 7.2, 2025));

		mockMvc.perform(post("/showtimes")
			.contentType(MediaType.APPLICATION_JSON)
			.content("""
				{
				"movieId": %d,
				"price": 20.0,
				"theater": "Theater X",
				"startTime": "2025-05-01T15:00:00",
				"endTime": "2025-05-01T13:00:00"
				}
				""".formatted(movie.getId())))
			.andExpect(status().isBadRequest());
	}

	@Test
	@Transactional
	void testAddShowtimeIntegration() throws Exception {
		Movie movie = new Movie("Add Showtime Movie", "Fantasy", 110, 7.9, 2024);
		movie = movieRepository.saveAndFlush(movie); // ensures ID is available

		mockMvc.perform(post("/showtimes")
			.contentType(MediaType.APPLICATION_JSON)
			.content("""
				{
				"movieId": %d,
				"price": 22.0,
				"theater": "Hall",
				"startTime": "2025-02-14T11:47:46.125405Z",
				"endTime": "2025-02-14T14:47:46.125405Z"
				}
				""".formatted(movie.getId())))
				.andDo(MockMvcResultHandlers.print())  // Logs the request and response
				.andExpect(status().isOk());
	}

	@Test
	@Transactional
	void testUpdateMovie() throws Exception {
		Movie movie = movieRepository.save(new Movie("Old Title", "Horror", 90, 6.5, 2023));

		mockMvc.perform(post("/movies/update/%s".formatted(movie.getTitle()))  // Use POST method here
				.contentType(MediaType.APPLICATION_JSON)
				.content("""
					{
					"title": "Updated Title",
					"genre": "Thriller",
					"duration": 100,
					"rating": 7.8,
					"releaseYear": 2024
					}
				"""))
				.andExpect(status().isOk());  

		Movie updatedMovie = movieRepository.findByTitle("Updated Title").orElseThrow();

		assertEquals("Updated Title", updatedMovie.getTitle());
	}
}
