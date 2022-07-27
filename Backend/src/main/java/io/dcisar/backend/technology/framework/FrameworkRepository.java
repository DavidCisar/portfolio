package io.dcisar.backend.technology.framework;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FrameworkRepository extends JpaRepository<Framework, Long> {
}
