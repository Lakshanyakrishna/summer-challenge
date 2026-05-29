package com.hackathon.hackathon_management_system.service;

import com.hackathon.hackathon_management_system.entity.Hackathon;
import com.hackathon.hackathon_management_system.repository.HackathonRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class HackathonService {

    private final HackathonRepository hackathonRepository;

    public HackathonService(HackathonRepository hackathonRepository) {
        this.hackathonRepository = hackathonRepository;
    }

    public Hackathon create(Hackathon hackathon) {
        return hackathonRepository.save(hackathon);
    }

    public Hackathon findById(Long id) {
        return hackathonRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Hackathon not found"));
    }

    public List<Hackathon> findAll() { return hackathonRepository.findAll(); }

    public List<Hackathon> findByStatus(String status) {
        return hackathonRepository.findByStatus(status);
    }

    public void updateStatuses() {
        List<Hackathon> all = hackathonRepository.findAll();
        LocalDate today = LocalDate.now();
        for (Hackathon h : all) {
            if ("UPCOMING".equals(h.getStatus()) && !today.isBefore(h.getStartDate()))
                h.setStatus("ONGOING");
            if ("ONGOING".equals(h.getStatus()) && today.isAfter(h.getEndDate()))
                h.setStatus("COMPLETED");
            hackathonRepository.save(h);
        }
    }

    public void delete(Long id) { hackathonRepository.deleteById(id); }
}
