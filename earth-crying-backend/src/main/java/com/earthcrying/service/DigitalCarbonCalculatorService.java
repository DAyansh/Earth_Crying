package com.earthcrying.service;

import com.earthcrying.dto.DigitalCarbonBenchmarkDTO;
import com.earthcrying.dto.DigitalCarbonEstimateRequest;
import com.earthcrying.dto.DigitalCarbonEstimateResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DigitalCarbonCalculatorService {

    private final DigitalCarbonBenchmarkService benchmarkService;

    // Global average constants (per person per year)
    private static final BigDecimal GLOBAL_AVERAGE_YEARLY_KG_CO2 = new BigDecimal("350"); // kg CO2/year per person from digital activities
    private static final BigDecimal CARBON_CONSCIOUS_TARGET_YEARLY_KG_CO2 = new BigDecimal("100"); // kg CO2/year target
    private static final BigDecimal TREES_PER_KG_CO2 = new BigDecimal("0.022"); // 1 tree absorbs ~22kg CO2/year, so 1kg needs 0.022 trees

    public DigitalCarbonEstimateResponse calculateEstimate(DigitalCarbonEstimateRequest request) {
        // Fetch all benchmarks
        List<DigitalCarbonBenchmarkDTO> benchmarks = benchmarkService.getAllBenchmarks();
        Map<String, DigitalCarbonBenchmarkDTO> benchmarkMap = benchmarks.stream()
                .collect(Collectors.toMap(DigitalCarbonBenchmarkDTO::getBenchmarkKey, b -> b));

        List<DigitalCarbonEstimateResponse.BreakdownItem> breakdown = new ArrayList<>();
        BigDecimal totalDailyGramsCO2 = BigDecimal.ZERO;

        // 1. Streaming (HD and 4K)
        if (request.getStreamingHoursPerDay() != null && request.getStreamingHoursPerDay() > 0) {
            BigDecimal hdFactor = getBenchmarkValue(benchmarkMap, "streaming_hd_per_hour");
            BigDecimal k4Factor = getBenchmarkValue(benchmarkMap, "streaming_4k_per_hour");
            
            // Assume 50/50 split between HD and 4K for simplicity
            BigDecimal avgStreamingFactor = hdFactor.add(k4Factor).divide(BigDecimal.valueOf(2), 6, RoundingMode.HALF_UP);
            BigDecimal dailyStreaming = avgStreamingFactor.multiply(BigDecimal.valueOf(request.getStreamingHoursPerDay()));
            
            breakdown.add(DigitalCarbonEstimateResponse.BreakdownItem.builder()
                    .activity("Video Streaming (HD/4K)")
                    .dailyGramsCO2(dailyStreaming.setScale(2, RoundingMode.HALF_UP))
                    .yearlyKgCO2(dailyStreaming.multiply(BigDecimal.valueOf(365)).divide(BigDecimal.valueOf(1000), 2, RoundingMode.HALF_UP))
                    .unit("hours/day")
                    .userValue(request.getStreamingHoursPerDay())
                    .build());
            
            totalDailyGramsCO2 = totalDailyGramsCO2.add(dailyStreaming);
        }

        // 2. Cloud Storage
        if (request.getCloudStorageGB() != null && request.getCloudStorageGB() > 0) {
            BigDecimal cloudFactor = getBenchmarkValue(benchmarkMap, "cloud_storage_per_gb_per_year");
            // Convert yearly to daily
            BigDecimal dailyCloud = cloudFactor.multiply(BigDecimal.valueOf(request.getCloudStorageGB()))
                    .divide(BigDecimal.valueOf(365), 6, RoundingMode.HALF_UP);
            
            breakdown.add(DigitalCarbonEstimateResponse.BreakdownItem.builder()
                    .activity("Cloud Storage")
                    .dailyGramsCO2(dailyCloud.setScale(2, RoundingMode.HALF_UP))
                    .yearlyKgCO2(cloudFactor.multiply(BigDecimal.valueOf(request.getCloudStorageGB()))
                            .divide(BigDecimal.valueOf(1000), 2, RoundingMode.HALF_UP))
                    .unit("GB")
                    .userValue(request.getCloudStorageGB().doubleValue())
                    .build());
            
            totalDailyGramsCO2 = totalDailyGramsCO2.add(dailyCloud);
        }

        // 3. AI Queries
        if (request.getAiQueriesPerDay() != null && request.getAiQueriesPerDay() > 0) {
            BigDecimal aiTextFactor = getBenchmarkValue(benchmarkMap, "ai_text_query");
            BigDecimal aiImageFactor = getBenchmarkValue(benchmarkMap, "ai_image_generation");
            
            // Assume 80% text, 20% image queries
            BigDecimal avgAiFactor = aiTextFactor.multiply(BigDecimal.valueOf(0.8))
                    .add(aiImageFactor.multiply(BigDecimal.valueOf(0.2)));
            
            BigDecimal dailyAi = avgAiFactor.multiply(BigDecimal.valueOf(request.getAiQueriesPerDay()));
            
            breakdown.add(DigitalCarbonEstimateResponse.BreakdownItem.builder()
                    .activity("AI Queries (Text/Image)")
                    .dailyGramsCO2(dailyAi.setScale(2, RoundingMode.HALF_UP))
                    .yearlyKgCO2(dailyAi.multiply(BigDecimal.valueOf(365)).divide(BigDecimal.valueOf(1000), 2, RoundingMode.HALF_UP))
                    .unit("queries/day")
                    .userValue(request.getAiQueriesPerDay().doubleValue())
                    .build());
            
            totalDailyGramsCO2 = totalDailyGramsCO2.add(dailyAi);
        }

        // 4. Emails
        if (request.getEmailsPerDay() != null && request.getEmailsPerDay() > 0) {
            BigDecimal emailFactor = getBenchmarkValue(benchmarkMap, "email_sent");
            BigDecimal emailAttachmentFactor = getBenchmarkValue(benchmarkMap, "email_with_attachment");
            
            // Assume 90% without attachment, 10% with
            BigDecimal avgEmailFactor = emailFactor.multiply(BigDecimal.valueOf(0.9))
                    .add(emailAttachmentFactor.multiply(BigDecimal.valueOf(0.1)));
            
            BigDecimal dailyEmail = avgEmailFactor.multiply(BigDecimal.valueOf(request.getEmailsPerDay()));
            
            breakdown.add(DigitalCarbonEstimateResponse.BreakdownItem.builder()
                    .activity("Emails Sent")
                    .dailyGramsCO2(dailyEmail.setScale(2, RoundingMode.HALF_UP))
                    .yearlyKgCO2(dailyEmail.multiply(BigDecimal.valueOf(365)).divide(BigDecimal.valueOf(1000), 2, RoundingMode.HALF_UP))
                    .unit("emails/day")
                    .userValue(request.getEmailsPerDay().doubleValue())
                    .build());
            
            totalDailyGramsCO2 = totalDailyGramsCO2.add(dailyEmail);
        }

        // 5. Searches
        if (request.getSearchesPerDay() != null && request.getSearchesPerDay() > 0) {
            BigDecimal searchFactor = getBenchmarkValue(benchmarkMap, "google_search");
            BigDecimal dailySearch = searchFactor.multiply(BigDecimal.valueOf(request.getSearchesPerDay()));
            
            breakdown.add(DigitalCarbonEstimateResponse.BreakdownItem.builder()
                    .activity("Web Searches")
                    .dailyGramsCO2(dailySearch.setScale(2, RoundingMode.HALF_UP))
                    .yearlyKgCO2(dailySearch.multiply(BigDecimal.valueOf(365)).divide(BigDecimal.valueOf(1000), 2, RoundingMode.HALF_UP))
                    .unit("searches/day")
                    .userValue(request.getSearchesPerDay().doubleValue())
                    .build());
            
            totalDailyGramsCO2 = totalDailyGramsCO2.add(dailySearch);
        }

        BigDecimal yearlyKgCO2 = totalDailyGramsCO2.multiply(BigDecimal.valueOf(365))
                .divide(BigDecimal.valueOf(1000), 2, RoundingMode.HALF_UP);
        
        // Calculate equivalent trees needed
        int equivalentTreesNeeded = yearlyKgCO2.multiply(TREES_PER_KG_CO2)
                .setScale(0, RoundingMode.UP)
                .intValue();

        // Comparison
        Double percentageOfGlobalAverage = yearlyKgCO2.divide(GLOBAL_AVERAGE_YEARLY_KG_CO2, 4, RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(100))
                .doubleValue();
        
        Double percentageOfCarbonConsciousTarget = yearlyKgCO2.divide(CARBON_CONSCIOUS_TARGET_YEARLY_KG_CO2, 4, RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(100))
                .doubleValue();

        DigitalCarbonEstimateResponse.Comparison comparison = DigitalCarbonEstimateResponse.Comparison.builder()
                .userYearlyKgCO2(yearlyKgCO2)
                .globalAverageYearlyKgCO2(GLOBAL_AVERAGE_YEARLY_KG_CO2)
                .carbonConsciousTargetYearlyKgCO2(CARBON_CONSCIOUS_TARGET_YEARLY_KG_CO2)
                .percentageOfGlobalAverage(percentageOfGlobalAverage)
                .percentageOfCarbonConsciousTarget(percentageOfCarbonConsciousTarget)
                .build();

        return DigitalCarbonEstimateResponse.builder()
                .dailyGramsCO2(totalDailyGramsCO2.setScale(2, RoundingMode.HALF_UP))
                .yearlyKgCO2(yearlyKgCO2)
                .equivalentTreesNeeded(equivalentTreesNeeded)
                .breakdown(breakdown)
                .comparisonVsGlobalAverage(comparison)
                .build();
    }

    private BigDecimal getBenchmarkValue(Map<String, DigitalCarbonBenchmarkDTO> benchmarkMap, String key) {
        DigitalCarbonBenchmarkDTO benchmark = benchmarkMap.get(key);
        if (benchmark == null || benchmark.getValue() == null) {
            throw new IllegalStateException("Benchmark not found or has no value: " + key);
        }
        return benchmark.getValue();
    }
}