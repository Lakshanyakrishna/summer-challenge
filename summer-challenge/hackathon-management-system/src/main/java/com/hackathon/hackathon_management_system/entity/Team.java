package com.hackathon.hackathon_management_system.entity;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "teams")
public class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String teamCode;

    @Column(nullable = false)
    private String teamName;

    @Column(nullable = false)
    private int stageStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hackathon_id", nullable = false)
    private Hackathon hackathon;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "leader_id")
    private User leader;

    @OneToMany(mappedBy = "team")
    private Set<User> members = new HashSet<>();

    public Team() { this.stageStatus = 1; }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTeamCode() { return teamCode; }
    public void setTeamCode(String teamCode) { this.teamCode = teamCode; }
    public String getTeamName() { return teamName; }
    public void setTeamName(String teamName) { this.teamName = teamName; }
    public int getStageStatus() { return stageStatus; }
    public void setStageStatus(int stageStatus) { this.stageStatus = stageStatus; }
    public Hackathon getHackathon() { return hackathon; }
    public void setHackathon(Hackathon hackathon) { this.hackathon = hackathon; }
    public User getLeader() { return leader; }
    public void setLeader(User leader) { this.leader = leader; }
    public Set<User> getMembers() { return members; }
    public void setMembers(Set<User> members) { this.members = members; }

    public int getMemberCount() {
        return (int) members.stream().filter(m -> m.getId() != null).count();
    }
}
