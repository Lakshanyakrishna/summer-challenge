package com.hackathon.hackathon_management_system.repository;

import com.hackathon.hackathon_management_system.entity.Submission;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface SubmissionRepository extends JpaRepository<Submission, Long> {
    List<Submission> findByHackathonId(Long hackathonId);
    List<Submission> findByTeamId(Long teamId);
    Optional<Submission> findByTeamIdAndStage(Long teamId, int stage);
    List<Submission> findByHackathonIdAndStage(Long hackathonId, int stage);
}
