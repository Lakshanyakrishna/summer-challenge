package com.hackathon.hackathon_management_system.service;

import com.hackathon.hackathon_management_system.entity.Setting;
import com.hackathon.hackathon_management_system.repository.SettingRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SettingService {

    private final SettingRepository settingRepository;

    public SettingService(SettingRepository settingRepository) {
        this.settingRepository = settingRepository;
    }

    public String get(String key, String defaultValue) {
        return settingRepository.findBySettingKey(key)
                .map(Setting::getSettingValue)
                .orElse(defaultValue);
    }

    public int getInt(String key, int defaultValue) {
        try { return Integer.parseInt(get(key, String.valueOf(defaultValue))); }
        catch (NumberFormatException e) { return defaultValue; }
    }

    public double getDouble(String key, double defaultValue) {
        try { return Double.parseDouble(get(key, String.valueOf(defaultValue))); }
        catch (NumberFormatException e) { return defaultValue; }
    }

    public boolean getBool(String key, boolean defaultValue) {
        return Boolean.parseBoolean(get(key, String.valueOf(defaultValue)));
    }

    public void set(String key, String value) {
        Setting setting = settingRepository.findBySettingKey(key)
                .orElse(new Setting(key, value, "general", key, "text"));
        setting.setSettingValue(value);
        settingRepository.save(setting);
    }

    public Setting save(Setting setting) { return settingRepository.save(setting); }

    public List<Setting> findByCategory(String category) { return settingRepository.findByCategory(category); }

    public List<Setting> findAll() { return settingRepository.findAll(); }

    public void deleteByKey(String key) {
        settingRepository.findBySettingKey(key).ifPresent(settingRepository::delete);
    }

    public void initDefault(String key, String value, String category, String label, String fieldType) {
        if (settingRepository.findBySettingKey(key).isEmpty()) {
            settingRepository.save(new Setting(key, value, category, label, fieldType));
        }
    }
}
