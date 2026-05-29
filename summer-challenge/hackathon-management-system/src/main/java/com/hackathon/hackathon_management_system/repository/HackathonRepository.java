package com.hackathon.hackathon_management_system.repository;

import com.hackathon.hackathon_management_system.entity.Hackathon;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface HackathonRepository extends JpaRepository<Hackathon, Long> {
    List<Hackathon> findByOrganizerId(Long organizerId);
    List<Hackathon> findByStatus(String status);
}
