package com.earthcrying.repository;

import com.earthcrying.entity.ScheduledJobLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ScheduledJobLogRepository extends JpaRepository<ScheduledJobLog, UUID> {

    List<ScheduledJobLog> findByJobNameOrderByStartedAtDesc(String jobName);

    List<ScheduledJobLog> findByStartedAtAfterOrderByStartedAtDesc(OffsetDateTime since);

    Optional<ScheduledJobLog> findTopByJobNameOrderByStartedAtDesc(String jobName);
}