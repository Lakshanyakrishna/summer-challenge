package com.hackathon.hackathon_management_system.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hackathon.hackathon_management_system.entity.Payment;
import com.hackathon.hackathon_management_system.entity.User;
import com.hackathon.hackathon_management_system.repository.PaymentRepository;
import com.hackathon.hackathon_management_system.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.HexFormat;

@RestController
@RequestMapping("/api/webhooks")
public class WebhookController {

    private final PaymentRepository paymentRepository;
    private final UserRepository userRepository;
    private final ObjectMapper objectMapper;

    @Value("${RAZORPAY_WEBHOOK_SECRET:}")
    private String webhookSecret;

    public WebhookController(PaymentRepository paymentRepository, UserRepository userRepository, ObjectMapper objectMapper) {
        this.paymentRepository = paymentRepository;
        this.userRepository = userRepository;
        this.objectMapper = objectMapper;
    }

    @PostMapping("/razorpay")
    @Transactional
    public ResponseEntity<?> handleRazorpayWebhook(
            @RequestBody String rawBody,
            @RequestHeader("X-Razorpay-Signature") String signature) {

        if (webhookSecret.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        String expectedSignature = hmacSha256(rawBody, webhookSecret);
        if (!expectedSignature.equals(signature)) {
            return ResponseEntity.badRequest().build();
        }

        try {
            JsonNode root = objectMapper.readTree(rawBody);
            String event = root.has("event") ? root.get("event").asText() : "";

            if (!"payment.captured".equals(event)) {
                return ResponseEntity.ok().build();
            }

            String orderId = root.path("payload").path("payment").path("entity").path("order_id").asText();
            if (orderId.isEmpty()) {
                return ResponseEntity.badRequest().build();
            }

            Payment payment = paymentRepository.findByRazorpayOrderId(orderId)
                    .orElse(null);
            if (payment == null) {
                return ResponseEntity.badRequest().build();
            }

            payment.setStatus("CAPTURED");
            payment.setVerifiedAt(LocalDateTime.now());
            paymentRepository.save(payment);

            User user = payment.getUser();
            user.setHasPaid(true);
            userRepository.save(user);

            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    private String hmacSha256(String data, String secret) {
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            SecretKeySpec keySpec = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
            mac.init(keySpec);
            byte[] hash = mac.doFinal(data.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(hash);
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            throw new RuntimeException("HMAC computation failed", e);
        }
    }
}
