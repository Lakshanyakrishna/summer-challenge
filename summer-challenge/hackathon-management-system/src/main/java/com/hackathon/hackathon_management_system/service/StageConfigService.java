package com.hackathon.hackathon_management_system.service;

import com.hackathon.hackathon_management_system.entity.StageConfig;
import com.hackathon.hackathon_management_system.repository.StageConfigRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StageConfigService {

    private final StageConfigRepository repository;

    public StageConfigService(StageConfigRepository repository) { this.repository = repository; }

    public List<StageConfig> findAll() { return repository.findByOrderByStageNumberAsc(); }

    public List<StageConfig> findActive() { return repository.findByActiveTrueOrderByStageNumberAsc(); }

    public StageConfig save(StageConfig config) { return repository.save(config); }

    public void delete(Long id) { repository.deleteById(id); }

    public StageConfig findById(Long id) { return repository.findById(id).orElseThrow(() -> new RuntimeException("Stage not found")); }
}
