package com.earthcrying.controller;

import com.earthcrying.dto.DigitalCarbonEstimateRequest;
import com.earthcrying.dto.DigitalCarbonEstimateResponse;
import com.earthcrying.service.DigitalCarbonCalculatorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Digital Carbon Calculator", description = "Personal digital carbon footprint estimation")
@RestController
@RequestMapping("/api/digital-carbon")
@RequiredArgsConstructor
public class DigitalCarbonController {

    private final DigitalCarbonCalculatorService calculatorService;

    @PostMapping("/estimate")
    @Operation(summary = "Calculate personal digital carbon footprint estimate")
    public ResponseEntity<DigitalCarbonEstimateResponse> estimate(@RequestBody DigitalCarbonEstimateRequest request) {
        DigitalCarbonEstimateResponse response = calculatorService.calculateEstimate(request);
        return ResponseEntity.ok(response);
    }
}