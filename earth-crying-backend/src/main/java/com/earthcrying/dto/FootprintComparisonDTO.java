package com.earthcrying.dto;

import lombok.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FootprintComparisonDTO {

    private Map<String, Integer> userCategoryScores;
    private int userTotalScore;
    private String userCountryCode;
    private CountryBenchmarkData countryAverage;
    private CountryBenchmarkData sustainableTarget;
    private List<ComparisonInsight> insights;
    private double overallPercentile; // 0-100, where 100 = better than everyone

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class CountryBenchmarkData {
        private String countryCode;
        private String countryName;
        private BigDecimal co2PerCapitaTons;
        private BigDecimal waterFootprintLitersPerDay;
        private BigDecimal digitalCarbonKgPerYear;
        private BigDecimal transportEmissionsKgPerYear;
        private BigDecimal fashionImpactScore;
        private BigDecimal eWasteKgPerYear;
        private BigDecimal indoorVocsScore;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ComparisonInsight {
        private String category;
        private String insight;
        private InsightType type;
        private int userValue;
        private int countryAverage;
        private int sustainableTarget;
    }

    public enum InsightType {
        ABOVE_AVERAGE,
        BELOW_AVERAGE,
        ABOVE_SUSTAINABLE,
        BELOW_SUSTAINABLE,
        CRITICAL
    }
}