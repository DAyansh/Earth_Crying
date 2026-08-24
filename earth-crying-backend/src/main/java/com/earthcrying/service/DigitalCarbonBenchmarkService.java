package com.earthcrying.service;

import com.earthcrying.dto.DigitalCarbonBenchmarkDTO;
import com.earthcrying.entity.DigitalCarbonBenchmark;
import com.earthcrying.exception.ResourceNotFoundException;
import com.earthcrying.repository.DigitalCarbonBenchmarkRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DigitalCarbonBenchmarkService {

    private final DigitalCarbonBenchmarkRepository benchmarkRepository;

    @Cacheable("digital-carbon-benchmarks")
    public List<DigitalCarbonBenchmarkDTO> getAllBenchmarks() {
        return benchmarkRepository.findByIsActiveTrue()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public List<DigitalCarbonBenchmarkDTO> getBenchmarksByKeys(List<String> keys) {
        return benchmarkRepository.findByBenchmarkKeyIn(keys)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public DigitalCarbonBenchmarkDTO getBenchmarkByKey(String key) {
        return benchmarkRepository.findByBenchmarkKey(key)
                .map(this::mapToDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Benchmark not found with key: " + key));
    }

    @Transactional
    @CacheEvict(value = "digital-carbon-benchmarks", allEntries = true)
    public DigitalCarbonBenchmarkDTO createBenchmark(DigitalCarbonBenchmarkDTO dto) {
        DigitalCarbonBenchmark benchmark = mapToEntity(dto);
        DigitalCarbonBenchmark saved = benchmarkRepository.save(benchmark);
        return mapToDTO(saved);
    }

    @Transactional
    @CacheEvict(value = "digital-carbon-benchmarks", allEntries = true)
    public DigitalCarbonBenchmarkDTO updateBenchmark(String key, DigitalCarbonBenchmarkDTO dto) {
        DigitalCarbonBenchmark existing = benchmarkRepository.findByBenchmarkKey(key)
                .orElseThrow(() -> new ResourceNotFoundException("Benchmark not found with key: " + key));

        existing.setDisplayName(dto.getDisplayName());
        existing.setDescription(dto.getDescription());
        existing.setValue(dto.getValue());
        existing.setUnit(dto.getUnit());
        existing.setSourceName(dto.getSourceName());
        existing.setSourceUrl(dto.getSourceUrl());
        existing.setConfidenceLevel(dto.getConfidenceLevel());
        existing.setNotes(dto.getNotes());
        existing.setIsActive(dto.getIsActive());

        DigitalCarbonBenchmark saved = benchmarkRepository.save(existing);
        return mapToDTO(saved);
    }

    @Transactional
    @CacheEvict(value = "digital-carbon-benchmarks", allEntries = true)
    public void deleteBenchmark(String key) {
        DigitalCarbonBenchmark existing = benchmarkRepository.findByBenchmarkKey(key)
                .orElseThrow(() -> new ResourceNotFoundException("Benchmark not found with key: " + key));
        benchmarkRepository.delete(existing);
    }

    public BigDecimal getBenchmarkValue(String key) {
        return benchmarkRepository.findByBenchmarkKey(key)
                .map(DigitalCarbonBenchmark::getValue)
                .orElseThrow(() -> new ResourceNotFoundException("Benchmark not found with key: " + key));
    }

    private DigitalCarbonBenchmarkDTO mapToDTO(DigitalCarbonBenchmark benchmark) {
        return DigitalCarbonBenchmarkDTO.builder()
                .id(benchmark.getId().toString())
                .benchmarkKey(benchmark.getBenchmarkKey())
                .displayName(benchmark.getDisplayName())
                .description(benchmark.getDescription())
                .value(benchmark.getValue())
                .unit(benchmark.getUnit())
                .sourceName(benchmark.getSourceName())
                .sourceUrl(benchmark.getSourceUrl())
                .confidenceLevel(benchmark.getConfidenceLevel())
                .notes(benchmark.getNotes())
                .isActive(benchmark.getIsActive())
                .createdAt(benchmark.getCreatedAt())
                .updatedAt(benchmark.getUpdatedAt())
                .build();
    }

    private DigitalCarbonBenchmark mapToEntity(DigitalCarbonBenchmarkDTO dto) {
        return DigitalCarbonBenchmark.builder()
                .id(dto.getId() != null ? UUID.fromString(dto.getId()) : UUID.randomUUID())
                .benchmarkKey(dto.getBenchmarkKey())
                .displayName(dto.getDisplayName())
                .description(dto.getDescription())
                .value(dto.getValue())
                .unit(dto.getUnit())
                .sourceName(dto.getSourceName())
                .sourceUrl(dto.getSourceUrl())
                .confidenceLevel(dto.getConfidenceLevel())
                .notes(dto.getNotes())
                .isActive(dto.getIsActive() != null ? dto.getIsActive() : true)
                .build();
    }
}