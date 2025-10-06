package com.darunkar.design_patterns;

interface PaymentProcessor{
    void pay(double amount);
}

class StripeAPI{
    public void makePayment(int cents){
        System.out.println("Stripe: Payment of $" + (cents / 100.0) + " processed.");
    }
}

class PayPalAPI {
    public void sendPayment(String currency, double amount) {
        System.out.println("PayPal: Payment of " + currency + " " + amount + " processed.");
    }
}

class StripeAdapter implements  PaymentProcessor{

    private final StripeAPI stripeAPI;
    StripeAdapter(StripeAPI stripeAPI){
        this.stripeAPI = stripeAPI;
    }

    @Override
    public void pay(double amount) {
        int cents = (int)amount*100;
        stripeAPI.makePayment(cents);
    }
}

class PayPalAdapter implements PaymentProcessor{
    private final PayPalAPI payPalAPI;
    PayPalAdapter(PayPalAPI payPalAPI){
        this.payPalAPI = payPalAPI;
    }

    @Override
    public void pay(double amount){
        String currency = "INR";
        payPalAPI.sendPayment(currency, amount);
    }
}

public class PaymentDemo {
    public static void main(String[] args) {
        // Stripe integration
        PaymentProcessor stripeProcessor = new StripeAdapter(new StripeAPI());
        stripeProcessor.pay(49.99);

        // PayPal integration
        PaymentProcessor payPalProcessor = new PayPalAdapter(new PayPalAPI());
        payPalProcessor.pay(75.50);
    }
}
