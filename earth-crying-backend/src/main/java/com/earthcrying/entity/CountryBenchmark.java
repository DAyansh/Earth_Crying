package com.earthcrying.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "country_benchmarks")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CountryBenchmark {

    @Id
    @GeneratedValue
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "country_code", nullable = false, length = 2, unique = true)
    private String countryCode;

    @Column(name = "country_name", nullable = false, length = 100)
    private String countryName;

    @Column(name = "avg_co2_per_capita_tons", nullable = false, precision = 10, scale = 2)
    private BigDecimal avgCo2PerCapitaTons;

    @Column(name = "avg_water_footprint_liters_per_day", nullable = false, precision = 10, scale = 2)
    private BigDecimal avgWaterFootprintLitersPerDay;

    @Column(name = "avg_digital_carbon_kg_per_year", nullable = false, precision = 10, scale = 2)
    private BigDecimal avgDigitalCarbonKgPerYear;

    @Column(name = "avg_transport_emissions_kg_per_year", nullable = false, precision = 10, scale = 2)
    private BigDecimal avgTransportEmissionsKgPerYear;

    @Column(name = "avg_fashion_impact_score", nullable = false, precision = 10, scale = 2)
    private BigDecimal avgFashionImpactScore;

    @Column(name = "avg_e_waste_kg_per_year", nullable = false, precision = 10, scale = 2)
    private BigDecimal avgEWasteKgPerYear;

    @Column(name = "avg_indoor_vocs_score", nullable = false, precision = 10, scale = 2)
    private BigDecimal avgIndoorVocsScore;

    @Column(name = "sustainable_target_co2_per_capita_tons", nullable = false, precision = 10, scale = 2)
    private BigDecimal sustainableTargetCo2PerCapitaTons;

    @Column(name = "sustainable_target_water_liters_per_day", nullable = false, precision = 10, scale = 2)
    private BigDecimal sustainableTargetWaterLitersPerDay;

    @Column(name = "data_year", nullable = false)
    private Integer dataYear;

    @Column(name = "source_name", nullable = false, length = 200)
    private String sourceName;

    @Column(name = "source_url", length = 500)
    private String sourceUrl;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;
}