package com.earthcrying.controller;

import com.earthcrying.dto.ImpactDTO;
import com.earthcrying.entity.ImpactCategory;
import com.earthcrying.service.ImpactService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Impacts", description = "Impact categories and hidden damage information")
@RestController
@RequestMapping("/api/impacts")
@RequiredArgsConstructor
public class ImpactController {

    private final ImpactService impactService;

    @GetMapping
    @Operation(summary = "Get all impact categories")
    public ResponseEntity<List<ImpactDTO>> getAllImpacts() {
        return ResponseEntity.ok(impactService.getAllImpacts());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get impact by ID")
    public ResponseEntity<ImpactDTO> getImpactById(@PathVariable String id) {
        return ResponseEntity.ok(impactService.getImpactById(id));
    }

    @GetMapping("/category/{category}")
    @Operation(summary = "Get impact by category")
    public ResponseEntity<ImpactDTO> getImpactByCategory(@PathVariable ImpactCategory category) {
        return ResponseEntity.ok(impactService.getImpactByCategory(category));
    }

    @PostMapping
    @Operation(summary = "Create new impact category")
    public ResponseEntity<ImpactDTO> createImpact(@RequestBody ImpactDTO impactDTO) {
        return ResponseEntity.ok(impactService.createImpact(impactDTO));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update impact category")
    public ResponseEntity<ImpactDTO> updateImpact(@PathVariable String id, @RequestBody ImpactDTO impactDTO) {
        return ResponseEntity.ok(impactService.updateImpact(id, impactDTO));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete impact category")
    public ResponseEntity<Void> deleteImpact(@PathVariable String id) {
        impactService.deleteImpact(id);
        return ResponseEntity.ok().build();
    }
}