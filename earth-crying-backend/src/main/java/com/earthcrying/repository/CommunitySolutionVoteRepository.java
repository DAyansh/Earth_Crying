package com.earthcrying.repository;

import com.earthcrying.entity.CommunitySolutionVote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CommunitySolutionVoteRepository extends JpaRepository<CommunitySolutionVote, UUID> {

    Optional<CommunitySolutionVote> findByCommunitySolutionIdAndUserId(UUID communitySolutionId, UUID userId);

    @Query("SELECT COUNT(csv) FROM CommunitySolutionVote csv WHERE csv.communitySolutionId = :communitySolutionId AND csv.voteType = 'UPVOTE'")
    Long countUpvotesByCommunitySolutionId(@Param("communitySolutionId") UUID communitySolutionId);

    @Query("SELECT COUNT(csv) FROM CommunitySolutionVote csv WHERE csv.communitySolutionId = :communitySolutionId AND csv.voteType = 'DOWNVOTE'")
    Long countDownvotesByCommunitySolutionId(@Param("communitySolutionId") UUID communitySolutionId);
}