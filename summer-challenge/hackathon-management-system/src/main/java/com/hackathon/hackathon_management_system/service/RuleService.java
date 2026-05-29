package com.hackathon.hackathon_management_system.service;

import com.hackathon.hackathon_management_system.entity.Rule;
import com.hackathon.hackathon_management_system.repository.RuleRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RuleService {

    private final RuleRepository ruleRepository;

    public RuleService(RuleRepository ruleRepository) {
        this.ruleRepository = ruleRepository;
    }

    public List<Rule> findByHackathon(Long hackathonId) {
        return ruleRepository.findByHackathonIdOrderBySortOrderAsc(hackathonId);
    }

    public Rule save(Rule rule) {
        if (rule.getSortOrder() == null) {
            List<Rule> existing = findByHackathon(rule.getHackathonId());
            rule.setSortOrder(existing.size() + 1);
        }
        return ruleRepository.save(rule);
    }

    public Rule findById(Long id) {
        return ruleRepository.findById(id).orElseThrow(() -> new RuntimeException("Rule not found"));
    }

    public void delete(Long id) {
        ruleRepository.deleteById(id);
    }
}
