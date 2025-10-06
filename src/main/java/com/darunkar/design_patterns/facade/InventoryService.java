package com.darunkar.design_patterns.facade;

import org.springframework.stereotype.Service;

@Service
public class InventoryService {
    public boolean reserveItems(String cartId) {
        System.out.println("Reserving items for cart " + cartId);
        return true;
    }
}
