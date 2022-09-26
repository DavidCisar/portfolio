package io.dcisar.backend.reference;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReferenceRepository extends JpaRepository<Reference, Long> {
    Optional<Reference> findById(Long id);
    Optional<Reference> findByName(String name);
}
