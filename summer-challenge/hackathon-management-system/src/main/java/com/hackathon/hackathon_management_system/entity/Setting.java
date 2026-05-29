package com.hackathon.hackathon_management_system.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "settings")
public class Setting {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String settingKey;

    @Column(columnDefinition = "TEXT")
    private String settingValue;

    @Column(nullable = false)
    private String category;

    private String label;

    private String fieldType;

    private String options;

    public Setting() {}

    public Setting(String settingKey, String settingValue, String category, String label, String fieldType) {
        this.settingKey = settingKey;
        this.settingValue = settingValue;
        this.category = category;
        this.label = label;
        this.fieldType = fieldType;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getSettingKey() { return settingKey; }
    public void setSettingKey(String settingKey) { this.settingKey = settingKey; }
    public String getSettingValue() { return settingValue; }
    public void setSettingValue(String settingValue) { this.settingValue = settingValue; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public String getLabel() { return label; }
    public void setLabel(String label) { this.label = label; }
    public String getFieldType() { return fieldType; }
    public void setFieldType(String fieldType) { this.fieldType = fieldType; }
    public String getOptions() { return options; }
    public void setOptions(String options) { this.options = options; }
}
