package com.hackathon.hackathon_management_system.repository;

import com.hackathon.hackathon_management_system.entity.Registration;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface RegistrationRepository extends JpaRepository<Registration, Long> {
    List<Registration> findByUserId(Long userId);
    List<Registration> findByHackathonId(Long hackathonId);
    Optional<Registration> findByUserIdAndHackathonId(Long userId, Long hackathonId);
    List<Registration> findByPaymentStatus(String paymentStatus);
}
