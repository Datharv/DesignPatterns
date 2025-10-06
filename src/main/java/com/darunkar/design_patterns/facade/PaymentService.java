package com.darunkar.design_patterns.facade;

import org.springframework.stereotype.Service;

@Service
public class PaymentService {
    public boolean processPayment(String paymentDetails) {
        System.out.println("Processing payment: " + paymentDetails);
        return true;
    }
}