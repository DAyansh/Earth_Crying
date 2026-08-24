package com.earthcrying.repository;

import com.earthcrying.entity.Impact;
import com.earthcrying.entity.ImpactCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ImpactRepository extends JpaRepository<Impact, java.util.UUID> {

    Optional<Impact> findByCategory(ImpactCategory category);

    List<Impact> findByIsActiveTrueOrderByDisplayOrderAsc();

    @Query("SELECT i FROM Impact i WHERE i.isActive = true ORDER BY i.displayOrder ASC")
    List<Impact> findAllActiveOrdered();
}