package com.hackathon.hackathon_management_system.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "hackathons")
public class Hackathon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private LocalDate startDate;

    @Column(nullable = false)
    private LocalDate endDate;

    private String location;

    @Column(nullable = false)
    private String status;

    @Column(nullable = false)
    private double registrationFee;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organizer_id")
    private User organizer;

    private String bannerImage;
    private String mode;
    private String meetingLink;
    private Integer maxTeams;
    private Integer minTeamSize;
    private Integer maxTeamSize;
    private LocalDate registrationDeadline;
    private LocalDate stage1Deadline;
    private LocalDate stage2Deadline;
    private LocalDate stage3Deadline;
    private LocalDate resultDate;

    public Hackathon() { this.registrationFee = 249.0; }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }
    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public double getRegistrationFee() { return registrationFee; }
    public void setRegistrationFee(double registrationFee) { this.registrationFee = registrationFee; }
    public User getOrganizer() { return organizer; }
    public void setOrganizer(User organizer) { this.organizer = organizer; }
    public String getBannerImage() { return bannerImage; }
    public void setBannerImage(String bannerImage) { this.bannerImage = bannerImage; }
    public String getMode() { return mode; }
    public void setMode(String mode) { this.mode = mode; }
    public String getMeetingLink() { return meetingLink; }
    public void setMeetingLink(String meetingLink) { this.meetingLink = meetingLink; }
    public Integer getMaxTeams() { return maxTeams; }
    public void setMaxTeams(Integer maxTeams) { this.maxTeams = maxTeams; }
    public Integer getMinTeamSize() { return minTeamSize; }
    public void setMinTeamSize(Integer minTeamSize) { this.minTeamSize = minTeamSize; }
    public Integer getMaxTeamSize() { return maxTeamSize; }
    public void setMaxTeamSize(Integer maxTeamSize) { this.maxTeamSize = maxTeamSize; }
    public LocalDate getRegistrationDeadline() { return registrationDeadline; }
    public void setRegistrationDeadline(LocalDate registrationDeadline) { this.registrationDeadline = registrationDeadline; }
    public LocalDate getStage1Deadline() { return stage1Deadline; }
    public void setStage1Deadline(LocalDate stage1Deadline) { this.stage1Deadline = stage1Deadline; }
    public LocalDate getStage2Deadline() { return stage2Deadline; }
    public void setStage2Deadline(LocalDate stage2Deadline) { this.stage2Deadline = stage2Deadline; }
    public LocalDate getStage3Deadline() { return stage3Deadline; }
    public void setStage3Deadline(LocalDate stage3Deadline) { this.stage3Deadline = stage3Deadline; }
    public LocalDate getResultDate() { return resultDate; }
    public void setResultDate(LocalDate resultDate) { this.resultDate = resultDate; }
}
