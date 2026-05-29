package com.hackathon.hackathon_management_system.repository;

import com.hackathon.hackathon_management_system.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface TeamRepository extends JpaRepository<Team, Long> {
    Optional<Team> findByTeamCode(String teamCode);
    List<Team> findByHackathonId(Long hackathonId);
    List<Team> findByLeaderId(Long leaderId);
    boolean existsByTeamCode(String teamCode);
}
