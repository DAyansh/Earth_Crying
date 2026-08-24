package com.earthcrying.service;

import com.earthcrying.dto.GeoImpactDTO;
import com.earthcrying.entity.GeoImpact;
import com.earthcrying.repository.GeoImpactRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GeoImpactService {

    private final GeoImpactRepository geoImpactRepository;

    @Cacheable("geo-impacts")
    public List<GeoImpactDTO> getAllGeoImpacts() {
        return geoImpactRepository.findAll()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public List<GeoImpactDTO> getByCountryCode(String countryCode) {
        return geoImpactRepository.findByCountryCodeOrderByDataYearDesc(countryCode)
                .stream()
                .limit(1)
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Cacheable("geo-impacts-latest")
    public List<GeoImpactDTO> getLatestData() {
        return geoImpactRepository.findLatestYearData()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public GeoImpactDTO saveGeoImpact(GeoImpactDTO dto) {
        var entity = GeoImpact.builder()
                .countryCode(dto.getCountryCode())
                .countryName(dto.getCountryName())
                .region(dto.getRegion())
                .deforestationRateHectaresPerYear(dto.getDeforestationRateHectaresPerYear())
                .aqiAvg(dto.getAqiAvg())
                .waterStressIndex(dto.getWaterStressIndex())
                .co2EmissionsMtPerYear(dto.getCo2EmissionsMtPerYear())
                .renewableEnergyPercent(dto.getRenewableEnergyPercent())
                .protectedLandPercent(dto.getProtectedLandPercent())
                .biodiversityIntactnessIndex(dto.getBiodiversityIntactnessIndex())
                .plasticWasteMtPerYear(dto.getPlasticWasteMtPerYear())
                .dataYear(dto.getDataYear())
                .sourceName(dto.getSourceName())
                .sourceUrl(dto.getSourceUrl())
                .build();

        var saved = geoImpactRepository.save(entity);
        return mapToDTO(saved);
    }

    private GeoImpactDTO mapToDTO(GeoImpact geoImpact) {
        return GeoImpactDTO.builder()
                .id(geoImpact.getId().toString())
                .countryCode(geoImpact.getCountryCode())
                .countryName(geoImpact.getCountryName())
                .region(geoImpact.getRegion())
                .deforestationRateHectaresPerYear(geoImpact.getDeforestationRateHectaresPerYear())
                .aqiAvg(geoImpact.getAqiAvg())
                .waterStressIndex(geoImpact.getWaterStressIndex())
                .co2EmissionsMtPerYear(geoImpact.getCo2EmissionsMtPerYear())
                .renewableEnergyPercent(geoImpact.getRenewableEnergyPercent())
                .protectedLandPercent(geoImpact.getProtectedLandPercent())
                .biodiversityIntactnessIndex(geoImpact.getBiodiversityIntactnessIndex())
                .plasticWasteMtPerYear(geoImpact.getPlasticWasteMtPerYear())
                .dataYear(geoImpact.getDataYear())
                .sourceName(geoImpact.getSourceName())
                .sourceUrl(geoImpact.getSourceUrl())
                .fetchedAt(geoImpact.getFetchedAt())
                .build();
    }
}