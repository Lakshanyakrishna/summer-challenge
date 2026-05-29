package com.hackathon.hackathon_management_system.repository;

import com.hackathon.hackathon_management_system.entity.ProblemStatement;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ProblemStatementRepository extends JpaRepository<ProblemStatement, Long> {
    List<ProblemStatement> findByHackathonIdOrderByIdAsc(Long hackathonId);
}
