package com.hackathon.hackathon_management_system.repository;

import com.hackathon.hackathon_management_system.entity.Judge;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface JudgeRepository extends JpaRepository<Judge, Long> {
    Optional<Judge> findByEmail(String email);
}
