package com.hackathon.hackathon_management_system.repository;

import com.hackathon.hackathon_management_system.entity.Announcement;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AnnouncementRepository extends JpaRepository<Announcement, Long> {
    List<Announcement> findByOrderByCreatedAtDesc();
}
