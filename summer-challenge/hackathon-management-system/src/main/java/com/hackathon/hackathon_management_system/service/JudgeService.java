package com.hackathon.hackathon_management_system.service;

import com.hackathon.hackathon_management_system.entity.Judge;
import com.hackathon.hackathon_management_system.repository.JudgeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JudgeService {

    private final JudgeRepository judgeRepository;

    public JudgeService(JudgeRepository judgeRepository) {
        this.judgeRepository = judgeRepository;
    }

    public Judge createJudge(String name, String email) {
        Judge judge = new Judge();
        judge.setName(name);
        judge.setEmail(email);
        return judgeRepository.save(judge);
    }

    public List<Judge> findAll() {
        return judgeRepository.findAll();
    }

    public Judge findById(Long id) {
        return judgeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Judge not found"));
    }
}
