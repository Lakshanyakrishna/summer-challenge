package com.hackathon.hackathon_management_system.repository;

import com.hackathon.hackathon_management_system.entity.Score;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface ScoreRepository extends JpaRepository<Score, Long> {
    List<Score> findByTeamId(Long teamId);
    List<Score> findByJudgeId(Long judgeId);
    Optional<Score> findByJudgeIdAndTeamId(Long judgeId, Long teamId);
}
