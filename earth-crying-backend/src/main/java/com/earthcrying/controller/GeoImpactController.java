package com.earthcrying.controller;

import com.earthcrying.dto.GeoImpactDTO;
import com.earthcrying.service.GeoImpactService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Geo Impact", description = "Geographic environmental impact data for maps and globes")
@RestController
@RequestMapping("/api/geo")
@RequiredArgsConstructor
public class GeoImpactController {

    private final GeoImpactService geoImpactService;

    @GetMapping
    @Operation(summary = "Get all geo impact data")
    public ResponseEntity<List<GeoImpactDTO>> getAllGeoImpacts() {
        return ResponseEntity.ok(geoImpactService.getAllGeoImpacts());
    }

    @GetMapping("/{countryCode}")
    @Operation(summary = "Get geo impact data by country code")
    public ResponseEntity<List<GeoImpactDTO>> getGeoImpactByCountry(@PathVariable String countryCode) {
        return ResponseEntity.ok(geoImpactService.getByCountryCode(countryCode));
    }

    @GetMapping("/latest")
    @Operation(summary = "Get latest geo impact data")
    public ResponseEntity<List<GeoImpactDTO>> getLatestGeoImpacts() {
        return ResponseEntity.ok(geoImpactService.getLatestData());
    }
}