package com.hackathon.hackathon_management_system.service;

import com.hackathon.hackathon_management_system.entity.Hackathon;
import com.hackathon.hackathon_management_system.entity.Payment;
import com.hackathon.hackathon_management_system.entity.User;
import com.hackathon.hackathon_management_system.repository.PaymentRepository;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final RazorpayClient razorpayClient;

    public PaymentService(PaymentRepository paymentRepository, @Autowired(required = false) RazorpayClient razorpayClient) {
        this.paymentRepository = paymentRepository;
        this.razorpayClient = razorpayClient;
    }

    public Payment createOrder(User user, Hackathon hackathon) {
        if (razorpayClient == null) {
            throw new RuntimeException("Razorpay is not configured. Set RAZORPAY_KEY_ID and RAZORPAY_KEY_SECRET environment variables.");
        }
        try {
            JSONObject orderRequest = new JSONObject();
            int amountInPaise = (int) (hackathon.getRegistrationFee() * 100);
            orderRequest.put("amount", amountInPaise);
            orderRequest.put("currency", "INR");
            orderRequest.put("receipt", "rcpt_" + System.currentTimeMillis());
            Order razorpayOrder = razorpayClient.orders.create(orderRequest);

            Payment payment = new Payment();
            payment.setUser(user);
            payment.setHackathon(hackathon);
            payment.setAmount(hackathon.getRegistrationFee());
            payment.setRazorpayOrderId(razorpayOrder.get("id"));
            payment.setStatus("CREATED");
            payment.setCreatedAt(LocalDateTime.now());
            return paymentRepository.save(payment);
        } catch (Exception e) {
            throw new RuntimeException("Failed to create Razorpay order", e);
        }
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
        return p != null && ("CAPTURED".equals(p.getStatus()) || "COMPLETED".equals(p.getStatus()));
    }
}
