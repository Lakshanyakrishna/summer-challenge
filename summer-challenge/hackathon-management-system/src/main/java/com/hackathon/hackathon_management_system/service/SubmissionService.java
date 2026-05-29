package com.hackathon.hackathon_management_system.service;

import com.hackathon.hackathon_management_system.entity.Hackathon;
import com.hackathon.hackathon_management_system.entity.Submission;
import com.hackathon.hackathon_management_system.entity.Team;
import com.hackathon.hackathon_management_system.repository.SubmissionRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class SubmissionService {

    private final SubmissionRepository submissionRepository;

    public SubmissionService(SubmissionRepository submissionRepository) {
        this.submissionRepository = submissionRepository;
    }

    public Submission submit(Team team, Hackathon hackathon, int stage,
                             String videoUrl, String pptUrl, String githubUrl, String deployUrl) {
        Submission submission = new Submission();
        submission.setTeam(team);
        submission.setHackathon(hackathon);
        submission.setStage(stage);
        submission.setVideoUrl(videoUrl);
        submission.setPptUrl(pptUrl);
        submission.setGithubUrl(githubUrl);
        submission.setDeployUrl(deployUrl);
        submission.setSubmittedAt(LocalDateTime.now());
        return submissionRepository.save(submission);
    }

    public Submission findByTeamAndStage(Long teamId, int stage) {
        return submissionRepository.findByTeamIdAndStage(teamId, stage).orElse(null);
    }

    public List<Submission> findByTeam(Long teamId) {
        return submissionRepository.findByTeamId(teamId);
    }

    public List<Submission> findByHackathonAndStage(Long hackathonId, int stage) {
        return submissionRepository.findByHackathonIdAndStage(hackathonId, stage);
    }

    public List<Submission> findByHackathon(Long hackathonId) {
        return submissionRepository.findByHackathonId(hackathonId);
    }

    public List<Submission> findAll() { return submissionRepository.findAll(); }
}
