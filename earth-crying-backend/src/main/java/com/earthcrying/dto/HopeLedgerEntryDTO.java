package com.earthcrying.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HopeLedgerEntryDTO {

    private String id;
    private String title;
    private String description;
    private String category;
    private String metricName;
    private BigDecimal metricValue;
    private String metricUnit;
    private String region;
    private String countryCode;
    private String sourceName;
    private String sourceUrl;
    private OffsetDateTime recordedAt;
    private OffsetDateTime fetchedAt;
    private Boolean isLatest;
}