package com.hackathon.hackathon_management_system.config;

import com.razorpay.RazorpayClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RazorpayConfig {

    private static final Logger log = LoggerFactory.getLogger(RazorpayConfig.class);

    @Value("${RAZORPAY_KEY_ID:}")
    private String keyId;

    @Value("${RAZORPAY_KEY_SECRET:}")
    private String keySecret;

    @Bean
    public RazorpayClient razorpayClient() throws Exception {
        if (keyId.isEmpty() || keySecret.isEmpty()) {
            log.warn("Razorpay credentials not configured. Payment features will be unavailable.");
            return null;
        }
        return new RazorpayClient(keyId, keySecret);
    }
}
