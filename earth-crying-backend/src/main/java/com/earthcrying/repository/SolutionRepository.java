package com.earthcrying.repository;

import com.earthcrying.entity.Impact;
import com.earthcrying.entity.Solution;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface SolutionRepository extends JpaRepository<Solution, UUID> {

    List<Solution> findByImpactIdOrderByDisplayOrderAsc(UUID impactId);

    List<Solution> findByImpactIdAndIsActiveTrueOrderByDisplayOrderAsc(UUID impactId);

    List<Solution> findByEffortLevel(com.earthcrying.entity.EffortLevel effortLevel);

    List<Solution> findByActionScale(com.earthcrying.entity.ActionScale actionScale);
}