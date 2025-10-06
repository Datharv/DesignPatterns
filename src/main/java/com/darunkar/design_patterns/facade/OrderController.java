package com.darunkar.design_patterns.facade;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/place")
    public String placeOrder(@RequestParam String cartId, @RequestParam String paymentDetails) {
        boolean success = orderService.placeCustomerOrder(cartId, paymentDetails);
        return success ? "Order placed" : "Order failed";
    }
}
