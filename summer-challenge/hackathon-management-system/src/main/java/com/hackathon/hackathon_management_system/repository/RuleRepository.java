package com.hackathon.hackathon_management_system.repository;

import com.hackathon.hackathon_management_system.entity.Rule;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface RuleRepository extends JpaRepository<Rule, Long> {
    List<Rule> findByHackathonIdOrderBySortOrderAsc(Long hackathonId);
    void deleteByHackathonId(Long hackathonId);
}
