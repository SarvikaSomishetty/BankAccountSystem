package com.bank.exception;

/**
 * Thrown when an operation is attempted on an account that does not exist.
 */
public class AccountNotFoundException extends RuntimeException {
    private final String accountNumber;

    public AccountNotFoundException(String accountNumber) {
        super("No account found with number: " + accountNumber);
        this.accountNumber = accountNumber;
    }

    public String getAccountNumber() { return accountNumber; }
}