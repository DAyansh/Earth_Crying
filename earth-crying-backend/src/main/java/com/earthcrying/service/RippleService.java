package com.earthcrying.service;

import com.earthcrying.dto.RippleDTO;
import com.earthcrying.entity.Pledge;
import com.earthcrying.entity.PledgeStatus;
import com.earthcrying.entity.Solution;
import com.earthcrying.repository.PledgeRepository;
import com.earthcrying.repository.SolutionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class RippleService {

    private final PledgeRepository pledgeRepository;
    private final SolutionRepository solutionRepository;

    public RippleDTO getRippleForSolution(String solutionId) {
        UUID solutionUuid = UUID.fromString(solutionId);
        
        Solution solution = solutionRepository.findById(solutionUuid)
                .orElseThrow(() -> new IllegalArgumentException("Solution not found: " + solutionId));

        OffsetDateTime weekAgo = OffsetDateTime.now().minusWeeks(1);
        OffsetDateTime now = OffsetDateTime.now();

        // Count pledges completed this week for this solution
        long recentCompletions = pledgeRepository.countBySolutionIdAndStatusAndCompletedAtAfter(
                solutionUuid, PledgeStatus.COMPLETED, weekAgo);

        // Count all-time completions for this solution
        long totalCompletions = pledgeRepository.countBySolutionIdAndStatus(
                solutionUuid, PledgeStatus.COMPLETED);

        return RippleDTO.builder()
                .solutionId(solutionId)
                .solutionTitle(solution.getTitle())
                .recentCompletions(recentCompletions)
                .totalCompletions(totalCompletions)
                .periodStart(weekAgo)
                .periodEnd(now)
                .build();
    }

    public RippleDTO getRippleForUserPledge(String pledgeId) {
        UUID pledgeUuid = UUID.fromString(pledgeId);
        
        Pledge pledge = pledgeRepository.findById(pledgeUuid)
                .orElseThrow(() -> new IllegalArgumentException("Pledge not found: " + pledgeId));

        return getRippleForSolution(pledge.getSolution().getId().toString());
    }
}