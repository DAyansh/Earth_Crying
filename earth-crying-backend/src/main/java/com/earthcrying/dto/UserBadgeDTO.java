package com.earthcrying.dto;

import lombok.*;

import java.time.OffsetDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserBadgeDTO {

    private String id;
    private String userId;
    private String badgeId;
    private String pledgeId;
    private OffsetDateTime earnedAt;
}