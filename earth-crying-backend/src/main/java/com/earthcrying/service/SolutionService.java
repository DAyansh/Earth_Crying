package com.earthcrying.service;

import com.earthcrying.dto.SolutionDTO;
import com.earthcrying.entity.ActionScale;
import com.earthcrying.entity.EffortLevel;
import com.earthcrying.entity.Solution;
import com.earthcrying.repository.SolutionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SolutionService {

    private final SolutionRepository solutionRepository;

    @Cacheable("solutions")
    public List<SolutionDTO> getAllSolutions() {
        return solutionRepository.findAll()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Cacheable("solutions")
    public List<SolutionDTO> getSolutionsByImpactId(String impactId) {
        return solutionRepository.findByImpactIdAndIsActiveTrueOrderByDisplayOrderAsc(UUID.fromString(impactId))
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Cacheable(value = "solutions-effort", key = "#effortLevel")
    public List<SolutionDTO> getSolutionsByEffortLevel(EffortLevel effortLevel) {
        return solutionRepository.findByEffortLevel(effortLevel)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Cacheable(value = "solutions-scale", key = "#actionScale")
    public List<SolutionDTO> getSolutionsByActionScale(ActionScale actionScale) {
        return solutionRepository.findByActionScale(actionScale)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public SolutionDTO getSolutionById(String id) {
        return solutionRepository.findById(UUID.fromString(id))
                .map(this::mapToDTO)
                .orElseThrow(() -> new RuntimeException("Solution not found with id: " + id));
    }

    private SolutionDTO mapToDTO(Solution solution) {
        return SolutionDTO.builder()
                .id(solution.getId().toString())
                .impactId(solution.getImpact() != null ? solution.getImpact().getId().toString() : null)
                .impactCategory(solution.getImpact() != null ? solution.getImpact().getCategory().name() : null)
                .title(solution.getTitle())
                .description(solution.getDescription())
                .effortLevel(solution.getEffortLevel().name())
                .actionScale(solution.getActionScale().name())
                .impactScore(solution.getImpactScore())
                .co2SavedKgPerYear(solution.getCo2SavedKgPerYear())
                .waterSavedLitersPerYear(solution.getWaterSavedLitersPerYear())
                .moneySavedUsdPerYear(solution.getMoneySavedUsdPerYear())
                .externalResourceUrl(solution.getExternalResourceUrl())
                .displayOrder(solution.getDisplayOrder())
                .isActive(solution.getIsActive())
                .createdAt(solution.getCreatedAt())
                .updatedAt(solution.getUpdatedAt())
                .build();
    }
}