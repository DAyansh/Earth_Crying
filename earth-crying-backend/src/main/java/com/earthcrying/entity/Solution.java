package com.earthcrying.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "solutions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Solution {

    @Id
    @GeneratedValue
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "impact_id", nullable = false)
    private Impact impact;

    @Column(name = "impact_id", insertable = false, updatable = false)
    private UUID impactId;

    @Column(name = "title", nullable = false, length = 200)
    private String title;

    @Column(name = "description", nullable = false, columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "effort_level", nullable = false)
    private EffortLevel effortLevel;

    @Enumerated(EnumType.STRING)
    @Column(name = "action_scale", nullable = false)
    private ActionScale actionScale;

    @Column(name = "impact_score", nullable = false)
    private Integer impactScore = 1;

    @Column(name = "co2_saved_kg_per_year", precision = 10, scale = 2)
    private BigDecimal co2SavedKgPerYear;

    @Column(name = "water_saved_liters_per_year", precision = 10, scale = 2)
    private BigDecimal waterSavedLitersPerYear;

    @Column(name = "money_saved_usd_per_year", precision = 10, scale = 2)
    private BigDecimal moneySavedUsdPerYear;

    @Column(name = "external_resource_url", length = 500)
    private String externalResourceUrl;

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