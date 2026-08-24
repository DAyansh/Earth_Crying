package com.earthcrying.service;

import com.earthcrying.dto.FootprintResultDTO;
import com.earthcrying.dto.request.FootprintQuizRequest;
import com.earthcrying.entity.FootprintResult;
import com.earthcrying.entity.ImpactCategory;
import com.earthcrying.entity.User;
import com.earthcrying.repository.FootprintResultRepository;
import com.earthcrying.repository.UserRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class FootprintCalculatorService {

    private static final TypeReference<Map<String, Integer>> MAP_STRING_INTEGER = new TypeReference<Map<String, Integer>>() {
    };
    private static final TypeReference<List<String>> LIST_STRING = new TypeReference<List<String>>() {
    };

    private final FootprintResultRepository footprintResultRepository;
    private final UserRepository userRepository;
    private final ObjectMapper objectMapper;

    @Transactional
    public FootprintResultDTO calculateFootprint(FootprintQuizRequest quizRequest, String userId) {
        var categoryScores = calculateCategoryScores(quizRequest);
        var totalScore = categoryScores.values().stream().mapToInt(Integer::intValue).sum();
        var topCategories = getTopCategories(categoryScores);
        var recommendedActions = getRecommendedActions(topCategories);

        User user = null;
        if (userId != null && !userId.isBlank()) {
            user = userRepository.findById(UUID.fromString(userId)).orElse(null);
        }

        var result = FootprintResult.builder()
                .user(user)
                .sessionId(UUID.randomUUID().toString())
                .categoryScores(writeJson(categoryScores))
                .totalScore(totalScore)
                .topCategories(writeJson(topCategories))
                .recommendedActions(writeJson(recommendedActions))
                .completedAt(OffsetDateTime.now())
                .build();

        var savedResult = footprintResultRepository.save(result);
        return mapToDTO(savedResult);
    }

    private Map<String, Integer> calculateCategoryScores(FootprintQuizRequest request) {
        var scores = new HashMap<String, Integer>();

        scores.put(ImpactCategory.DIGITAL_CARBON_FOOTPRINT.name(), request.getElectricityUsage() * 2);
        scores.put(ImpactCategory.TIRE_BRAKE_DUST.name(), request.getTransportationMode() * 3);
        scores.put(ImpactCategory.FAST_FASHION_MICROPLASTICS.name(), request.getShoppingHabits() * 2);
        scores.put(ImpactCategory.HIDDEN_WATER_FOOTPRINT.name(), request.getWaterUsage() * 4);
        scores.put(ImpactCategory.E_WASTE_RARE_EARTH_MINING.name(), request.getShoppingHabits() * 1);
        scores.put(ImpactCategory.INDOOR_VOCS_FRAGRANCE_CHEMICALS.name(), request.getElectricityUsage() * 1);

        return scores;
    }

    private List<String> getTopCategories(Map<String, Integer> scores) {
        return scores.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .limit(3)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    private List<String> getRecommendedActions(List<String> topCategories) {
        return topCategories.stream()
                .map(cat -> "Action for " + cat)
                .collect(Collectors.toList());
    }

    public FootprintResultDTO getPreviousResults(String userId) {
        if (userId == null || userId.isBlank()) {
            return null;
        }
        return footprintResultRepository.findByUserIdOrderByCompletedAtDesc(UUID.fromString(userId))
                .stream()
                .findFirst()
                .map(this::mapToDTO)
                .orElse(null);
    }

    private String writeJson(Object value) {
        try {
            return objectMapper.writeValueAsString(value);
        } catch (Exception e) {
            log.error("Failed to serialize footprint data to JSON", e);
            return "[]";
        }
    }

    private Map<String, Integer> readCategoryScores(String json) {
        try {
            return objectMapper.readValue(json, MAP_STRING_INTEGER);
        } catch (Exception e) {
            log.error("Failed to parse category scores JSON", e);
            return Map.of();
        }
    }

    private List<String> readStringList(String json) {
        try {
            return objectMapper.readValue(json, LIST_STRING);
        } catch (Exception e) {
            log.error("Failed to parse string list JSON", e);
            return List.of();
        }
    }

    private FootprintResultDTO mapToDTO(FootprintResult result) {
        return FootprintResultDTO.builder()
                .id(result.getId().toString())
                .userId(result.getUser() != null ? result.getUser().getId().toString() : null)
                .sessionId(result.getSessionId())
                .categoryScores(readCategoryScores(result.getCategoryScores()))
                .totalScore(result.getTotalScore())
                .topCategories(readStringList(result.getTopCategories()))
                .recommendedActions(readStringList(result.getRecommendedActions()))
                .completedAt(result.getCompletedAt())
                .build();
    }
}
