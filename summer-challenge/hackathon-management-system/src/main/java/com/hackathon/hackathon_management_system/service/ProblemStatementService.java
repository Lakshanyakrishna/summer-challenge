package com.hackathon.hackathon_management_system.service;

import com.hackathon.hackathon_management_system.entity.ProblemStatement;
import com.hackathon.hackathon_management_system.repository.ProblemStatementRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProblemStatementService {

    private final ProblemStatementRepository repository;

    public ProblemStatementService(ProblemStatementRepository repository) {
        this.repository = repository;
    }

    public List<ProblemStatement> findByHackathon(Long hackathonId) {
        return repository.findByHackathonIdOrderByIdAsc(hackathonId);
    }

    public ProblemStatement save(ProblemStatement ps) { return repository.save(ps); }

    public void delete(Long id) { repository.deleteById(id); }

    public List<ProblemStatement> findAll() { return repository.findAll(); }
}
