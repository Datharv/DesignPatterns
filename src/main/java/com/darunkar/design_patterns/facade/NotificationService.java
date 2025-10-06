package com.darunkar.design_patterns.facade;

import org.springframework.stereotype.Service;

@Service
public class NotificationService {
    public void sendConfirmation(String cartId) {
        System.out.println("Confirmation sent for cart " + cartId);
    }
}