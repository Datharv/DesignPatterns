package com.darunkar.design_patterns.facade;

import org.springframework.stereotype.Service;

@Service
public class InvoiceService {
    public void generateInvoice(String cartId) {
        System.out.println("Invoice generated for cart " + cartId);
    }
}