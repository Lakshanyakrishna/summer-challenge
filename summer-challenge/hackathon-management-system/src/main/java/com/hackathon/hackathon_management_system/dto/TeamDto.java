package com.hackathon.hackathon_management_system.dto;

import jakarta.validation.constraints.NotBlank;

public class TeamDto {

    private Long id;

    @NotBlank
    private String teamName;

    private Long hackathonId;

    private Long leaderId;

    public TeamDto() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTeamName() { return teamName; }
    public void setTeamName(String teamName) { this.teamName = teamName; }
    public Long getHackathonId() { return hackathonId; }
    public void setHackathonId(Long hackathonId) { this.hackathonId = hackathonId; }
    public Long getLeaderId() { return leaderId; }
    public void setLeaderId(Long leaderId) { this.leaderId = leaderId; }
}
