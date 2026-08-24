package com.earthcrying.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "impacts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Impact {

    @Id
    @GeneratedValue
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Enumerated(EnumType.STRING)
    @Column(name = "category", nullable = false, unique = true)
    private ImpactCategory category;

    @Column(name = "title", nullable = false, length = 200)
    private String title;

    @Column(name = "hidden_stat", nullable = false, length = 500)
    private String hiddenStat;

    @Column(name = "explanation", nullable = false, columnDefinition = "TEXT")
    private String explanation;

    @Column(name = "why_invisible", nullable = false, columnDefinition = "TEXT")
    private String whyInvisible;

    @Column(name = "video_asset_url", length = 500)
    private String videoAssetUrl;

    @Column(name = "thumbnail_url", length = 500)
    private String thumbnailUrl;

    @Column(name = "hope_story", nullable = false, columnDefinition = "TEXT")
    private String hopeStory;

    @Column(name = "hope_story_title", length = 200)
    private String hopeStoryTitle;

    @Column(name = "hope_story_image_url", length = 500)
    private String hopeStoryImageUrl;

    @Column(name = "sources", nullable = false, columnDefinition = "JSONB")
    private String sources;

    @Column(name = "global_per_second_rate", precision = 20, scale = 4)
    private java.math.BigDecimal globalPerSecondRate;

    @Column(name = "counter_unit", length = 50)
    private String counterUnit;

    @Column(name = "counter_label", length = 100)
    private String counterLabel;

    @Column(name = "display_order", nullable = false)
    private Integer displayOrder = 0;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;
}