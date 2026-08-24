package com.earthcrying.service;

import com.earthcrying.dto.PledgeDTO;
import com.earthcrying.entity.Pledge;
import com.earthcrying.entity.PledgeStatus;
import com.earthcrying.repository.PledgeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class PledgeService {

    private final PledgeRepository pledgeRepository;

    public List<PledgeDTO> getUserPledges(String userId) {
        return pledgeRepository.findByUserIdOrderByStartedAtDesc(UUID.fromString(userId))
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public List<PledgeDTO> getActivePledges(String userId) {
        return pledgeRepository.findByUserIdAndStatus(UUID.fromString(userId), PledgeStatus.ACTIVE)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public PledgeDTO createPledge(PledgeDTO pledgeDTO) {
        var pledge = Pledge.builder()
                .user(null)
                .solution(null)
                .status(PledgeStatus.ACTIVE)
                .startedAt(OffsetDateTime.now())
                .currentStreak(0)
                .longestStreak(0)
                .checkInCount(0)
                .graceDaysRemaining(1)
                .consecutiveMissedDays(0)
                .build();

        var saved = pledgeRepository.save(pledge);
        return mapToDTO(saved);
    }

    @Transactional
    public PledgeDTO checkIn(String pledgeId) {
        var pledge = pledgeRepository.findById(UUID.fromString(pledgeId))
                .orElseThrow(() -> new RuntimeException("Pledge not found: " + pledgeId));

        var newStreak = pledge.getCurrentStreak() + 1;
        if (newStreak > pledge.getLongestStreak()) {
            pledge.setLongestStreak(newStreak);
        }

        // Reset grace on successful check-in
        pledge.setGraceDaysRemaining(1);
        pledge.setConsecutiveMissedDays(0);
        pledge.setLastMissedCheckInAt(null);

        pledgeRepository.updateStreakAndCheckIn(
                UUID.fromString(pledgeId),
                newStreak,
                OffsetDateTime.now(),
                1, // grace days reset
                0  // consecutive missed reset
        );

        return mapToDTO(pledge);
    }

    @Transactional
    public PledgeDTO recordMissedCheckIn(String pledgeId) {
        var pledge = pledgeRepository.findById(UUID.fromString(pledgeId))
                .orElseThrow(() -> new RuntimeException("Pledge not found: " + pledgeId));

        int graceRemaining = pledge.getGraceDaysRemaining() - 1;
        int consecutiveMissed = pledge.getConsecutiveMissedDays() + 1;

        if (graceRemaining >= 0) {
            // Still in grace period - soft warning
            pledge.setGraceDaysRemaining(graceRemaining);
            pledge.setConsecutiveMissedDays(consecutiveMissed);
            pledge.setLastMissedCheckInAt(OffsetDateTime.now());
            
            log.info("Pledge {} missed check-in. Grace days remaining: {}", pledgeId, graceRemaining);
            
            pledgeRepository.updateGraceAndMissed(
                    UUID.fromString(pledgeId),
                    graceRemaining,
                    consecutiveMissed,
                    OffsetDateTime.now()
            );
            
            return mapToDTO(pledge);
        } else {
            // Grace exhausted - streak breaks
            log.warn("Pledge {} grace exhausted. Resetting streak.", pledgeId);
            
            pledge.setCurrentStreak(0);
            pledge.setGraceDaysRemaining(1); // Reset grace for next time
            pledge.setConsecutiveMissedDays(0);
            pledge.setLastMissedCheckInAt(null);
            
            pledgeRepository.updateStreakAndGrace(
                    UUID.fromString(pledgeId),
                    0,
                    1,
                    0,
                    null
            );
            
            return mapToDTO(pledge);
        }
    }

    @Transactional
    public PledgeDTO completePledge(String pledgeId) {
        var pledge = pledgeRepository.findById(UUID.fromString(pledgeId))
                .orElseThrow(() -> new RuntimeException("Pledge not found: " + pledgeId));

        pledge.setStatus(PledgeStatus.COMPLETED);
        pledge.setCompletedAt(OffsetDateTime.now());

        return mapToDTO(pledge);
    }

    @Transactional
    public PledgeDTO pausePledge(String pledgeId) {
        var pledge = pledgeRepository.findById(UUID.fromString(pledgeId))
                .orElseThrow(() -> new RuntimeException("Pledge not found: " + pledgeId));

        pledge.setStatus(PledgeStatus.PAUSED);

        return mapToDTO(pledge);
    }

    private PledgeDTO mapToDTO(Pledge pledge) {
        return PledgeDTO.builder()
                .id(pledge.getId().toString())
                .userId(pledge.getUser() != null ? pledge.getUser().getId().toString() : null)
                .solutionId(pledge.getSolution() != null ? pledge.getSolution().getId().toString() : null)
                .status(pledge.getStatus().name())
                .startedAt(pledge.getStartedAt())
                .completedAt(pledge.getCompletedAt())
                .targetDate(pledge.getTargetDate())
                .currentStreak(pledge.getCurrentStreak())
                .longestStreak(pledge.getLongestStreak())
                .lastCheckInAt(pledge.getLastCheckInAt())
                .checkInCount(pledge.getCheckInCount())
                .notes(pledge.getNotes())
                .graceDaysRemaining(pledge.getGraceDaysRemaining())
                .consecutiveMissedDays(pledge.getConsecutiveMissedDays())
                .lastMissedCheckInAt(pledge.getLastMissedCheckInAt())
                .createdAt(pledge.getCreatedAt())
                .updatedAt(pledge.getUpdatedAt())
                .build();
    }
}