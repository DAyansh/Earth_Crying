package com.earthcrying.repository;

import com.earthcrying.entity.UserBadge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserBadgeRepository extends JpaRepository<UserBadge, UUID> {

    List<UserBadge> findByUserId(UUID userId);

    List<UserBadge> findByBadgeId(UUID badgeId);

    Optional<UserBadge> findByUserIdAndBadgeId(UUID userId, UUID badgeId);

    List<UserBadge> findByUserIdAndBadgeIdIn(UUID userId, List<UUID> badgeIds);
}