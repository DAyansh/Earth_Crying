package com.earthcrying.repository;

import com.earthcrying.entity.Confession;
import com.earthcrying.entity.ImpactCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ConfessionRepository extends JpaRepository<Confession, UUID> {

    Page<Confession> findByIsApprovedTrueOrderBySubmittedAtDesc(Pageable pageable);

    List<Confession> findByIsApprovedTrueAndImpactCategoryOrderBySubmittedAtDesc(ImpactCategory impactCategory);

    List<Confession> findByIsApprovedTrueOrderBySubmittedAtDesc();

    @Query("SELECT c FROM Confession c WHERE c.isApproved = true AND c.ipHash = :ipHash AND c.submittedAt > :since")
    List<Confession> findRecentByIpHash(@Param("ipHash") String ipHash, @Param("since") java.time.OffsetDateTime since);

    long countByIsApprovedTrue();

    long countByIsFlaggedTrue();
}