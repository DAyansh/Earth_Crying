package com.earthcrying.service;

import com.earthcrying.dto.ConfessionDTO;
import com.earthcrying.entity.Confession;
import com.earthcrying.entity.ImpactCategory;
import com.earthcrying.repository.ConfessionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ConfessionService {

    private final ConfessionRepository confessionRepository;

    private static final Set<String> PROFANITY_WORDS = Set.of(
        "fuck", "shit", "damn", "bitch", "ass", "cunt", "dick", "pussy",
        "bastard", "motherfucker", "whore", "slut", "fag", "retard",
        "nigger", "kike", "spic", "chink", "gook", "towelhead",
        "rape", "molest", "pedophile", "terrorist", "bomb", "kill",
        "murder", "suicide", "hang yourself", "kill yourself", "kys"
    );

    private static final Pattern PROFANITY_PATTERN = Pattern.compile(
        "\\b(" + String.join("|", PROFANITY_WORDS.stream().map(Pattern::quote).toList()) + ")\\b",
        Pattern.CASE_INSENSITIVE
    );

    private static final int MAX_CONFESSION_LENGTH = 500;
    private static final int MIN_CONFESSION_LENGTH = 10;

    @Transactional
    public ConfessionDTO submitConfession(String content, ImpactCategory impactCategory, String ipHash, String userAgent) {
        if (content == null || content.trim().isEmpty()) {
            throw new IllegalArgumentException("Confession content cannot be empty");
        }

        String trimmedContent = content.trim();
        if (trimmedContent.length() < MIN_CONFESSION_LENGTH) {
            throw new IllegalArgumentException("Confession must be at least " + MIN_CONFESSION_LENGTH + " characters");
        }
        if (trimmedContent.length() > MAX_CONFESSION_LENGTH) {
            throw new IllegalArgumentException("Confession cannot exceed " + MAX_CONFESSION_LENGTH + " characters");
        }

        String filteredContent = filterProfanity(trimmedContent);
        boolean isFlagged = !filteredContent.equals(trimmedContent);

        if (isFlagged) {
            log.warn("Confession flagged for profanity: {}", trimmedContent);
        }

        var confession = Confession.builder()
                .content(filteredContent)
                .impactCategory(impactCategory)
                .isApproved(!isFlagged)
                .isFlagged(isFlagged)
                .flagReason(isFlagged ? "Profanity detected" : null)
                .ipHash(ipHash)
                .userAgent(userAgent)
                .build();

        var saved = confessionRepository.save(confession);
        return mapToDTO(saved);
    }

    public List<ConfessionDTO> getApprovedConfessions(int limit) {
        return confessionRepository.findByIsApprovedTrueOrderBySubmittedAtDesc()
                .stream()
                .limit(limit)
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public List<ConfessionDTO> getApprovedConfessionsByCategory(ImpactCategory category, int limit) {
        return confessionRepository.findByIsApprovedTrueAndImpactCategoryOrderBySubmittedAtDesc(category)
                .stream()
                .limit(limit)
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public long getApprovedCount() {
        return confessionRepository.countByIsApprovedTrue();
    }

    public long getFlaggedCount() {
        return confessionRepository.countByIsFlaggedTrue();
    }

    private String filterProfanity(String content) {
        return PROFANITY_PATTERN.matcher(content).replaceAll(match -> "*".repeat(match.group().length()));
    }

    private ConfessionDTO mapToDTO(Confession confession) {
        return ConfessionDTO.builder()
                .id(confession.getId().toString())
                .content(confession.getContent())
                .impactCategory(confession.getImpactCategory())
                .isApproved(confession.getIsApproved())
                .isFlagged(confession.getIsFlagged())
                .flagReason(confession.getFlagReason())
                .submittedAt(confession.getSubmittedAt())
                .approvedAt(confession.getApprovedAt())
                .approvedBy(confession.getApprovedBy() != null ? confession.getApprovedBy().toString() : null)
                .ipHash(confession.getIpHash())
                .userAgent(confession.getUserAgent())
                .build();
    }
}