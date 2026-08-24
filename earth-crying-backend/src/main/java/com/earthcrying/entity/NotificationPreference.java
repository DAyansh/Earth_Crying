package com.earthcrying.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "notification_preferences")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationPreference {

    @Id
    @GeneratedValue
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Column(name = "email_digest", nullable = false)
    private Boolean emailDigest = true;

    @Column(name = "email_streak_reminders", nullable = false)
    private Boolean emailStreakReminders = true;

    @Column(name = "email_earth_day", nullable = false)
    private Boolean emailEarthDay = true;

    @Column(name = "email_world_env_day", nullable = false)
    private Boolean emailWorldEnvDay = true;

    @Column(name = "push_notifications", nullable = false)
    private Boolean pushNotifications = false;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;
}