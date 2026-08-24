package com.earthcrying.repository;

import com.earthcrying.entity.CommunitySolution;
import com.earthcrying.entity.CommunitySolutionFlag;
import com.earthcrying.entity.CommunitySolutionVote;
import com.earthcrying.entity.ImpactCategory;
import com.earthcrying.entity.SolutionStatus;
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
public interface CommunitySolutionRepository extends JpaRepository<CommunitySolution, UUID> {

    List<CommunitySolution> findByUserIdOrderByCreatedAtDesc(UUID userId);

    List<CommunitySolution> findByStatus(SolutionStatus status);

    List<CommunitySolution> findByImpactCategory(ImpactCategory impactCategory);

    List<CommunitySolution> findByStatusOrderByCreatedAtDesc(SolutionStatus status);

    List<CommunitySolution> findByImpactCategoryAndStatus(ImpactCategory impactCategory, SolutionStatus status);

    List<CommunitySolution> findByOrderByCreatedAtDesc();

    @Modifying
    @Query("UPDATE CommunitySolution c SET c.upvoteCount = c.upvoteCount + 1 WHERE c.id = :solutionId")
    void incrementUpvote(@Param("solutionId") UUID solutionId);

    @Modifying
    @Query("UPDATE CommunitySolution c SET c.downvoteCount = c.downvoteCount + 1 WHERE c.id = :solutionId")
    void incrementDownvote(@Param("solutionId") UUID solutionId);

    @Modifying
    @Query("UPDATE CommunitySolution c SET c.flagCount = c.flagCount + 1 WHERE c.id = :solutionId")
    void incrementFlagCount(@Param("solutionId") UUID solutionId);

    @Modifying
    @Query("UPDATE CommunitySolution c SET c.status = :status, c.moderatedAt = :moderatedAt, c.moderatedBy = :moderatedBy, c.moderationNotes = :moderationNotes WHERE c.id = :solutionId")
    void moderateSolution(@Param("solutionId") UUID solutionId, @Param("status") SolutionStatus status,
                         @Param("moderatedAt") OffsetDateTime moderatedAt, @Param("moderatedBy") UUID moderatedBy,
                         @Param("moderationNotes") String moderationNotes);
}