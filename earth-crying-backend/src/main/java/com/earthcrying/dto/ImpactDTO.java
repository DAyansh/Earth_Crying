package com.earthcrying.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ImpactDTO {

    private String id;
    private String category;
    private String title;
    private String hiddenStat;
    private String explanation;
    private String whyInvisible;
    private String videoAssetUrl;
    private String thumbnailUrl;
    private String hopeStory;
    private String hopeStoryTitle;
    private String hopeStoryImageUrl;
    private String sources; // JSON string
    private BigDecimal globalPerSecondRate;
    private String counterUnit;
    private String counterLabel;
    private Integer displayOrder;
    private Boolean isActive;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
}