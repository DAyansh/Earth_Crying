package com.earthcrying.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SolutionDTO {

    private String id;
    private String impactId;
    private String impactCategory;
    private String title;
    private String description;
    private String effortLevel;
    private String actionScale;
    private Integer impactScore;
    private BigDecimal co2SavedKgPerYear;
    private BigDecimal waterSavedLitersPerYear;
    private BigDecimal moneySavedUsdPerYear;
    private String externalResourceUrl;
    private Integer displayOrder;
    private Boolean isActive;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
}