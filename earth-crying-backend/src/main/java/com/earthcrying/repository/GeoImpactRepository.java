package com.earthcrying.repository;

import com.earthcrying.entity.GeoImpact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface GeoImpactRepository extends JpaRepository<GeoImpact, UUID> {

    Optional<GeoImpact> findByCountryCodeAndDataYear(String countryCode, Integer dataYear);

    List<GeoImpact> findByDataYearOrderByCountryNameAsc(Integer dataYear);

    @Query("SELECT g FROM GeoImpact g WHERE g.dataYear = (SELECT MAX(g2.dataYear) FROM GeoImpact g2)")
    List<GeoImpact> findLatestYearData();

    List<GeoImpact> findByCountryCodeOrderByDataYearDesc(String countryCode);
}