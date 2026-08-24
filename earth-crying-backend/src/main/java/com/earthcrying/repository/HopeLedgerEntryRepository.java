package com.earthcrying.repository;

import com.earthcrying.entity.HopeLedgerEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface HopeLedgerEntryRepository extends JpaRepository<HopeLedgerEntry, UUID> {

    List<HopeLedgerEntry> findByCategory(String category);

    List<HopeLedgerEntry> findByCategoryOrderByRecordedAtDesc(String category);

    List<HopeLedgerEntry> findByIsLatestTrue();

    @Query("SELECT h FROM HopeLedgerEntry h WHERE h.isLatest = true AND h.category = :category")
    Optional<HopeLedgerEntry> findLatestByCategory(String category);

    @Query("SELECT h FROM HopeLedgerEntry h WHERE h.category = :category ORDER BY h.recordedAt DESC")
    List<HopeLedgerEntry> findByCategoryOrderByRecordedAtDescLimit(String category,
            org.springframework.data.domain.Pageable pageable);

    List<HopeLedgerEntry> findByCountryCodeOrderByRecordedAtDesc(String countryCode);

    List<HopeLedgerEntry> findByRecordedAtAfterOrderByRecordedAtDesc(OffsetDateTime since);
}