package com.earthcrying.dto;

import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DigitalCarbonEstimateResponse {

    private BigDecimal dailyGramsCO2;
    private BigDecimal yearlyKgCO2;
    private Integer equivalentTreesNeeded;
    private List<BreakdownItem> breakdown;
    private Comparison comparisonVsGlobalAverage;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class BreakdownItem {
        private String activity;
        private BigDecimal dailyGramsCO2;
        private BigDecimal yearlyKgCO2;
        private String unit;
        private Double userValue;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Comparison {
        private BigDecimal userYearlyKgCO2;
        private BigDecimal globalAverageYearlyKgCO2;
        private BigDecimal carbonConsciousTargetYearlyKgCO2;
        private Double percentageOfGlobalAverage;
        private Double percentageOfCarbonConsciousTarget;
    }
}