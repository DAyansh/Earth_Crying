package com.earthcrying.repository;

import com.earthcrying.entity.FootprintResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface FootprintResultRepository extends JpaRepository<FootprintResult, UUID> {

    List<FootprintResult> findByUserIdOrderByCompletedAtDesc(UUID userId);

    Optional<FootprintResult> findBySessionId(String sessionId);

    List<FootprintResult> findByCompletedAtAfterOrderByCompletedAtDesc(OffsetDateTime since);
}