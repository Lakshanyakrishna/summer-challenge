package com.hackathon.hackathon_management_system.repository;

import com.hackathon.hackathon_management_system.entity.Prize;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PrizeRepository extends JpaRepository<Prize, Long> {
    List<Prize> findByOrderByRankAsc();
}
