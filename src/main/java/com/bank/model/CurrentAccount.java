package com.bank.model;

/**
 * Current Account — for businesses, supports overdraft up to a limit.
 * Demonstrates: Inheritance, Polymorphism
 */
public class CurrentAccount extends Account {

    private static final double INTEREST_RATE = 0.02;  // 2%
    private final double overdraftLimit;

    public CurrentAccount(String accountHolder, double initialDeposit, double overdraftLimit) {
        super(accountHolder, initialDeposit);
        if (overdraftLimit < 0) {
            throw new IllegalArgumentException("Overdraft limit cannot be negative.");
        }
        this.overdraftLimit = overdraftLimit;
    }

    public CurrentAccount(String accountHolder, double initialDeposit) {
        this(accountHolder, initialDeposit, 10000.0); // default overdraft: ₹10,000
    }

    @Override
    public String getAccountType() { return "CURRENT"; }

    @Override
    public double getInterestRate() { return INTEREST_RATE; }

    @Override
    public void withdraw(double amount) {
        if (getBalance() - amount < -overdraftLimit) {
            throw new IllegalStateException(
                String.format("Withdrawal exceeds overdraft limit of ₹%.2f.", overdraftLimit));
        }
        super.withdraw(amount);
    }

    public double getOverdraftLimit() { return overdraftLimit; }
}