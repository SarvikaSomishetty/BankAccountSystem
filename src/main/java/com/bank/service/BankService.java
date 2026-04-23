package com.bank.service;

import com.bank.exception.AccountNotFoundException;
import com.bank.exception.InactiveAccountException;
import com.bank.exception.InsufficientFundsException;
import com.bank.model.*;
import com.bank.repository.AccountRepository;

import java.util.List;

/**
 * Core service layer — all business logic lives here.
 * Demonstrates: Service pattern, Exception handling, Collections
 */
public class BankService {

    private final AccountRepository repository;

    public BankService(AccountRepository repository) {
        this.repository = repository;
    }

    // ------------------------------------------------------------------ CREATE

    public Account createSavingsAccount(String holderName, double initialDeposit) {
        Account account = new SavingsAccount(holderName, initialDeposit);
        repository.save(account);
        repository.recordTransaction(new Transaction(
                account.getAccountNumber(),
                Transaction.Type.DEPOSIT,
                initialDeposit,
                account.getBalance(),
                "Account opened — initial deposit"));
        System.out.println("✔ Savings account created: " + account.getAccountNumber());
        return account;
    }

    public Account createCurrentAccount(String holderName, double initialDeposit, double overdraftLimit) {
        Account account = new CurrentAccount(holderName, initialDeposit, overdraftLimit);
        repository.save(account);
        repository.recordTransaction(new Transaction(
                account.getAccountNumber(),
                Transaction.Type.DEPOSIT,
                initialDeposit,
                account.getBalance(),
                "Account opened — initial deposit"));
        System.out.println("✔ Current account created: " + account.getAccountNumber());
        return account;
    }

    // ----------------------------------------------------------------- DEPOSIT

    public void deposit(String accountNumber, double amount) {
        Account account = getValidatedAccount(accountNumber);
        account.deposit(amount);
        repository.recordTransaction(new Transaction(
                accountNumber, Transaction.Type.DEPOSIT, amount, account.getBalance(), "Cash deposit"));
        System.out.printf("✔ Deposited ₹%.2f to %s. New balance: ₹%.2f%n",
                amount, accountNumber, account.getBalance());
    }

    // --------------------------------------------------------------- WITHDRAW

    public void withdraw(String accountNumber, double amount) {
        Account account = getValidatedAccount(accountNumber);
        if (account.getBalance() < amount) {
            throw new InsufficientFundsException(amount, account.getBalance());
        }
        account.withdraw(amount); // Let IllegalStateException propagate with proper message
        repository.recordTransaction(new Transaction(
                accountNumber, Transaction.Type.WITHDRAWAL, amount, account.getBalance(), "Cash withdrawal"));
        System.out.printf("✔ Withdrew ₹%.2f from %s. New balance: ₹%.2f%n",
                amount, accountNumber, account.getBalance());
    }

    // --------------------------------------------------------------- TRANSFER

    public void transfer(String fromAccountNumber, String toAccountNumber, double amount) {
        Account from = getValidatedAccount(fromAccountNumber);
        Account to = getValidatedAccount(toAccountNumber);

        if (from.getBalance() < amount) {
            throw new InsufficientFundsException(amount, from.getBalance());
        }

        from.withdraw(amount); // Let IllegalStateException propagate with proper message
        to.deposit(amount);

        String note = "Transfer to " + toAccountNumber;
        repository.recordTransaction(new Transaction(
                fromAccountNumber, Transaction.Type.TRANSFER_OUT, amount, from.getBalance(), note));
        repository.recordTransaction(new Transaction(
                toAccountNumber, Transaction.Type.TRANSFER_IN, amount, to.getBalance(),
                "Transfer from " + fromAccountNumber));

        System.out.printf("✔ Transferred ₹%.2f from %s to %s%n", amount, fromAccountNumber, toAccountNumber);
    }

    // ------------------------------------------------------------- INTEREST

    public void applyInterestToAll() {
        List<Account> all = repository.findAll();
        int count = 0;
        for (Account account : all) {
            if (account.isActive()) {
                double before = account.getBalance();
                account.applyInterest();
                double earned = account.getBalance() - before;
                repository.recordTransaction(new Transaction(
                        account.getAccountNumber(),
                        Transaction.Type.INTEREST,
                        earned,
                        account.getBalance(),
                        "Interest applied @ " + (account.getInterestRate() * 100) + "%"));
                count++;
            }
        }
        System.out.println("✔ Interest applied to " + count + " accounts.");
    }

    // -------------------------------------------------------------- CLOSE

    public void closeAccount(String accountNumber) {
        Account account = getValidatedAccount(accountNumber);
        account.setActive(false);
        System.out.println("✔ Account " + accountNumber + " has been closed.");
    }

    // ----------------------------------------------------------- QUERIES

    public Account getAccount(String accountNumber) {
        return repository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new AccountNotFoundException(accountNumber));
    }

    public List<Account> getAllAccounts() {
        return repository.findAll();
    }

    public List<Account> getAccountsByHolder(String holderName) {
        return repository.findByHolder(holderName);
    }

    public List<Transaction> getTransactionHistory(String accountNumber) {
        getValidatedAccount(accountNumber); // validate exists
        return repository.getTransactions(accountNumber);
    }

    public int getTotalAccounts() {
        return repository.totalAccounts();
    }

    // ---------------------------------------------------------- HELPERS

    private Account getValidatedAccount(String accountNumber) {
        Account account = repository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new AccountNotFoundException(accountNumber));
        if (!account.isActive()) {
            throw new InactiveAccountException(accountNumber);
        }
        return account;
    }
}