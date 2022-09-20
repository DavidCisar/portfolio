package io.dcisar.backend.rating;

import io.dcisar.backend.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RatingRepository extends JpaRepository<Rating, Long> {
    Optional<Rating> findById(Long id);

    Optional<Rating> findByUser(User user);
}
