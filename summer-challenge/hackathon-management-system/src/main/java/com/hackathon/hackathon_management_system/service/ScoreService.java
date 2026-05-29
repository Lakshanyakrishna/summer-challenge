package com.hackathon.hackathon_management_system.service;

import com.hackathon.hackathon_management_system.entity.Judge;
import com.hackathon.hackathon_management_system.entity.Score;
import com.hackathon.hackathon_management_system.entity.Team;
import com.hackathon.hackathon_management_system.repository.ScoreRepository;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ScoreService {

    private final ScoreRepository scoreRepository;

    public ScoreService(ScoreRepository scoreRepository) {
        this.scoreRepository = scoreRepository;
    }

    public Score createScore(Judge judge, Team team, Integer innovation, Integer technical, Integer presentation) {
        Score score = new Score();
        score.setJudge(judge);
        score.setTeam(team);
        score.setInnovationScore(innovation);
        score.setTechnicalScore(technical);
        score.setPresentationScore(presentation);
        score.calculateTotal();
        return scoreRepository.save(score);
    }

    public List<Score> findByTeam(Long teamId) {
        return scoreRepository.findByTeamId(teamId);
    }

    public List<Score> findAll() {
        return scoreRepository.findAll();
    }

    public List<Object[]> getLeaderboard() {
        List<Score> allScores = scoreRepository.findAll();
        Map<Team, Integer> totals = allScores.stream()
                .collect(Collectors.groupingBy(
                        Score::getTeam,
                        Collectors.summingInt(Score::getTotalScore)
                ));
        return totals.entrySet().stream()
                .sorted(Map.Entry.<Team, Integer>comparingByValue().reversed())
                .map(e -> new Object[]{e.getKey(), e.getValue()})
                .collect(Collectors.toList());
    }
}
