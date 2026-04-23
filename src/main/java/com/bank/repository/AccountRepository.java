package com.bank.repository;

import com.bank.model.Account;
import com.bank.model.Transaction;

import java.util.*;

/**
 * In-memory repository for accounts and transactions.
 * Demonstrates: HashMap, ArrayList, Collections usage
 */
public class AccountRepository {

    // accountNumber -> Account
    private final Map<String, Account> accounts = new HashMap<>();

    // accountNumber -> list of transactions
    private final Map<String, List<Transaction>> transactionLedger = new HashMap<>();

    public void save(Account account) {
        accounts.put(account.getAccountNumber(), account);
        transactionLedger.putIfAbsent(account.getAccountNumber(), new ArrayList<>());
    }

    public Optional<Account> findByAccountNumber(String accountNumber) {
        return Optional.ofNullable(accounts.get(accountNumber));
    }

    public List<Account> findAll() {
        return Collections.unmodifiableList(new ArrayList<>(accounts.values()));
    }

    public List<Account> findByHolder(String holderName) {
        List<Account> result = new ArrayList<>();
        for (Account acc : accounts.values()) {
            if (acc.getAccountHolder().equalsIgnoreCase(holderName)) {
                result.add(acc);
            }
        }
        return result;
    }

    public void recordTransaction(Transaction txn) {
        transactionLedger
            .computeIfAbsent(txn.getAccountNumber(), k -> new ArrayList<>())
            .add(txn);
    }

    public List<Transaction> getTransactions(String accountNumber) {
        return Collections.unmodifiableList(
            transactionLedger.getOrDefault(accountNumber, Collections.emptyList())
        );
    }

    public boolean exists(String accountNumber) {
        return accounts.containsKey(accountNumber);
    }

    public int totalAccounts() {
        return accounts.size();
    }
}