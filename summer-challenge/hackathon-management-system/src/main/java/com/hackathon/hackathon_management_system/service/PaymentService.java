package com.hackathon.hackathon_management_system.service;

import com.hackathon.hackathon_management_system.entity.Hackathon;
import com.hackathon.hackathon_management_system.entity.Payment;
import com.hackathon.hackathon_management_system.entity.User;
import com.hackathon.hackathon_management_system.repository.PaymentRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;

    public PaymentService(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    public Payment createOrder(User user, Hackathon hackathon) {
        Payment payment = new Payment();
        payment.setUser(user);
        payment.setHackathon(hackathon);
        payment.setAmount(hackathon.getRegistrationFee());
        payment.setRazorpayOrderId("order_" + UUID.randomUUID().toString().substring(0, 12));
        payment.setStatus("CREATED");
        payment.setCreatedAt(LocalDateTime.now());
        return paymentRepository.save(payment);
    }

    public Payment verifyPayment(String razorpayOrderId, String razorpayPaymentId, String razorpaySignature) {
        Payment payment = paymentRepository.findByRazorpayOrderId(razorpayOrderId)
                .orElseThrow(() -> new RuntimeException("Payment order not found"));

        payment.setRazorpayPaymentId(razorpayPaymentId);
        payment.setRazorpaySignature(razorpaySignature);
        payment.setStatus("COMPLETED");
        payment.setVerifiedAt(LocalDateTime.now());

        User user = payment.getUser();
        user.setHasPaid(true);

        return paymentRepository.save(payment);
    }

    public Payment findByOrderId(String orderId) {
        return paymentRepository.findByRazorpayOrderId(orderId)
                .orElseThrow(() -> new RuntimeException("Payment not found"));
    }

    public Payment findByUserAndHackathon(Long userId, Long hackathonId) {
        return paymentRepository.findByUserIdAndHackathonId(userId, hackathonId).orElse(null);
    }

    public List<Payment> findByUser(Long userId) {
        return paymentRepository.findByUserId(userId);
    }

    public List<Payment> findAll() { return paymentRepository.findAll(); }

    public boolean hasPaid(Long userId, Long hackathonId) {
        Payment p = findByUserAndHackathon(userId, hackathonId);
        return p != null && "COMPLETED".equals(p.getStatus());
    }
}
