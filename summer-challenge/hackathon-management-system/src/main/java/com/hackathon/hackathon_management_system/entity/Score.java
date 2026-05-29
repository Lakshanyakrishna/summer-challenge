package com.hackathon.hackathon_management_system.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "scores")
public class Score {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "judge_id", nullable = false)
    private Judge judge;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id", nullable = false)
    private Team team;

    @Column(nullable = false)
    private Integer innovationScore;

    @Column(nullable = false)
    private Integer technicalScore;

    @Column(nullable = false)
    private Integer presentationScore;

    @Column(nullable = false)
    private Integer totalScore;

    public Score() {}

    @PrePersist
    @PreUpdate
    public void calculateTotal() {
        this.totalScore = (this.innovationScore != null ? this.innovationScore : 0)
                        + (this.technicalScore != null ? this.technicalScore : 0)
                        + (this.presentationScore != null ? this.presentationScore : 0);
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Judge getJudge() { return judge; }
    public void setJudge(Judge judge) { this.judge = judge; }
    public Team getTeam() { return team; }
    public void setTeam(Team team) { this.team = team; }
    public Integer getInnovationScore() { return innovationScore; }
    public void setInnovationScore(Integer innovationScore) { this.innovationScore = innovationScore; }
    public Integer getTechnicalScore() { return technicalScore; }
    public void setTechnicalScore(Integer technicalScore) { this.technicalScore = technicalScore; }
    public Integer getPresentationScore() { return presentationScore; }
    public void setPresentationScore(Integer presentationScore) { this.presentationScore = presentationScore; }
    public Integer getTotalScore() { return totalScore; }
    public void setTotalScore(Integer totalScore) { this.totalScore = totalScore; }
}
