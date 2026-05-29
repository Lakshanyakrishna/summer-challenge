package com.hackathon.hackathon_management_system.service;

import com.hackathon.hackathon_management_system.entity.Announcement;
import com.hackathon.hackathon_management_system.repository.AnnouncementRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AnnouncementService {

    private final AnnouncementRepository repository;

    public AnnouncementService(AnnouncementRepository repository) { this.repository = repository; }

    public List<Announcement> findAll() { return repository.findByOrderByCreatedAtDesc(); }

    public Announcement save(Announcement announcement) { return repository.save(announcement); }

    public void delete(Long id) { repository.deleteById(id); }
}
