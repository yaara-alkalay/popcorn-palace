package com.att.tdp.popcorn_palace.repository;

import com.att.tdp.popcorn_palace.model.Movie;  // Import Movie entity
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// The @Repository annotation marks this interface as a Spring Data JPA repository. 
// It enables the implementation of CRUD operations and allows you to define custom queries.
@Repository  
public interface MovieRepository extends JpaRepository<Movie, Long> {
        // Custom method to find a movie by its title. This method will be automatically implemented by Spring Data JPA.
        // 'Optional<Movie>' is used to handle the case when a movie is not found.
        Optional<Movie> findByTitle(String title);
}
