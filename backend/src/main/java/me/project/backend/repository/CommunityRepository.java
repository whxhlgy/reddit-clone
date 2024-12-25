package me.project.backend.repository;

import jakarta.persistence.LockModeType;
import jakarta.validation.constraints.NotBlank;
import me.project.backend.domain.Community;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CommunityRepository extends JpaRepository<Community, Long> {

    Optional<Community> findByName(@NotBlank String name);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT c FROM community c WHERE c.id = :community_id")
    Optional<Community> findByIdForUpdate(@Param("community_id") Long id);
}
