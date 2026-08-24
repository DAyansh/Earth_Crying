package com.earthcrying.repository;

import com.earthcrying.entity.CommunitySolutionFlag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CommunitySolutionFlagRepository extends JpaRepository<CommunitySolutionFlag, UUID> {

    List<CommunitySolutionFlag> findByCommunitySolutionId(UUID communitySolutionId);

    List<CommunitySolutionFlag> findByUserId(UUID userId);

    Optional<CommunitySolutionFlag> findByCommunitySolutionIdAndUserId(UUID communitySolutionId, UUID userId);

    List<CommunitySolutionFlag> findByResolvedFalse();
}