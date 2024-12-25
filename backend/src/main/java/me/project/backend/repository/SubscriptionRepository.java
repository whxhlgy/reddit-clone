package me.project.backend.repository;

import me.project.backend.domain.Subscription;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {
    @EntityGraph(attributePaths = { "community" })
    @Query("""
SELECT s FROM Subscription s WHERE s.user.username = :username
""")
    List<Subscription> findAllWithCommunityByUserId(@Param("username") String username);
}
