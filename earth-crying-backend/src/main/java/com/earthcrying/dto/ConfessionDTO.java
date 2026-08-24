package com.earthcrying.dto;

import com.earthcrying.entity.ImpactCategory;
import lombok.*;

import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConfessionDTO {

    private String id;
    private String content;
    private ImpactCategory impactCategory;
    private Boolean isApproved;
    private Boolean isFlagged;
    private String flagReason;
    private OffsetDateTime submittedAt;
    private OffsetDateTime approvedAt;
    private String approvedBy;
    private String ipHash;
    private String userAgent;
}