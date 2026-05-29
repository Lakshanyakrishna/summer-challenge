package com.hackathon.hackathon_management_system.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "rules")
public class Rule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long hackathonId;

    @Column(nullable = false)
    private String content;

    private Integer sortOrder;

    public Rule() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getHackathonId() { return hackathonId; }
    public void setHackathonId(Long hackathonId) { this.hackathonId = hackathonId; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public Integer getSortOrder() { return sortOrder; }
    public void setSortOrder(Integer sortOrder) { this.sortOrder = sortOrder; }
}
