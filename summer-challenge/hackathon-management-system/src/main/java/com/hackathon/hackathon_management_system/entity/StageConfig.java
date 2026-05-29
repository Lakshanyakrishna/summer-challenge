package com.hackathon.hackathon_management_system.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "stage_configs")
public class StageConfig {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private int stageNumber;

    @Column(nullable = false)
    private String stageName;

    @Column(columnDefinition = "TEXT")
    private String requiredFields;

    @Column(nullable = false)
    private boolean active;

    public StageConfig() {}

    public StageConfig(int stageNumber, String stageName, String requiredFields) {
        this.stageNumber = stageNumber;
        this.stageName = stageName;
        this.requiredFields = requiredFields;
        this.active = true;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public int getStageNumber() { return stageNumber; }
    public void setStageNumber(int stageNumber) { this.stageNumber = stageNumber; }
    public String getStageName() { return stageName; }
    public void setStageName(String stageName) { this.stageName = stageName; }
    public String getRequiredFields() { return requiredFields; }
    public void setRequiredFields(String requiredFields) { this.requiredFields = requiredFields; }
    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
}
