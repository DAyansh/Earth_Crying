package com.earthcrying.service;

import com.earthcrying.dto.TimeTravelDataDTO;
import com.earthcrying.entity.GeoImpact;
import com.earthcrying.repository.GeoImpactRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class TimeTravelService {

    private final GeoImpactRepository geoImpactRepository;

    public TimeTravelDataDTO getTimelineForCountry(String countryCode) {
        List<GeoImpact> allData = geoImpactRepository.findByCountryCodeOrderByDataYearDesc(countryCode.toUpperCase());
        
        if (allData.isEmpty()) {
            throw new IllegalArgumentException("No data found for country: " + countryCode);
        }

        String countryName = allData.get(0).getCountryName();

        // Separate historical, current, and projected
        List<GeoImpact> historical = allData.stream()
                .filter(d -> !d.getIsProjected() && d.getDataYear() < 2024)
                .sorted(Comparator.comparing(GeoImpact::getDataYear))
                .collect(Collectors.toList());

        GeoImpact current = allData.stream()
                .filter(d -> !d.getIsProjected() && d.getDataYear() >= 2024)
                .min(Comparator.comparing(GeoImpact::getDataYear))
                .orElse(null);

        List<GeoImpact> projected = allData.stream()
                .filter(GeoImpact::getIsProjected)
                .sorted(Comparator.comparing(GeoImpact::getDataYear))
                .collect(Collectors.toList());

        // Build timeline points
        List<TimeTravelDataDTO.TimelinePoint> historicalPoints = historical.stream()
                .map(this::toTimelinePoint)
                .collect(Collectors.toList());

        TimeTravelDataDTO.TimelinePoint currentPoint = current != null ? toTimelinePoint(current) : null;

        List<TimeTravelDataDTO.TimelinePoint> projectedPoints = projected.stream()
                .map(this::toTimelinePoint)
                .collect(Collectors.toList());

        // Build metric timelines
        Map<String, TimeTravelDataDTO.MetricTimeline> metricTimelines = buildMetricTimelines(allData);

        return TimeTravelDataDTO.builder()
                .countryCode(countryCode.toUpperCase())
                .countryName(countryName)
                .historicalData(historicalPoints)
                .currentData(currentPoint)
                .projectedData(projectedPoints)
                .metricTimelines(metricTimelines)
                .build();
    }

    public List<TimeTravelDataDTO.TimelinePoint> getGlobalTimeline() {
        // Get latest year for each country and aggregate
        List<GeoImpact> latestData = geoImpactRepository.findLatestYearData();
        
        // Group by year and calculate global averages
        Map<Integer, List<GeoImpact>> byYear = latestData.stream()
                .filter(d -> !d.getIsProjected())
                .collect(Collectors.groupingBy(GeoImpact::getDataYear));

        return byYear.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .map(entry -> {
                    List<GeoImpact> yearData = entry.getValue();
                    return TimeTravelDataDTO.TimelinePoint.builder()
                            .year(entry.getKey())
                            .isProjected(false)
                            .deforestationRate(average(yearData.stream().map(GeoImpact::getDeforestationRateHectaresPerYear)))
                            .aqiAvg((int) Math.round(yearData.stream().mapToInt(d -> d.getAqiAvg() != null ? d.getAqiAvg() : 0).average().orElse(0)))
                            .waterStressIndex(average(yearData.stream().map(GeoImpact::getWaterStressIndex)))
                            .co2Emissions(average(yearData.stream().map(GeoImpact::getCo2EmissionsMtPerYear)))
                            .renewableEnergyPercent(average(yearData.stream().map(GeoImpact::getRenewableEnergyPercent)))
                            .protectedLandPercent(average(yearData.stream().map(GeoImpact::getProtectedLandPercent)))
                            .biodiversityIntactnessIndex(average(yearData.stream().map(GeoImpact::getBiodiversityIntactnessIndex)))
                            .plasticWaste(average(yearData.stream().map(GeoImpact::getPlasticWasteMtPerYear)))
                            .build();
                })
                .collect(Collectors.toList());
    }

    private TimeTravelDataDTO.TimelinePoint toTimelinePoint(GeoImpact g) {
        return TimeTravelDataDTO.TimelinePoint.builder()
                .year(g.getDataYear())
                .isProjected(g.getIsProjected())
                .projectionScenario(g.getProjectionScenario())
                .confidenceLevel(g.getConfidenceLevel())
                .deforestationRate(g.getDeforestationRateHectaresPerYear())
                .aqiAvg(g.getAqiAvg())
                .waterStressIndex(g.getWaterStressIndex())
                .co2Emissions(g.getCo2EmissionsMtPerYear())
                .renewableEnergyPercent(g.getRenewableEnergyPercent())
                .protectedLandPercent(g.getProtectedLandPercent())
                .biodiversityIntactnessIndex(g.getBiodiversityIntactnessIndex())
                .plasticWaste(g.getPlasticWasteMtPerYear())
                .build();
    }

    private Map<String, TimeTravelDataDTO.MetricTimeline> buildMetricTimelines(List<GeoImpact> allData) {
        Map<String, TimeTravelDataDTO.MetricTimeline> timelines = new LinkedHashMap<>();

        // Deforestation
        timelines.put("deforestation", TimeTravelDataDTO.MetricTimeline.builder()
                .metricName("Deforestation Rate")
                .unit("hectares/year")
                .points(allData.stream()
                        .sorted(Comparator.comparing(GeoImpact::getDataYear))
                        .map(g -> TimeTravelDataDTO.MetricTimeline.DataPoint.builder()
                                .year(g.getDataYear())
                                .value(g.getDeforestationRateHectaresPerYear())
                                .isProjected(g.getIsProjected())
                                .scenario(g.getProjectionScenario())
                                .build())
                        .collect(Collectors.toList()))
                .build());

        // CO2 Emissions
        timelines.put("co2", TimeTravelDataDTO.MetricTimeline.builder()
                .metricName("CO₂ Emissions")
                .unit("Mt/year")
                .points(allData.stream()
                        .sorted(Comparator.comparing(GeoImpact::getDataYear))
                        .map(g -> TimeTravelDataDTO.MetricTimeline.DataPoint.builder()
                                .year(g.getDataYear())
                                .value(g.getCo2EmissionsMtPerYear())
                                .isProjected(g.getIsProjected())
                                .scenario(g.getProjectionScenario())
                                .build())
                        .collect(Collectors.toList()))
                .build());

        // Renewable Energy
        timelines.put("renewable", TimeTravelDataDTO.MetricTimeline.builder()
                .metricName("Renewable Energy")
                .unit("%")
                .points(allData.stream()
                        .sorted(Comparator.comparing(GeoImpact::getDataYear))
                        .map(g -> TimeTravelDataDTO.MetricTimeline.DataPoint.builder()
                                .year(g.getDataYear())
                                .value(g.getRenewableEnergyPercent())
                                .isProjected(g.getIsProjected())
                                .scenario(g.getProjectionScenario())
                                .build())
                        .collect(Collectors.toList()))
                .build());

        // Biodiversity
        timelines.put("biodiversity", TimeTravelDataDTO.MetricTimeline.builder()
                .metricName("Biodiversity Intactness")
                .unit("index (0-1)")
                .points(allData.stream()
                        .sorted(Comparator.comparing(GeoImpact::getDataYear))
                        .map(g -> TimeTravelDataDTO.MetricTimeline.DataPoint.builder()
                                .year(g.getDataYear())
                                .value(g.getBiodiversityIntactnessIndex())
                                .isProjected(g.getIsProjected())
                                .scenario(g.getProjectionScenario())
                                .build())
                        .collect(Collectors.toList()))
                .build());

        return timelines;
    }

    private BigDecimal average(java.util.stream.Stream<BigDecimal> stream) {
        List<BigDecimal> values = stream.filter(Objects::nonNull).toList();
        if (values.isEmpty()) {
            return BigDecimal.ZERO;
        }

        BigDecimal sum = values.stream().reduce(BigDecimal.ZERO, BigDecimal::add);
        return sum.divide(BigDecimal.valueOf(values.size()), 2, java.math.RoundingMode.HALF_UP);
    }
}
