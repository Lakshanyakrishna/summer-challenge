package com.hackathon.hackathon_management_system.dto;

import jakarta.validation.constraints.NotBlank;

public class SubmissionDto {

    private Long id;

    private Long teamId;

    private Long hackathonId;

    @NotBlank
    private String githubLink;

    private String demoLink;

    public SubmissionDto() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getTeamId() { return teamId; }
    public void setTeamId(Long teamId) { this.teamId = teamId; }
    public Long getHackathonId() { return hackathonId; }
    public void setHackathonId(Long hackathonId) { this.hackathonId = hackathonId; }
    public String getGithubLink() { return githubLink; }
    public void setGithubLink(String githubLink) { this.githubLink = githubLink; }
    public String getDemoLink() { return demoLink; }
    public void setDemoLink(String demoLink) { this.demoLink = demoLink; }
}
