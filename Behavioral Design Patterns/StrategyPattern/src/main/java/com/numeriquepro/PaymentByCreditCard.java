package com.numeriquepro;

public class PaymentByCreditCard implements PaymentStrategy {
    private CreditCard card;

    @Override
    public void collectPaymentDetails() {
        // Pop-up to collect card details...
        card = new CreditCard("cardNumber", "Mohamadou", "expiryDate", "cvv");
    }

    @Override
    public boolean validatePaymentDetails() {
        // Validate credit card...
        return false;
    }

    @Override
    public void pay(int amount) {
        System.out.println("Paying " + amount + " using Credit Card");
        card.setAmount(card.getAmount() - amount);
    }
}

