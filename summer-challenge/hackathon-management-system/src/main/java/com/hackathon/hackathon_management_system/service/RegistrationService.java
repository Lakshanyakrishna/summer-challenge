package com.hackathon.hackathon_management_system.service;

import com.hackathon.hackathon_management_system.entity.Hackathon;
import com.hackathon.hackathon_management_system.entity.Registration;
import com.hackathon.hackathon_management_system.entity.User;
import com.hackathon.hackathon_management_system.repository.RegistrationRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class RegistrationService {

    private final RegistrationRepository registrationRepository;

    public RegistrationService(RegistrationRepository registrationRepository) {
        this.registrationRepository = registrationRepository;
    }

    public Registration register(User user, Hackathon hackathon) {
        Registration registration = new Registration();
        registration.setUser(user);
        registration.setHackathon(hackathon);
        registration.setRegistrationDate(LocalDateTime.now());
        registration.setPaymentStatus("PENDING");
        return registrationRepository.save(registration);
    }

    public List<Registration> findByUser(Long userId) {
        return registrationRepository.findByUserId(userId);
    }

    public List<Registration> findByHackathon(Long hackathonId) {
        return registrationRepository.findByHackathonId(hackathonId);
    }

    public Optional<Registration> findByUserIdAndHackathonId(Long userId, Long hackathonId) {
        return registrationRepository.findByUserIdAndHackathonId(userId, hackathonId);
    }

    public List<Registration> findAll() {
        return registrationRepository.findAll();
    }

    public boolean isRegistered(Long userId, Long hackathonId) {
        return registrationRepository.findByUserIdAndHackathonId(userId, hackathonId).isPresent();
    }

    public void unregister(Long userId, Long hackathonId) {
        registrationRepository.findByUserIdAndHackathonId(userId, hackathonId)
                .ifPresent(registrationRepository::delete);
    }

    public Registration updateRegistration(Registration registration) {
        return registrationRepository.save(registration);
    }
}
