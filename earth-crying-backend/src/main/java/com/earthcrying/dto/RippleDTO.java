package com.earthcrying.dto;

import lombok.*;

import java.time.OffsetDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RippleDTO {

    private String solutionId;
    private String solutionTitle;
    private long recentCompletions; // This week
    private long totalCompletions; // All time
    private OffsetDateTime periodStart;
    private OffsetDateTime periodEnd;
}