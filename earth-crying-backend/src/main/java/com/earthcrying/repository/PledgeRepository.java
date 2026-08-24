package com.earthcrying.repository;

import com.earthcrying.entity.Pledge;
import com.earthcrying.entity.PledgeStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PledgeRepository extends JpaRepository<Pledge, UUID> {

    List<Pledge> findByUserIdOrderByStartedAtDesc(UUID userId);

    List<Pledge> findBySolutionId(UUID solutionId);

    List<Pledge> findByStatus(PledgeStatus status);

    List<Pledge> findByUserIdAndStatus(UUID userId, PledgeStatus status);

    @Modifying
    @Query("UPDATE Pledge p SET p.currentStreak = :currentStreak, p.longestStreak = GREATEST(p.longestStreak, :currentStreak), p.lastCheckInAt = :lastCheckInAt, p.checkInCount = p.checkInCount + 1 WHERE p.id = :pledgeId")
    void updateStreakAndCheckIn(@Param("pledgeId") UUID pledgeId, @Param("currentStreak") Integer currentStreak, @Param("lastCheckInAt") OffsetDateTime lastCheckInAt);

    @Modifying
    @Query("UPDATE Pledge p SET p.currentStreak = :currentStreak, p.lastCheckInAt = :lastCheckInAt, p.checkInCount = p.checkInCount + 1, p.graceDaysRemaining = :graceDaysRemaining, p.consecutiveMissedDays = :consecutiveMissedDays WHERE p.id = :pledgeId")
    void updateStreakAndCheckIn(@Param("pledgeId") UUID pledgeId, @Param("currentStreak") Integer currentStreak, @Param("lastCheckInAt") OffsetDateTime lastCheckInAt, @Param("graceDaysRemaining") Integer graceDaysRemaining, @Param("consecutiveMissedDays") Integer consecutiveMissedDays);

    @Modifying
    @Query("UPDATE Pledge p SET p.graceDaysRemaining = :graceDaysRemaining, p.consecutiveMissedDays = :consecutiveMissedDays, p.lastMissedCheckInAt = :lastMissedCheckInAt WHERE p.id = :pledgeId")
    void updateGraceAndMissed(@Param("pledgeId") UUID pledgeId, @Param("graceDaysRemaining") Integer graceDaysRemaining, @Param("consecutiveMissedDays") Integer consecutiveMissedDays, @Param("lastMissedCheckInAt") OffsetDateTime lastMissedCheckInAt);

    @Modifying
    @Query("UPDATE Pledge p SET p.currentStreak = :currentStreak, p.graceDaysRemaining = :graceDaysRemaining, p.consecutiveMissedDays = :consecutiveMissedDays, p.lastMissedCheckInAt = :lastMissedCheckInAt WHERE p.id = :pledgeId")
    void updateStreakAndGrace(@Param("pledgeId") UUID pledgeId, @Param("currentStreak") Integer currentStreak, @Param("graceDaysRemaining") Integer graceDaysRemaining, @Param("consecutiveMissedDays") Integer consecutiveMissedDays, @Param("lastMissedCheckInAt") OffsetDateTime lastMissedCheckInAt);

    @Query("SELECT COUNT(p) FROM Pledge p WHERE p.solution.id = :solutionId AND p.status = :status AND p.completedAt > :since")
    long countBySolutionIdAndStatusAndCompletedAtAfter(@Param("solutionId") UUID solutionId, @Param("status") PledgeStatus status, @Param("since") OffsetDateTime since);

    @Query("SELECT COUNT(p) FROM Pledge p WHERE p.solution.id = :solutionId AND p.status = :status")
    long countBySolutionIdAndStatus(@Param("solutionId") UUID solutionId, @Param("status") PledgeStatus status);

    Optional<Pledge> findByUserIdAndSolutionId(UUID userId, UUID solutionId);
}