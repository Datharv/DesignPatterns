package com.darunkar.design_patterns.facade;

import org.springframework.stereotype.Component;

@Component
public class CheckoutFacade {

    private final InventoryService inventoryService;
    private final PaymentService paymentService;
    private final InvoiceService invoiceService;
    private final NotificationService notificationService;

    public CheckoutFacade(InventoryService inventoryService, PaymentService paymentService,
                          InvoiceService invoiceService, NotificationService notificationService) {
        this.inventoryService = inventoryService;
        this.paymentService = paymentService;
        this.invoiceService = invoiceService;
        this.notificationService = notificationService;
    }

        public boolean placeOrder(String cartId, String paymentDetails) {
        if (!inventoryService.reserveItems(cartId)) {
            System.out.println("Inventory not available!");
            return false;
        }
        if (!paymentService.processPayment(paymentDetails)) {
            System.out.println("Payment failed!");
            return false;
        }

        invoiceService.generateInvoice(cartId);
        notificationService.sendConfirmation(cartId);

        System.out.println("Order placed successfully!");
        return true;
    }
}
