package com.earthcrying.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DigitalCarbonBenchmarkDTO {

    private String id;
    private String benchmarkKey;
    private String displayName;
    private String description;
    private BigDecimal value;
    private String unit;
    private String sourceName;
    private String sourceUrl;
    private String confidenceLevel;
    private String notes;
    private Boolean isActive;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
}