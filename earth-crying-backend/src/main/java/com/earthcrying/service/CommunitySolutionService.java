package com.earthcrying.service;

import com.earthcrying.dto.CommunitySolutionDTO;
import com.earthcrying.entity.CommunitySolution;
import com.earthcrying.entity.SolutionStatus;
import com.earthcrying.repository.CommunitySolutionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommunitySolutionService {

    private final CommunitySolutionRepository communitySolutionRepository;

    public List<CommunitySolutionDTO> getAllSolutions() {
        return communitySolutionRepository.findByStatusOrderByCreatedAtDesc(SolutionStatus.APPROVED)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public List<CommunitySolutionDTO> getPendingSolutions() {
        return communitySolutionRepository.findByStatus(SolutionStatus.PENDING)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public CommunitySolutionDTO getSolutionById(String id) {
        return communitySolutionRepository.findById(UUID.fromString(id))
                .map(this::mapToDTO)
                .orElseThrow(() -> new RuntimeException("Community solution not found: " + id));
    }

    @Transactional
    public CommunitySolutionDTO createSolution(CommunitySolutionDTO dto) {
        var solution = CommunitySolution.builder()
                .user(null)
                .title(dto.getTitle())
                .description(dto.getDescription())
                .location(dto.getLocation())
                .latitude(dto.getLatitude() != null ? java.math.BigDecimal.valueOf(dto.getLatitude()) : null)
                .longitude(dto.getLongitude() != null ? java.math.BigDecimal.valueOf(dto.getLongitude()) : null)
                .impactCategory(dto.getImpactCategory() != null ? com.earthcrying.entity.ImpactCategory.valueOf(dto.getImpactCategory()) : null)
                .status(SolutionStatus.PENDING)
                .upvoteCount(0)
                .downvoteCount(0)
                .flagCount(0)
                .build();

        var saved = communitySolutionRepository.save(solution);
        return mapToDTO(saved);
    }

    @Transactional
    public CommunitySolutionDTO upvoteSolution(String solutionId, String userId) {
        communitySolutionRepository.incrementUpvote(UUID.fromString(solutionId));
        return getSolutionById(solutionId);
    }

    @Transactional
    public CommunitySolutionDTO downvoteSolution(String solutionId) {
        communitySolutionRepository.incrementDownvote(UUID.fromString(solutionId));
        return getSolutionById(solutionId);
    }

    @Transactional
    public CommunitySolutionDTO flagSolution(String solutionId, String userId) {
        communitySolutionRepository.incrementFlagCount(UUID.fromString(solutionId));
        return getSolutionById(solutionId);
    }

    @Transactional
    public CommunitySolutionDTO moderateSolution(String solutionId, String status, String moderatorId, String notes) {
        communitySolutionRepository.moderateSolution(
                UUID.fromString(solutionId),
                SolutionStatus.valueOf(status),
                java.time.OffsetDateTime.now(),
                UUID.fromString(moderatorId),
                notes
        );
        return getSolutionById(solutionId);
    }

    private CommunitySolutionDTO mapToDTO(CommunitySolution solution) {
        return CommunitySolutionDTO.builder()
                .id(solution.getId().toString())
                .userId(solution.getUser() != null ? solution.getUser().getId().toString() : null)
                .title(solution.getTitle())
                .description(solution.getDescription())
                .location(solution.getLocation())
                .latitude(solution.getLatitude() != null ? solution.getLatitude().doubleValue() : null)
                .longitude(solution.getLongitude() != null ? solution.getLongitude().doubleValue() : null)
                .impactCategory(solution.getImpactCategory() != null ? solution.getImpactCategory().name() : null)
                .relatedSolutionId(solution.getRelatedSolution() != null ? solution.getRelatedSolution().getId().toString() : null)
                .status(solution.getStatus().name())
                .upvoteCount(solution.getUpvoteCount())
                .downvoteCount(solution.getDownvoteCount())
                .flagCount(solution.getFlagCount())
                .moderatedAt(solution.getModeratedAt())
                .moderatedById(solution.getModeratedBy() != null ? solution.getModeratedBy().getId().toString() : null)
                .moderationNotes(solution.getModerationNotes())
                .createdAt(solution.getCreatedAt())
                .updatedAt(solution.getUpdatedAt())
                .build();
    }
}