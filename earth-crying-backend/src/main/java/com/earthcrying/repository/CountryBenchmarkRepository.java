package com.earthcrying.repository;

import com.earthcrying.entity.CountryBenchmark;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CountryBenchmarkRepository extends JpaRepository<CountryBenchmark, java.util.UUID> {

    Optional<CountryBenchmark> findByCountryCode(String countryCode);
}