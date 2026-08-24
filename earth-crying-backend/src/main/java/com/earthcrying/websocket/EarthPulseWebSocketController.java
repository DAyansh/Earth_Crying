package com.earthcrying.websocket;

import com.earthcrying.dto.HopeLedgerEntryDTO;
import com.earthcrying.service.DigitalCarbonBenchmarkService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class EarthPulseWebSocketController {

    private final SimpMessagingTemplate messagingTemplate;
    private final DigitalCarbonBenchmarkService benchmarkService;

    private static final Map<String, BigDecimal> liveCounters = new HashMap<>();

    static {
        liveCounters.put("treesLost", new BigDecimal("0"));
        liveCounters.put("co2Emissions", new BigDecimal("0"));
        liveCounters.put("eWasteGenerated", new BigDecimal("0"));
        liveCounters.put("waterUsed", new BigDecimal("0"));
        liveCounters.put("microplasticsShed", new BigDecimal("0"));
        // Digital carbon specific counter
        liveCounters.put("digitalCarbonGramsPerSecond", new BigDecimal("0"));
    }

    @Scheduled(fixedRate = 1000)
    public void pushLiveCounters() {
        calculateAndPushCounters();
    }

    private void calculateAndPushCounters() {
        // Simulate real-time counters based on known global rates
        var treesLostRate = 0.046; // trees lost per second globally
        var co2Rate = 43_000_000_000L / 31_536_000d; // kg CO2 per second
        var eWasteRate = 50_000_000_000d / 31_536_000d; // kg e-waste per second
        var waterRate = 1_500_000_000_000d / 31_536_000d; // liters per second
        var microplasticsRate = 500_000_000_000d / 31_536_000d; // microfibers per second

        liveCounters.compute("treesLost", (k, v) -> v.add(new BigDecimal(treesLostRate)));
        liveCounters.compute("co2Emissions", (k, v) -> v.add(new BigDecimal(co2Rate)));
        liveCounters.compute("eWasteGenerated", (k, v) -> v.add(new BigDecimal(eWasteRate)));
        liveCounters.compute("waterUsed", (k, v) -> v.add(new BigDecimal(waterRate)));
        liveCounters.compute("microplasticsShed", (k, v) -> v.add(new BigDecimal(microplasticsRate)));

        // Digital carbon specific: estimate global streaming-driven emissions per second
        // Based on ~1 billion hours of video streamed globally per day
        var globalStreamingHoursPerDay = 1_000_000_000d;
        var avgStreamingFactorGramPerHour = getAverageStreamingFactor(); // grams CO2 per hour
        var digitalCarbonRate = (globalStreamingHoursPerDay * avgStreamingFactorGramPerHour) / 86400d; // grams per second

        liveCounters.compute("digitalCarbonGramsPerSecond", (k, v) -> v.add(new BigDecimal(digitalCarbonRate)));

        var pulseData = new PulseData();
        pulseData.setTimestamp(OffsetDateTime.now());
        pulseData.setCounters(new HashMap<>(liveCounters));
        pulseData.setHopeEntries(getMockHopeEntries());

        messagingTemplate.convertAndSend("/topic/earth-pulse", pulseData);
    }

    private double getAverageStreamingFactor() {
        try {
            var hdBenchmark = benchmarkService.getBenchmarkByKey("streaming_hd_per_hour");
            var k4Benchmark = benchmarkService.getBenchmarkByKey("streaming_4k_per_hour");
            var hdValue = hdBenchmark.getValue().doubleValue();
            var k4Value = k4Benchmark.getValue().doubleValue();
            // Assume 50/50 split
            return (hdValue + k4Value) / 2.0;
        } catch (Exception e) {
            log.warn("Could not fetch streaming benchmarks, using fallback: {}", e.getMessage());
            return 55.0; // fallback: ~55g CO2/hour average
        }
    }

    private java.util.List<HopeLedgerEntryDTO> getMockHopeEntries() {
        return java.util.List.of(
                HopeLedgerEntryDTO.builder()
                        .title("Renewable Energy Growth")
                        .metricValue(new BigDecimal("12.3"))
                        .metricUnit("TW capacity added in 2024")
                        .build(),
                HopeLedgerEntryDTO.builder()
                        .title("Forest Restoration")
                        .metricValue(new BigDecimal("380000000"))
                        .metricUnit("hectares restored")
                        .build(),
                HopeLedgerEntryDTO.builder()
                        .title("Species Recovery")
                        .metricValue(new BigDecimal("82"))
                        .metricUnit("% species improving")
                        .build()
        );
    }

    public static class PulseData {
        private OffsetDateTime timestamp;
        private Map<String, BigDecimal> counters;
        private java.util.List<HopeLedgerEntryDTO> hopeEntries;

        public OffsetDateTime getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(OffsetDateTime timestamp) {
            this.timestamp = timestamp;
        }

        public Map<String, BigDecimal> getCounters() {
            return counters;
        }

        public void setCounters(Map<String, BigDecimal> counters) {
            this.counters = counters;
        }

        public java.util.List<HopeLedgerEntryDTO> getHopeEntries() {
            return hopeEntries;
        }

        public void setHopeEntries(java.util.List<HopeLedgerEntryDTO> hopeEntries) {
            this.hopeEntries = hopeEntries;
        }
    }
}