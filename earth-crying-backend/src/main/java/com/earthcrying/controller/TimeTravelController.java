package com.earthcrying.controller;

import com.earthcrying.dto.TimeTravelDataDTO;
import com.earthcrying.service.TimeTravelService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Time Travel", description = "Scrubbable timeline of environmental data (past -> now -> projected)")
@RestController
@RequestMapping("/api/timeline")
@RequiredArgsConstructor
public class TimeTravelController {

    private final TimeTravelService timeTravelService;

    @GetMapping("/country/{countryCode}")
    @Operation(summary = "Get full timeline for a specific country")
    public ResponseEntity<TimeTravelDataDTO> getCountryTimeline(@PathVariable String countryCode) {
        TimeTravelDataDTO timeline = timeTravelService.getTimelineForCountry(countryCode);
        return ResponseEntity.ok(timeline);
    }

    @GetMapping("/global")
    @Operation(summary = "Get aggregated global timeline")
    public ResponseEntity<List<TimeTravelDataDTO.TimelinePoint>> getGlobalTimeline() {
        List<TimeTravelDataDTO.TimelinePoint> timeline = timeTravelService.getGlobalTimeline();
        return ResponseEntity.ok(timeline);
    }
}