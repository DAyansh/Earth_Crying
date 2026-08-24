package com.earthcrying.controller;

import com.earthcrying.dto.DigitalCarbonBenchmarkDTO;
import com.earthcrying.service.DigitalCarbonBenchmarkService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Digital Carbon Benchmarks", description = "Conversion factors for digital carbon footprint calculations")
@RestController
@RequestMapping("/api/digital-carbon/benchmarks")
@RequiredArgsConstructor
public class DigitalCarbonBenchmarkController {

    private final DigitalCarbonBenchmarkService benchmarkService;

    @GetMapping
    @Operation(summary = "Get all active benchmarks")
    public ResponseEntity<List<DigitalCarbonBenchmarkDTO>> getAllBenchmarks() {
        return ResponseEntity.ok(benchmarkService.getAllBenchmarks());
    }

    @GetMapping("/by-keys")
    @Operation(summary = "Get benchmarks by keys")
    public ResponseEntity<List<DigitalCarbonBenchmarkDTO>> getBenchmarksByKeys(@RequestParam List<String> keys) {
        return ResponseEntity.ok(benchmarkService.getBenchmarksByKeys(keys));
    }

    @GetMapping("/{key}")
    @Operation(summary = "Get benchmark by key")
    public ResponseEntity<DigitalCarbonBenchmarkDTO> getBenchmarkByKey(@PathVariable String key) {
        return ResponseEntity.ok(benchmarkService.getBenchmarkByKey(key));
    }

    @PostMapping
    @Operation(summary = "Create new benchmark")
    public ResponseEntity<DigitalCarbonBenchmarkDTO> createBenchmark(@RequestBody DigitalCarbonBenchmarkDTO dto) {
        return ResponseEntity.ok(benchmarkService.createBenchmark(dto));
    }

    @PutMapping("/{key}")
    @Operation(summary = "Update benchmark")
    public ResponseEntity<DigitalCarbonBenchmarkDTO> updateBenchmark(@PathVariable String key, @RequestBody DigitalCarbonBenchmarkDTO dto) {
        return ResponseEntity.ok(benchmarkService.updateBenchmark(key, dto));
    }

    @DeleteMapping("/{key}")
    @Operation(summary = "Delete benchmark")
    public ResponseEntity<Void> deleteBenchmark(@PathVariable String key) {
        benchmarkService.deleteBenchmark(key);
        return ResponseEntity.ok().build();
    }
}