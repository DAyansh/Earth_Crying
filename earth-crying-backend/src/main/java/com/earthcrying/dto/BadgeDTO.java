package com.earthcrying.dto;

import lombok.*;

import java.time.OffsetDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BadgeDTO {

    private String id;
    private String code;
    private String name;
    private String description;
    private String iconUrl;
    private String category;
    private String requirementType;
    private Integer requirementValue;
    private String rarity;
    private Boolean isActive;
    private OffsetDateTime createdAt;
}