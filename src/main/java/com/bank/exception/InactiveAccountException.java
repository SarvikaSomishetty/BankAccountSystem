package com.bank.exception;

/**
 * Thrown when an operation is attempted on a closed/inactive account.
 */
public class InactiveAccountException extends RuntimeException {
    public InactiveAccountException(String accountNumber) {
        super("Account " + accountNumber + " is inactive. Please contact support.");
    }
}