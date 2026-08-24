package com.earthcrying.service;

import com.earthcrying.dto.ImpactDTO;
import com.earthcrying.entity.Impact;
import com.earthcrying.entity.ImpactCategory;
import com.earthcrying.exception.BadRequestException;
import com.earthcrying.exception.ResourceNotFoundException;
import com.earthcrying.repository.ImpactRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ImpactService {

    private final ImpactRepository impactRepository;

    @Cacheable("impacts")
    public List<ImpactDTO> getAllImpacts() {
        return impactRepository.findByIsActiveTrueOrderByDisplayOrderAsc()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Cacheable("impacts")
    public ImpactDTO getImpactById(String id) {
        return impactRepository.findById(parseId(id))
                .map(this::mapToDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Impact not found with id: " + id));
    }

    @Cacheable("impacts")
    public ImpactDTO getImpactByCategory(ImpactCategory category) {
        return impactRepository.findByCategory(category)
                .map(this::mapToDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Impact not found for category: " + category));
    }

    @Transactional
    @CacheEvict(value = "impacts", allEntries = true)
    public ImpactDTO createImpact(ImpactDTO impactDTO) {
        if (impactDTO.getCategory() == null) {
            throw new BadRequestException("Category is required");
        }
        Impact impact = mapToEntity(impactDTO);
        Impact savedImpact = impactRepository.save(impact);
        return mapToDTO(savedImpact);
    }

    @Transactional
    @CacheEvict(value = "impacts", allEntries = true)
    public ImpactDTO updateImpact(String id, ImpactDTO impactDTO) {
        Impact existingImpact = impactRepository.findById(parseId(id))
                .orElseThrow(() -> new ResourceNotFoundException("Impact not found with id: " + id));

        if (impactDTO.getTitle() != null) {
            existingImpact.setTitle(impactDTO.getTitle());
        }
        if (impactDTO.getHiddenStat() != null) {
            existingImpact.setHiddenStat(impactDTO.getHiddenStat());
        }
        if (impactDTO.getExplanation() != null) {
            existingImpact.setExplanation(impactDTO.getExplanation());
        }
        if (impactDTO.getWhyInvisible() != null) {
            existingImpact.setWhyInvisible(impactDTO.getWhyInvisible());
        }
        if (impactDTO.getVideoAssetUrl() != null) {
            existingImpact.setVideoAssetUrl(impactDTO.getVideoAssetUrl());
        }
        if (impactDTO.getThumbnailUrl() != null) {
            existingImpact.setThumbnailUrl(impactDTO.getThumbnailUrl());
        }
        if (impactDTO.getHopeStory() != null) {
            existingImpact.setHopeStory(impactDTO.getHopeStory());
        }
        if (impactDTO.getHopeStoryTitle() != null) {
            existingImpact.setHopeStoryTitle(impactDTO.getHopeStoryTitle());
        }
        if (impactDTO.getHopeStoryImageUrl() != null) {
            existingImpact.setHopeStoryImageUrl(impactDTO.getHopeStoryImageUrl());
        }
        if (impactDTO.getSources() != null) {
            existingImpact.setSources(impactDTO.getSources());
        }
        if (impactDTO.getGlobalPerSecondRate() != null) {
            existingImpact.setGlobalPerSecondRate(impactDTO.getGlobalPerSecondRate());
        }
        if (impactDTO.getCounterUnit() != null) {
            existingImpact.setCounterUnit(impactDTO.getCounterUnit());
        }
        if (impactDTO.getCounterLabel() != null) {
            existingImpact.setCounterLabel(impactDTO.getCounterLabel());
        }
        if (impactDTO.getDisplayOrder() != null) {
            existingImpact.setDisplayOrder(impactDTO.getDisplayOrder());
        }
        if (impactDTO.getIsActive() != null) {
            existingImpact.setIsActive(impactDTO.getIsActive());
        }

        Impact updatedImpact = impactRepository.save(existingImpact);
        return mapToDTO(updatedImpact);
    }

    @Transactional
    @CacheEvict(value = "impacts", allEntries = true)
    public void deleteImpact(String id) {
        UUID impactId = parseId(id);
        if (!impactRepository.existsById(impactId)) {
            throw new ResourceNotFoundException("Impact not found with id: " + id);
        }
        impactRepository.deleteById(impactId);
    }

    private UUID parseId(String id) {
        try {
            return UUID.fromString(id);
        } catch (IllegalArgumentException e) {
            throw new BadRequestException("Invalid impact ID format: " + id);
        }
    }

    private ImpactDTO mapToDTO(Impact impact) {
        return ImpactDTO.builder()
                .id(impact.getId().toString())
                .category(impact.getCategory().name())
                .title(impact.getTitle())
                .hiddenStat(impact.getHiddenStat())
                .explanation(impact.getExplanation())
                .whyInvisible(impact.getWhyInvisible())
                .videoAssetUrl(impact.getVideoAssetUrl())
                .thumbnailUrl(impact.getThumbnailUrl())
                .hopeStory(impact.getHopeStory())
                .hopeStoryTitle(impact.getHopeStoryTitle())
                .hopeStoryImageUrl(impact.getHopeStoryImageUrl())
                .sources(impact.getSources())
                .globalPerSecondRate(impact.getGlobalPerSecondRate())
                .counterUnit(impact.getCounterUnit())
                .counterLabel(impact.getCounterLabel())
                .displayOrder(impact.getDisplayOrder())
                .isActive(impact.getIsActive())
                .createdAt(impact.getCreatedAt())
                .updatedAt(impact.getUpdatedAt())
                .build();
    }

    private Impact mapToEntity(ImpactDTO dto) {
        return Impact.builder()
                .id(dto.getId() != null ? java.util.UUID.fromString(dto.getId()) : java.util.UUID.randomUUID())
                .category(ImpactCategory.valueOf(dto.getCategory()))
                .title(dto.getTitle())
                .hiddenStat(dto.getHiddenStat())
                .explanation(dto.getExplanation())
                .whyInvisible(dto.getWhyInvisible())
                .videoAssetUrl(dto.getVideoAssetUrl())
                .thumbnailUrl(dto.getThumbnailUrl())
                .hopeStory(dto.getHopeStory())
                .hopeStoryTitle(dto.getHopeStoryTitle())
                .hopeStoryImageUrl(dto.getHopeStoryImageUrl())
                .sources(dto.getSources())
                .globalPerSecondRate(dto.getGlobalPerSecondRate())
                .counterUnit(dto.getCounterUnit())
                .counterLabel(dto.getCounterLabel())
                .displayOrder(dto.getDisplayOrder())
                .isActive(dto.getIsActive())
                .build();
    }
}