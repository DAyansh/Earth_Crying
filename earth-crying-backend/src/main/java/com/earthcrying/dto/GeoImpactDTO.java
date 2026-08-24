package com.earthcrying.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GeoImpactDTO {

    private String id;
    private String countryCode;
    private String countryName;
    private String region;
    private BigDecimal deforestationRateHectaresPerYear;
    private Integer aqiAvg;
    private BigDecimal waterStressIndex;
    private BigDecimal co2EmissionsMtPerYear;
    private BigDecimal renewableEnergyPercent;
    private BigDecimal protectedLandPercent;
    private BigDecimal biodiversityIntactnessIndex;
    private BigDecimal plasticWasteMtPerYear;
    private Integer dataYear;
    private String sourceName;
    private String sourceUrl;
    private OffsetDateTime fetchedAt;
}