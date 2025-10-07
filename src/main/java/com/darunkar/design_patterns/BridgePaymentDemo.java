package com.darunkar.design_patterns;

//Implementor - gateway bridge
interface PaymentGateway{
    void processPayment(double amount);
}

//concrete classes for gateway
class StripeGateway implements PaymentGateway{

    @Override
    public void processPayment(double amount) {
        System.out.println("Processing payment of ₹" + amount + " via Stripe gateway");
    }
}

class RazorPayGateway implements PaymentGateway{
    @Override
    public void processPayment(double amount) {
        System.out.println("Processing payment of ₹" + amount + " via RazorPay gateway");
    }
}


//Abstraction - payment method side of bridge
abstract class PaymentT{
   abstract void pay(double amount);
}

//concrete payment methods

class UpiPayment extends PaymentT{

    protected PaymentGateway paymentGateway;

    public UpiPayment(PaymentGateway paymentGateway) {
        this.paymentGateway = paymentGateway;
    }

    @Override
    void pay(double amount) {
        paymentGateway.processPayment(amount);
    }
}

class CCPayment extends PaymentT{

    protected PaymentGateway paymentGateway;
    public CCPayment(PaymentGateway paymentGateway) {
        this.paymentGateway = paymentGateway;
    }

    @Override
    void pay(double amount) {
        paymentGateway.processPayment(amount);
    }
}

public class BridgePaymentDemo {

    public static void main(String[] args) {
        PaymentT creditWithStripe = new CCPayment(new StripeGateway());
        creditWithStripe.pay(5000);

        PaymentT upiWithRazorPay = new UpiPayment(new RazorPayGateway());
        upiWithRazorPay.pay(7000);

    }
}
