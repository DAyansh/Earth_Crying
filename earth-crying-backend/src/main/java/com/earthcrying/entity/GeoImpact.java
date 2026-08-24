package com.earthcrying.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "geo_impacts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GeoImpact {

    @Id
    @GeneratedValue
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "country_code", nullable = false, length = 2)
    private String countryCode;

    @Column(name = "country_name", nullable = false, length = 100)
    private String countryName;

    @Column(name = "region", length = 100)
    private String region;

    @Column(name = "deforestation_rate_hectares_per_year", precision = 15, scale = 2)
    private BigDecimal deforestationRateHectaresPerYear;

    @Column(name = "aqi_avg")
    private Integer aqiAvg;

    @Column(name = "water_stress_index", precision = 5, scale = 2)
    private BigDecimal waterStressIndex;

    @Column(name = "co2_emissions_mt_per_year", precision = 15, scale = 2)
    private BigDecimal co2EmissionsMtPerYear;

    @Column(name = "renewable_energy_percent", precision = 5, scale = 2)
    private BigDecimal renewableEnergyPercent;

    @Column(name = "protected_land_percent", precision = 5, scale = 2)
    private BigDecimal protectedLandPercent;

    @Column(name = "biodiversity_intactness_index", precision = 5, scale = 2)
    private BigDecimal biodiversityIntactnessIndex;

    @Column(name = "plastic_waste_mt_per_year", precision = 15, scale = 2)
    private BigDecimal plasticWasteMtPerYear;

    @Column(name = "data_year", nullable = false)
    private Integer dataYear;

    @Column(name = "source_name", nullable = false, length = 100)
    private String sourceName;

    @Column(name = "source_url", length = 500)
    private String sourceUrl;

    @Column(name = "is_projected", nullable = false)
    private Boolean isProjected = false;

    @Column(name = "projection_scenario", length = 50)
    private String projectionScenario;

    @Column(name = "confidence_level", precision = 3, scale = 2)
    private BigDecimal confidenceLevel;

    @CreationTimestamp
    @Column(name = "fetched_at", nullable = false, updatable = false)
    private OffsetDateTime fetchedAt;
}