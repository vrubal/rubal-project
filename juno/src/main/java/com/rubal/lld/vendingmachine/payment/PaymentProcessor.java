package com.rubal.lld.vendingmachine.payment;

/**
 * @author vrubal
 * @created
 * @project rubal-project
 */
public interface PaymentProcessor {
    void processPayment(double amount);
}
