package com.earthcrying.dto;

import lombok.*;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FootprintResultDTO {

    private String id;
    private String userId;
    private String sessionId;
    private Map<String, Integer> categoryScores;
    private Integer totalScore;
    private List<String> topCategories;
    private List<String> recommendedActions;
    private OffsetDateTime completedAt;
}
