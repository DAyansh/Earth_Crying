package com.earthcrying.service;

import com.earthcrying.dto.HopeLedgerEntryDTO;
import com.earthcrying.entity.HopeLedgerEntry;
import com.earthcrying.repository.HopeLedgerEntryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class HopeLedgerService {

    private final HopeLedgerEntryRepository hopeLedgerEntryRepository;

    @Cacheable("hope-ledger-entries")
    public List<HopeLedgerEntryDTO> getAllEntries() {
        return hopeLedgerEntryRepository.findAll()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Cacheable("hope-ledger-latest")
    public List<HopeLedgerEntryDTO> getLatestEntries() {
        return hopeLedgerEntryRepository.findByIsLatestTrue()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public List<HopeLedgerEntryDTO> getByCategory(String category) {
        return hopeLedgerEntryRepository.findByCategoryOrderByRecordedAtDesc(category)
                .stream()
                .limit(10)
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public HopeLedgerEntryDTO getEntryById(String id) {
        return hopeLedgerEntryRepository.findById(UUID.fromString(id))
                .map(this::mapToDTO)
                .orElseThrow(() -> new RuntimeException("Hope ledger entry not found: " + id));
    }

    @Transactional
    public HopeLedgerEntryDTO saveHopeLedgerEntry(HopeLedgerEntryDTO dto) {
        var entity = HopeLedgerEntry.builder()
                .title(dto.getTitle())
                .description(dto.getDescription())
                .category(dto.getCategory())
                .metricName(dto.getMetricName())
                .metricValue(dto.getMetricValue())
                .metricUnit(dto.getMetricUnit())
                .region(dto.getRegion())
                .countryCode(dto.getCountryCode())
                .sourceName(dto.getSourceName())
                .sourceUrl(dto.getSourceUrl())
                .recordedAt(dto.getRecordedAt() != null ? dto.getRecordedAt() : OffsetDateTime.now())
                .isLatest(true)
                .build();

        var saved = hopeLedgerEntryRepository.save(entity);
        return mapToDTO(saved);
    }

    @Transactional
    public void updateLatestFlags(String category) {
        hopeLedgerEntryRepository.findByCategoryOrderByRecordedAtDesc(category)
                .stream()
                .findFirst()
                .ifPresent(latest -> {
                    hopeLedgerEntryRepository.findByCategory(category)
                            .forEach(entry -> {
                                entry.setIsLatest(entry.getId().equals(latest.getId()));
                                hopeLedgerEntryRepository.save(entry);
                            });
                });
    }

    private HopeLedgerEntryDTO mapToDTO(HopeLedgerEntry entry) {
        return HopeLedgerEntryDTO.builder()
                .id(entry.getId().toString())
                .title(entry.getTitle())
                .description(entry.getDescription())
                .category(entry.getCategory())
                .metricName(entry.getMetricName())
                .metricValue(entry.getMetricValue())
                .metricUnit(entry.getMetricUnit())
                .region(entry.getRegion())
                .countryCode(entry.getCountryCode())
                .sourceName(entry.getSourceName())
                .sourceUrl(entry.getSourceUrl())
                .recordedAt(entry.getRecordedAt())
                .fetchedAt(entry.getFetchedAt())
                .isLatest(entry.getIsLatest())
                .build();
    }
}