package com.earthcrying.dto;

import lombok.*;

import java.time.OffsetDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PledgeDTO {

    private String id;
    private String userId;
    private String solutionId;
    private String status;
    private OffsetDateTime startedAt;
    private OffsetDateTime completedAt;
    private OffsetDateTime targetDate;
    private Integer currentStreak;
    private Integer longestStreak;
    private OffsetDateTime lastCheckInAt;
    private Integer checkInCount;
    private String notes;
    private Integer graceDaysRemaining;
    private Integer consecutiveMissedDays;
    private OffsetDateTime lastMissedCheckInAt;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
}