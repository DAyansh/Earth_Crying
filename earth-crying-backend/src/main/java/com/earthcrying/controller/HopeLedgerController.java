package com.earthcrying.controller;

import com.earthcrying.dto.HopeLedgerEntryDTO;
import com.earthcrying.service.HopeLedgerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Hope Ledger", description = "Positive environmental progress stories and data")
@RestController
@RequestMapping("/api/hope-ledger")
@RequiredArgsConstructor
public class HopeLedgerController {

    private final HopeLedgerService hopeLedgerService;

    @GetMapping
    @Operation(summary = "Get all hope ledger entries")
    public ResponseEntity<List<HopeLedgerEntryDTO>> getAllEntries() {
        return ResponseEntity.ok(hopeLedgerService.getAllEntries());
    }

    @GetMapping("/latest")
    @Operation(summary = "Get latest hope ledger entries")
    public ResponseEntity<List<HopeLedgerEntryDTO>> getLatestEntries() {
        return ResponseEntity.ok(hopeLedgerService.getLatestEntries());
    }

    @GetMapping("/category/{category}")
    @Operation(summary = "Get hope ledger entries by category")
    public ResponseEntity<List<HopeLedgerEntryDTO>> getEntriesByCategory(@PathVariable String category) {
        return ResponseEntity.ok(hopeLedgerService.getByCategory(category));
    }

    @GetMapping("/digital-carbon")
    @Operation(summary = "Get digital carbon specific hope ledger entries")
    public ResponseEntity<List<HopeLedgerEntryDTO>> getDigitalCarbonEntries() {
        return ResponseEntity.ok(hopeLedgerService.getByCategory("digital-carbon"));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get hope ledger entry by ID")
    public ResponseEntity<HopeLedgerEntryDTO> getEntryById(@PathVariable String id) {
        return ResponseEntity.ok(hopeLedgerService.getEntryById(id));
    }
}