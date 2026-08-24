package com.earthcrying.service;

import com.earthcrying.dto.BadgeDTO;
import com.earthcrying.dto.UserBadgeDTO;
import com.earthcrying.entity.Badge;
import com.earthcrying.entity.UserBadge;
import com.earthcrying.repository.BadgeRepository;
import com.earthcrying.repository.UserBadgeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BadgeService {

    private final BadgeRepository badgeRepository;
    private final UserBadgeRepository userBadgeRepository;

    public List<BadgeDTO> getAllBadges() {
        return badgeRepository.findByIsActiveTrue()
                .stream()
                .map(this::mapBadgeToDTO)
                .collect(Collectors.toList());
    }

    public BadgeDTO getBadgeByCode(String code) {
        return badgeRepository.findByCode(code)
                .map(this::mapBadgeToDTO)
                .orElseThrow(() -> new RuntimeException("Badge not found: " + code));
    }

    public List<BadgeDTO> getBadgesByCategory(String category) {
        return badgeRepository.findByCategory(category)
                .stream()
                .map(this::mapBadgeToDTO)
                .collect(Collectors.toList());
    }

    public List<UserBadgeDTO> getUserBadges(String userId) {
        return userBadgeRepository.findByUserId(UUID.fromString(userId))
                .stream()
                .map(this::mapUserBadgeToDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public UserBadgeDTO awardBadge(String userId, String badgeId) {
        var existing = userBadgeRepository.findByUserIdAndBadgeId(
                UUID.fromString(userId),
                UUID.fromString(badgeId));

        if (existing.isPresent()) {
            return mapUserBadgeToDTO(existing.get());
        }

        var userBadge = UserBadge.builder()
                .user(null)
                .badge(null)
                .earnedAt(java.time.OffsetDateTime.now())
                .build();

        var saved = userBadgeRepository.save(userBadge);
        return mapUserBadgeToDTO(saved);
    }

    private BadgeDTO mapBadgeToDTO(Badge badge) {
        return BadgeDTO.builder()
                .id(badge.getId().toString())
                .code(badge.getCode())
                .name(badge.getName())
                .description(badge.getDescription())
                .iconUrl(badge.getIconUrl())
                .category(badge.getCategory())
                .requirementType(badge.getRequirementType())
                .requirementValue(badge.getRequirementValue())
                .rarity(badge.getRarity())
                .isActive(badge.getIsActive())
                .createdAt(badge.getCreatedAt())
                .build();
    }

    private UserBadgeDTO mapUserBadgeToDTO(UserBadge userBadge) {
        return UserBadgeDTO.builder()
                .id(userBadge.getId().toString())
                .userId(userBadge.getUser() != null ? userBadge.getUser().getId().toString() : null)
                .badgeId(userBadge.getBadge() != null ? userBadge.getBadge().getId().toString() : null)
                .pledgeId(userBadge.getPledge() != null ? userBadge.getPledge().getId().toString() : null)
                .earnedAt(userBadge.getEarnedAt())
                .build();
    }
}