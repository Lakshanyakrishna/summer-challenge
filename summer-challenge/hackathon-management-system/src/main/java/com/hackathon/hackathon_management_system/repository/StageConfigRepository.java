package com.hackathon.hackathon_management_system.repository;

import com.hackathon.hackathon_management_system.entity.StageConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface StageConfigRepository extends JpaRepository<StageConfig, Long> {
    List<StageConfig> findByOrderByStageNumberAsc();
    List<StageConfig> findByActiveTrueOrderByStageNumberAsc();
}
