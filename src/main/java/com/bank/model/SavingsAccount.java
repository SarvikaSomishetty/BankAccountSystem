package com.bank.model;

/**
 * Savings Account — earns interest, has minimum balance constraint.
 * Demonstrates: Inheritance, Method Overriding
 */
public class SavingsAccount extends Account {

    private static final double INTEREST_RATE = 0.04;   // 4% per annum
    private static final double MINIMUM_BALANCE = 500.0;

    public SavingsAccount(String accountHolder, double initialDeposit) {
        super(accountHolder, initialDeposit);
        if (initialDeposit < MINIMUM_BALANCE) {
            throw new IllegalArgumentException(
                "Savings account requires a minimum initial deposit of ₹" + MINIMUM_BALANCE);
        }
    }

    @Override
    public String getAccountType() { return "SAVINGS"; }

    @Override
    public double getInterestRate() { return INTEREST_RATE; }

    @Override
    public void withdraw(double amount) {
        if (getBalance() - amount < MINIMUM_BALANCE) {
            throw new IllegalStateException(
                "Withdrawal denied. Savings account must maintain a minimum balance of ₹" + MINIMUM_BALANCE);
        }
        super.withdraw(amount);
    }

    public double getMinimumBalance() { return MINIMUM_BALANCE; }
}