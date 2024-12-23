package me.project.backend.repository;

import jakarta.validation.constraints.NotBlank;
import me.project.backend.domain.Community;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CommunityRepository extends JpaRepository<Community, Long> {

    Optional<Community> findByName(@NotBlank String name);
}
