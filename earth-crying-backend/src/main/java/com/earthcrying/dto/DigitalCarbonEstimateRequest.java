package com.earthcrying.dto;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DigitalCarbonEstimateRequest {

    private Double streamingHoursPerDay;
    private Double cloudStorageGB;
    private Integer aiQueriesPerDay;
    private Integer emailsPerDay;
    private Integer searchesPerDay;
}