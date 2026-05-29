package com.hackathon.hackathon_management_system.repository;

import com.hackathon.hackathon_management_system.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    List<Payment> findByUserId(Long userId);
    List<Payment> findByHackathonId(Long hackathonId);
    Optional<Payment> findByUserIdAndHackathonId(Long userId, Long hackathonId);
    Optional<Payment> findByRazorpayOrderId(String razorpayOrderId);
    List<Payment> findByStatus(String status);
}
