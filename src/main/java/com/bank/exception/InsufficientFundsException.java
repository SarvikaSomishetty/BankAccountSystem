package com.bank.exception;

/**
 * Thrown when a withdrawal or transfer exceeds available balance (+ overdraft).
 */
public class InsufficientFundsException extends RuntimeException {
    private final double requested;
    private final double available;

    public InsufficientFundsException(double requested, double available) {
        super(String.format(
            "Insufficient funds. Requested: ₹%.2f, Available: ₹%.2f", requested, available));
        this.requested = requested;
        this.available = available;
    }

    public double getRequested() { return requested; }
    public double getAvailable() { return available; }
}