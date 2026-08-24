package com.earthcrying.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnTransformer;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "footprint_results")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FootprintResult {

    @Id
    @GeneratedValue
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "session_id", length = 100)
    private String sessionId;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "category_scores", nullable = false, columnDefinition = "JSONB")
    @ColumnTransformer(write = "?::jsonb")
    private String categoryScores;

    @Column(name = "total_score", nullable = false)
    private Integer totalScore;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "top_categories", nullable = false, columnDefinition = "JSONB")
    @ColumnTransformer(write = "?::jsonb")
    private String topCategories;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "recommended_actions", nullable = false, columnDefinition = "JSONB")
    @ColumnTransformer(write = "?::jsonb")
    private String recommendedActions;

    @CreationTimestamp
    @Column(name = "completed_at", nullable = false, updatable = false)
    private OffsetDateTime completedAt;
}