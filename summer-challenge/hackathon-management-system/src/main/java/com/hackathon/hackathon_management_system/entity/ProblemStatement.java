package com.hackathon.hackathon_management_system.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "problem_statements")
public class ProblemStatement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long hackathonId;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    private String techStack;

    private String difficulty;

    private String icon;

    public ProblemStatement() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getHackathonId() { return hackathonId; }
    public void setHackathonId(Long hackathonId) { this.hackathonId = hackathonId; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getTechStack() { return techStack; }
    public void setTechStack(String techStack) { this.techStack = techStack; }
    public String getDifficulty() { return difficulty; }
    public void setDifficulty(String difficulty) { this.difficulty = difficulty; }
    public String getIcon() { return icon; }
    public void setIcon(String icon) { this.icon = icon; }
}
