package com.hackathon.hackathon_management_system.service;

import com.hackathon.hackathon_management_system.entity.Hackathon;
import com.hackathon.hackathon_management_system.entity.Team;
import com.hackathon.hackathon_management_system.entity.User;
import com.hackathon.hackathon_management_system.repository.TeamRepository;
import com.hackathon.hackathon_management_system.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Random;

@Service
public class TeamService {

    private final TeamRepository teamRepository;
    private final UserRepository userRepository;

    public TeamService(TeamRepository teamRepository, UserRepository userRepository) {
        this.teamRepository = teamRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public Team createTeam(String teamName, User leader, Hackathon hackathon) {
        String code;
        do { code = generateCode(); } while (teamRepository.existsByTeamCode(code));

        Team team = new Team();
        team.setTeamName(teamName);
        team.setTeamCode(code);
        team.setLeader(leader);
        team.setHackathon(hackathon);
        team.setStageStatus(1);
        team = teamRepository.save(team);

        leader.setTeam(team);
        userRepository.save(leader);
        return team;
    }

    @Transactional
    public Team joinTeam(String teamCode, User user) {
        Team team = teamRepository.findByTeamCode(teamCode)
                .orElseThrow(() -> new RuntimeException("Invalid team code"));
        user.setTeam(team);
        userRepository.save(user);
        return team;
    }

    public Team findByCode(String code) {
        return teamRepository.findByTeamCode(code)
                .orElseThrow(() -> new RuntimeException("Team not found"));
    }

    public Team findById(Long id) {
        return teamRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Team not found"));
    }

    public List<Team> findByLeader(Long leaderId) {
        return teamRepository.findByLeaderId(leaderId);
    }

    public List<Team> findByHackathon(Long hackathonId) {
        return teamRepository.findByHackathonId(hackathonId);
    }

    public List<Team> findAll() { return teamRepository.findAll(); }

    public void promoteTeam(Long teamId, int stage) {
        Team team = findById(teamId);
        team.setStageStatus(stage);
        teamRepository.save(team);
    }

    public long countMembers(Long teamId) {
        Team team = findById(teamId);
        return team.getMembers().stream().filter(m -> m.getId() != null).count();
    }

    private String generateCode() {
        String chars = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789";
        StringBuilder code = new StringBuilder();
        Random rnd = new Random();
        for (int i = 0; i < 6; i++) code.append(chars.charAt(rnd.nextInt(chars.length())));
        return code.toString();
    }
}
