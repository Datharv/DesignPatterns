package com.darunkar.design_patterns.facade;

import org.springframework.stereotype.Service;

@Service
public class OrderService {

    private final CheckoutFacade checkoutFacade;

    public OrderService(CheckoutFacade checkoutFacade) {
        this.checkoutFacade = checkoutFacade;
    }

    public boolean placeCustomerOrder(String cartId, String paymentDetails) {
        return checkoutFacade.placeOrder(cartId, paymentDetails);
    }
}
