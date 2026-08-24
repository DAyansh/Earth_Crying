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
public class TimeTravelDataDTO {

    private String countryCode;
    private String countryName;
    private List<TimelinePoint> historicalData;
    private TimelinePoint currentData;
    private List<TimelinePoint> projectedData;
    private Map<String, MetricTimeline> metricTimelines;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class TimelinePoint {
        private Integer year;
        private Boolean isProjected;
        private String projectionScenario;
        private BigDecimal confidenceLevel;
        private BigDecimal deforestationRate;
        private Integer aqiAvg;
        private BigDecimal waterStressIndex;
        private BigDecimal co2Emissions;
        private BigDecimal renewableEnergyPercent;
        private BigDecimal protectedLandPercent;
        private BigDecimal biodiversityIntactnessIndex;
        private BigDecimal plasticWaste;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class MetricTimeline {
        private String metricName;
        private String unit;
        private List<DataPoint> points;

        @Getter
        @Setter
        @NoArgsConstructor
        @AllArgsConstructor
        @Builder
        public static class DataPoint {
            private Integer year;
            private BigDecimal value;
            private Boolean isProjected;
            private String scenario;
        }
    }
}