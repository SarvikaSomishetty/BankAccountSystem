package com.bank.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Immutable record of a single bank transaction.
 */
public class Transaction {

    public enum Type { DEPOSIT, WITHDRAWAL, TRANSFER_IN, TRANSFER_OUT, INTEREST }

    private final String transactionId;
    private final String accountNumber;
    private final Type type;
    private final double amount;
    private final double balanceAfter;
    private final LocalDateTime timestamp;
    private final String note;

    public Transaction(String accountNumber, Type type, double amount,
                       double balanceAfter, String note) {
        this.transactionId = "TXN" + System.currentTimeMillis();
        this.accountNumber  = accountNumber;
        this.type           = type;
        this.amount         = amount;
        this.balanceAfter   = balanceAfter;
        this.timestamp      = LocalDateTime.now();
        this.note           = note;
    }

    public String getTransactionId()  { return transactionId; }
    public String getAccountNumber()  { return accountNumber; }
    public Type   getType()           { return type; }
    public double getAmount()         { return amount; }
    public double getBalanceAfter()   { return balanceAfter; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public String getNote()           { return note; }

    @Override
    public String toString() {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        return String.format("%-20s | %-14s | %-12s | Amount: ₹%10.2f | Balance: ₹%10.2f | %s",
                transactionId, type, timestamp.format(fmt), amount, balanceAfter, note);
    }
}