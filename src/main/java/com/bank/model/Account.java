package com.bank.model;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Abstract base class for all bank account types.
 * Demonstrates: Abstraction, Encapsulation
 */
public abstract class Account {

    private final String accountId;
    private final String accountHolder;
    private final String accountNumber;
    private double balance;
    private final LocalDateTime createdAt;
    private boolean active;

    public Account(String accountHolder, double initialDeposit) {
        if (accountHolder == null || accountHolder.trim().isEmpty()) {
            throw new IllegalArgumentException("Account holder name cannot be empty.");
        }
        if (initialDeposit < 0) {
            throw new IllegalArgumentException("Initial deposit cannot be negative.");
        }
        this.accountId = UUID.randomUUID().toString();
        this.accountHolder = accountHolder.trim();
        this.accountNumber = generateAccountNumber();
        this.balance = initialDeposit;
        this.createdAt = LocalDateTime.now();
        this.active = true;
    }

    private String generateAccountNumber() {
        return "ACC" + System.currentTimeMillis();
    }

    // Abstract methods — subclasses define their own behavior
    public abstract String getAccountType();
    public abstract double getInterestRate();

    public void deposit(double amount) {
        if (amount <= 0) throw new IllegalArgumentException("Deposit amount must be positive.");
        this.balance += amount;
    }

    public void withdraw(double amount) {
        if (amount <= 0) throw new IllegalArgumentException("Withdrawal amount must be positive.");
        this.balance -= amount;
    }

    public void applyInterest() {
        double interest = balance * getInterestRate();
        this.balance += interest;
    }

    // Getters
    public String getAccountId()     { return accountId; }
    public String getAccountHolder() { return accountHolder; }
    public String getAccountNumber() { return accountNumber; }
    public double getBalance()       { return balance; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public boolean isActive()        { return active; }
    public void setActive(boolean active) { this.active = active; }

    @Override
    public String toString() {
        return String.format("[%s] %s | Holder: %s | Balance: ₹%.2f | Active: %s",
                getAccountType(), accountNumber, accountHolder, balance, active);
    }
}