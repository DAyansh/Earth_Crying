package com.earthcrying.service;

import com.earthcrying.dto.FootprintComparisonDTO;
import com.earthcrying.entity.CountryBenchmark;
import com.earthcrying.entity.FootprintResult;
import com.earthcrying.repository.CountryBenchmarkRepository;
import com.earthcrying.repository.FootprintResultRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class FootprintComparisonService {

    private final CountryBenchmarkRepository countryBenchmarkRepository;
    private final FootprintResultRepository footprintResultRepository;
    private final ObjectMapper objectMapper;

    private static final TypeReference<Map<String, Integer>> MAP_STRING_INTEGER = new TypeReference<Map<String, Integer>>() {
    };

    public FootprintComparisonDTO compareFootprint(String userId, String countryCode) {
        // Get user's latest footprint result
        FootprintResult latestResult = footprintResultRepository.findByUserIdOrderByCompletedAtDesc(
                        java.util.UUID.fromString(userId))
                .stream().findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No footprint result found for user"));

        return buildComparison(latestResult, countryCode);
    }

    public FootprintComparisonDTO compareFootprintBySession(String sessionId, String countryCode) {
        FootprintResult latestResult = footprintResultRepository.findBySessionId(sessionId)
                .orElseThrow(() -> new IllegalArgumentException("No footprint result found for session"));

        return buildComparison(latestResult, countryCode);
    }

    private FootprintComparisonDTO buildComparison(FootprintResult latestResult, String countryCode) {

        Map<String, Integer> userCategoryScores = parseCategoryScores(latestResult.getCategoryScores());
        int userTotalScore = latestResult.getTotalScore();

        // Get country benchmark (fallback to global average if not found)
        String effectiveCountryCode = countryCode != null ? countryCode.toUpperCase() : "ZZ";
        CountryBenchmark benchmark = countryBenchmarkRepository.findByCountryCode(effectiveCountryCode)
                .orElseGet(() -> countryBenchmarkRepository.findByCountryCode("ZZ")
                        .orElseThrow(() -> new IllegalStateException("Global benchmark not found")));

        // Build country average data
        FootprintComparisonDTO.CountryBenchmarkData countryAverage = buildCountryBenchmarkData(benchmark);
        FootprintComparisonDTO.CountryBenchmarkData sustainableTarget = buildSustainableTargetData(benchmark);

        // Generate insights
        List<FootprintComparisonDTO.ComparisonInsight> insights = generateInsights(
                userCategoryScores, benchmark);

        // Calculate overall percentile (simplified - based on total score vs country average)
        double overallPercentile = calculatePercentile(userTotalScore, benchmark);

        return FootprintComparisonDTO.builder()
                .userCategoryScores(userCategoryScores)
                .userTotalScore(userTotalScore)
                .userCountryCode(effectiveCountryCode)
                .countryAverage(countryAverage)
                .sustainableTarget(sustainableTarget)
                .insights(insights)
                .overallPercentile(overallPercentile)
                .build();
    }

    private Map<String, Integer> parseCategoryScores(String json) {
        try {
            return objectMapper.readValue(json, MAP_STRING_INTEGER);
        } catch (Exception e) {
            log.error("Failed to parse category scores", e);
            return Map.of();
        }
    }

    private FootprintComparisonDTO.CountryBenchmarkData buildCountryBenchmarkData(CountryBenchmark b) {
        return FootprintComparisonDTO.CountryBenchmarkData.builder()
                .countryCode(b.getCountryCode())
                .countryName(b.getCountryName())
                .co2PerCapitaTons(b.getAvgCo2PerCapitaTons())
                .waterFootprintLitersPerDay(b.getAvgWaterFootprintLitersPerDay())
                .digitalCarbonKgPerYear(b.getAvgDigitalCarbonKgPerYear())
                .transportEmissionsKgPerYear(b.getAvgTransportEmissionsKgPerYear())
                .fashionImpactScore(b.getAvgFashionImpactScore())
                .eWasteKgPerYear(b.getAvgEWasteKgPerYear())
                .indoorVocsScore(b.getAvgIndoorVocsScore())
                .build();
    }

    private FootprintComparisonDTO.CountryBenchmarkData buildSustainableTargetData(CountryBenchmark b) {
        return FootprintComparisonDTO.CountryBenchmarkData.builder()
                .countryCode(b.getCountryCode())
                .countryName("Sustainable Target")
                .co2PerCapitaTons(b.getSustainableTargetCo2PerCapitaTons())
                .waterFootprintLitersPerDay(b.getSustainableTargetWaterLitersPerDay())
                .digitalCarbonKgPerYear(BigDecimal.valueOf(50)) // Estimated sustainable target
                .transportEmissionsKgPerYear(BigDecimal.valueOf(500)) // Estimated sustainable target
                .fashionImpactScore(BigDecimal.valueOf(20)) // Estimated sustainable target
                .eWasteKgPerYear(BigDecimal.valueOf(5)) // Estimated sustainable target
                .indoorVocsScore(BigDecimal.valueOf(15)) // Estimated sustainable target
                .build();
    }

    private List<FootprintComparisonDTO.ComparisonInsight> generateInsights(
            Map<String, Integer> userScores, CountryBenchmark benchmark) {

        List<FootprintComparisonDTO.ComparisonInsight> insights = new ArrayList<>();

        // Map quiz categories to benchmark fields
        Map<String, BenchmarkField> categoryMapping = Map.of(
                "DIGITAL_CARBON_FOOTPRINT", new BenchmarkField("Digital Carbon", 
                    benchmark.getAvgDigitalCarbonKgPerYear().intValue(), 
                    50, // sustainable target
                    userScores.getOrDefault("DIGITAL_CARBON_FOOTPRINT", 0)),
                "TIRE_BRAKE_DUST", new BenchmarkField("Transport",
                    benchmark.getAvgTransportEmissionsKgPerYear().intValue(),
                    500,
                    userScores.getOrDefault("TIRE_BRAKE_DUST", 0)),
                "FAST_FASHION_MICROPLASTICS", new BenchmarkField("Fast Fashion",
                    benchmark.getAvgFashionImpactScore().intValue(),
                    20,
                    userScores.getOrDefault("FAST_FASHION_MICROPLASTICS", 0)),
                "HIDDEN_WATER_FOOTPRINT", new BenchmarkField("Water Footprint",
                    benchmark.getAvgWaterFootprintLitersPerDay().intValue(),
                    3000,
                    userScores.getOrDefault("HIDDEN_WATER_FOOTPRINT", 0)),
                "E_WASTE_RARE_EARTH_MINING", new BenchmarkField("E-Waste",
                    benchmark.getAvgEWasteKgPerYear().intValue(),
                    5,
                    userScores.getOrDefault("E_WASTE_RARE_EARTH_MINING", 0)),
                "INDOOR_VOCS_FRAGRANCE_CHEMICALS", new BenchmarkField("Indoor Air Quality",
                    benchmark.getAvgIndoorVocsScore().intValue(),
                    15,
                    userScores.getOrDefault("INDOOR_VOCS_FRAGRANCE_CHEMICALS", 0))
        );

        for (Map.Entry<String, BenchmarkField> entry : categoryMapping.entrySet()) {
            BenchmarkField bf = entry.getValue();
            String insightText;
            FootprintComparisonDTO.InsightType insightType;

            if (bf.userValue > bf.countryAverage * 2) {
                insightText = String.format("Your %s impact is %.0fx the %s average — a critical area to address",
                        bf.categoryName, (double) bf.userValue / bf.countryAverage, benchmark.getCountryName());
                insightType = FootprintComparisonDTO.InsightType.CRITICAL;
            } else if (bf.userValue > bf.countryAverage) {
                insightText = String.format("Your %s impact is %d%% above the %s average",
                        bf.categoryName, ((bf.userValue - bf.countryAverage) * 100 / bf.countryAverage), benchmark.getCountryName());
                insightType = FootprintComparisonDTO.InsightType.ABOVE_AVERAGE;
            } else {
                insightText = String.format("Your %s impact is %d%% below the %s average — well done!",
                        bf.categoryName, ((bf.countryAverage - bf.userValue) * 100 / bf.countryAverage), benchmark.getCountryName());
                insightType = FootprintComparisonDTO.InsightType.BELOW_AVERAGE;
            }

            if (bf.userValue > bf.sustainableTarget) {
                insightText += String.format(" and %d%% above the sustainable target.", 
                        ((bf.userValue - bf.sustainableTarget) * 100 / bf.sustainableTarget));
                if (insightType != FootprintComparisonDTO.InsightType.CRITICAL) {
                    insightType = FootprintComparisonDTO.InsightType.ABOVE_SUSTAINABLE;
                }
            }

            insights.add(FootprintComparisonDTO.ComparisonInsight.builder()
                    .category(entry.getKey())
                    .insight(insightText)
                    .type(insightType)
                    .userValue(bf.userValue)
                    .countryAverage(bf.countryAverage)
                    .sustainableTarget(bf.sustainableTarget)
                    .build());
        }

        return insights;
    }

    private double calculatePercentile(int userTotalScore, CountryBenchmark benchmark) {
        // Simplified: assume normal distribution around country average total score
        int countryAvgTotal = benchmark.getAvgDigitalCarbonKgPerYear().intValue()
                + benchmark.getAvgTransportEmissionsKgPerYear().intValue()
                + benchmark.getAvgFashionImpactScore().intValue()
                + benchmark.getAvgWaterFootprintLitersPerDay().intValue() / 10
                + benchmark.getAvgEWasteKgPerYear().intValue()
                + benchmark.getAvgIndoorVocsScore().intValue();

        // Lower score = better (lower footprint)
        if (userTotalScore <= countryAvgTotal * 0.5) return 95.0;
        if (userTotalScore <= countryAvgTotal * 0.75) return 75.0;
        if (userTotalScore <= countryAvgTotal) return 50.0;
        if (userTotalScore <= countryAvgTotal * 1.5) return 25.0;
        return 10.0;
    }

    private record BenchmarkField(String categoryName, int countryAverage, int sustainableTarget, int userValue) {}
}
