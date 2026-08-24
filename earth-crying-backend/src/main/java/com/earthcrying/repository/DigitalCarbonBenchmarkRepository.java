package com.earthcrying.repository;

import com.earthcrying.entity.DigitalCarbonBenchmark;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface DigitalCarbonBenchmarkRepository extends JpaRepository<DigitalCarbonBenchmark, UUID> {

    List<DigitalCarbonBenchmark> findByIsActiveTrue();

    Optional<DigitalCarbonBenchmark> findByBenchmarkKey(String benchmarkKey);

    List<DigitalCarbonBenchmark> findByBenchmarkKeyIn(List<String> benchmarkKeys);
}