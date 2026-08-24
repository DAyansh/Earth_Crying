package com.earthcrying.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommunitySolutionDTO {

    private String id;
    private String userId;
    private String title;
    private String description;
    private String location;
    private Double latitude;
    private Double longitude;
    private String impactCategory;
    private String relatedSolutionId;
    private String status;
    private Integer upvoteCount;
    private Integer downvoteCount;
    private Integer flagCount;
    private OffsetDateTime moderatedAt;
    private String moderatedById;
    private String moderationNotes;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
}