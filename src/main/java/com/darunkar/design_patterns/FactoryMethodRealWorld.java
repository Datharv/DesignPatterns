package com.darunkar.design_patterns;/*
 04_FactoryMethodRealWorld.java
 Real-world Factory Method demo with interface-based factories.
 - No switch statements
 - Fully decoupled client code
 - Easy to add new payment types
*/

// Product interface
interface Payment {
    void pay(double amount);
}

// Concrete products
class CreditCardPayment implements Payment {
    public void pay(double amount) {
        System.out.println("Paid " + amount + " using Credit Card");
    }
}

class PayPalPayment implements Payment {
    public void pay(double amount) {
        System.out.println("Paid " + amount + " using PayPal");
    }
}

class UPIPayment implements Payment {
    public void pay(double amount) {
        System.out.println("Paid " + amount + " using UPI");
    }
}

// Factory interface
interface PaymentFactory {
    Payment createPayment();
}

// Concrete factories
class CreditCardPaymentFactory implements PaymentFactory {
    public Payment createPayment() {
        return new CreditCardPayment();
    }
}

class PayPalPaymentFactory implements PaymentFactory {
    public Payment createPayment() {
        return new PayPalPayment();
    }
}

class UPIPaymentFactory implements PaymentFactory {
    public Payment createPayment() {
        return new UPIPayment();
    }
}

// Client / Demo
public class FactoryMethodRealWorld {
    public static void main(String[] args) {
        // Using concrete factories
        PaymentFactory ccFactory = new CreditCardPaymentFactory();
        PaymentFactory ppFactory = new PayPalPaymentFactory();
        PaymentFactory upiFactory = new UPIPaymentFactory();

        Payment p1 = ccFactory.createPayment();
        Payment p2 = ppFactory.createPayment();
        Payment p3 = upiFactory.createPayment();

        p1.pay(1000);
        p2.pay(2500);
        p3.pay(500);

        // Adding a new payment type is simple
        class CryptoPayment implements Payment {
            public void pay(double amount) {
                System.out.println("Paid " + amount + " using Cryptocurrency");
            }
        }
        class CryptoPaymentFactory implements PaymentFactory {
            public Payment createPayment() { return new CryptoPayment(); }
        }

        PaymentFactory cryptoFactory = new CryptoPaymentFactory();
        Payment cryptoPayment = cryptoFactory.createPayment();
        cryptoPayment.pay(1500);
    }
}
