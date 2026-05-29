package com.hackathon.hackathon_management_system.service;

import com.hackathon.hackathon_management_system.entity.Prize;
import com.hackathon.hackathon_management_system.repository.PrizeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PrizeService {

    private final PrizeRepository repository;

    public PrizeService(PrizeRepository repository) { this.repository = repository; }

    public List<Prize> findAll() { return repository.findByOrderByRankAsc(); }

    public Prize save(Prize prize) { return repository.save(prize); }

    public void delete(Long id) { repository.deleteById(id); }
}
