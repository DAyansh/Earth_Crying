package com.earthcrying.controller;

import com.earthcrying.dto.SolutionDTO;
import com.earthcrying.entity.ActionScale;
import com.earthcrying.entity.EffortLevel;
import com.earthcrying.service.SolutionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Solutions", description = "Actions and solutions for environmental impact")
@RestController
@RequestMapping("/api/solutions")
@RequiredArgsConstructor
public class SolutionController {

    private final SolutionService solutionService;

    @GetMapping
    @Operation(summary = "Get all solutions")
    public ResponseEntity<List<SolutionDTO>> getAllSolutions() {
        return ResponseEntity.ok(solutionService.getAllSolutions());
    }

    @GetMapping("/impact/{impactId}")
    @Operation(summary = "Get solutions for a specific impact")
    public ResponseEntity<List<SolutionDTO>> getSolutionsByImpact(@PathVariable String impactId) {
        return ResponseEntity.ok(solutionService.getSolutionsByImpactId(impactId));
    }

    @GetMapping("/effort/{effortLevel}")
    @Operation(summary = "Get solutions by effort level")
    public ResponseEntity<List<SolutionDTO>> getSolutionsByEffortLevel(@PathVariable EffortLevel effortLevel) {
        return ResponseEntity.ok(solutionService.getSolutionsByEffortLevel(effortLevel));
    }

    @GetMapping("/scale/{actionScale}")
    @Operation(summary = "Get solutions by action scale")
    public ResponseEntity<List<SolutionDTO>> getSolutionsByActionScale(@PathVariable ActionScale actionScale) {
        return ResponseEntity.ok(solutionService.getSolutionsByActionScale(actionScale));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get solution by ID")
    public ResponseEntity<SolutionDTO> getSolutionById(@PathVariable String id) {
        return ResponseEntity.ok(solutionService.getSolutionById(id));
    }
}