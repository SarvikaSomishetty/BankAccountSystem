package com.bank.ui;

import com.bank.exception.AccountNotFoundException;
import com.bank.exception.InactiveAccountException;
import com.bank.exception.InsufficientFundsException;
import com.bank.model.Account;
import com.bank.model.Transaction;
import com.bank.service.BankService;

import java.util.List;
import java.util.Scanner;

/**
 * Console-based UI for the Bank Account System.
 * All user interaction is handled here — service layer stays clean.
 */
public class BankCLI {

    private final BankService bankService;
    private final Scanner scanner;

    public BankCLI(BankService bankService) {
        this.bankService = bankService;
        this.scanner = new Scanner(System.in);
    }

    public void start() {
        printBanner();
        boolean running = true;
        while (running) {
            printMenu();
            String choice = scanner.nextLine().trim();
            System.out.println();
            switch (choice) {
                case "1" -> createSavingsAccount();
                case "2" -> createCurrentAccount();
                case "3" -> deposit();
                case "4" -> withdraw();
                case "5" -> transfer();
                case "6" -> checkBalance();
                case "7" -> viewTransactionHistory();
                case "8" -> listAllAccounts();
                case "9" -> applyInterest();
                case "10" -> closeAccount();
                case "0" -> {
                    running = false;
                    System.out.println("Thank you for using SimpleBank. Goodbye!");
                }
                default -> System.out.println("⚠ Invalid option. Please try again.");
            }
            System.out.println();
        }
        scanner.close();
    }

    private void createSavingsAccount() {
        System.out.println("=== Open Savings Account ===");
        String name = promptString("Account holder name: ");
        double deposit = promptDouble("Initial deposit (min ₹500): ");
        try {
            Account acc = bankService.createSavingsAccount(name, deposit);
            System.out.println("Account Number: " + acc.getAccountNumber());
        } catch (Exception e) {
            System.out.println("✘ Error: " + e.getMessage());
        }
    }

    private void createCurrentAccount() {
        System.out.println("=== Open Current Account ===");
        String name = promptString("Account holder name: ");
        double deposit = promptDouble("Initial deposit: ");
        double overdraft = promptDouble("Overdraft limit (press Enter for default ₹10,000): ");
        try {
            Account acc = bankService.createCurrentAccount(name, deposit, overdraft);
            System.out.println("Account Number: " + acc.getAccountNumber());
        } catch (Exception e) {
            System.out.println("✘ Error: " + e.getMessage());
        }
    }

    private void deposit() {
        System.out.println("=== Deposit ===");
        String accNo = promptString("Account number: ");
        double amount = promptDouble("Amount to deposit: ");
        try {
            bankService.deposit(accNo, amount);
        } catch (AccountNotFoundException | InactiveAccountException | IllegalArgumentException e) {
            System.out.println("✘ " + e.getMessage());
        }
    }

    private void withdraw() {
        System.out.println("=== Withdraw ===");
        String accNo = promptString("Account number: ");
        double amount = promptDouble("Amount to withdraw: ");
        try {
            bankService.withdraw(accNo, amount);
        } catch (AccountNotFoundException | InactiveAccountException
                | InsufficientFundsException | IllegalArgumentException | IllegalStateException e) {
            System.out.println("✘ " + e.getMessage());
        }
    }

    private void transfer() {
        System.out.println("=== Transfer ===");
        String from = promptString("From account number: ");
        String to = promptString("To account number: ");
        double amount = promptDouble("Amount to transfer: ");
        try {
            bankService.transfer(from, to, amount);
        } catch (AccountNotFoundException | InactiveAccountException
                | InsufficientFundsException | IllegalArgumentException | IllegalStateException e) {
            System.out.println("✘ " + e.getMessage());
        }
    }

    private void checkBalance() {
        System.out.println("=== Check Balance ===");
        String accNo = promptString("Account number: ");
        try {
            Account acc = bankService.getAccount(accNo);
            System.out.println(acc);
        } catch (AccountNotFoundException | InactiveAccountException e) {
            System.out.println("✘ " + e.getMessage());
        }
    }

    private void viewTransactionHistory() {
        System.out.println("=== Transaction History ===");
        String accNo = promptString("Account number: ");
        try {
            List<Transaction> txns = bankService.getTransactionHistory(accNo);
            if (txns.isEmpty()) {
                System.out.println("No transactions found.");
            } else {
                System.out.println("-".repeat(110));
                txns.forEach(System.out::println);
                System.out.println("-".repeat(110));
                System.out.println("Total transactions: " + txns.size());
            }
        } catch (AccountNotFoundException | InactiveAccountException e) {
            System.out.println("✘ " + e.getMessage());
        }
    }

    private void listAllAccounts() {
        System.out.println("=== All Accounts ===");
        List<Account> accounts = bankService.getAllAccounts();
        if (accounts.isEmpty()) {
            System.out.println("No accounts in the system.");
        } else {
            accounts.forEach(System.out::println);
            System.out.println("Total accounts: " + bankService.getTotalAccounts());
        }
    }

    private void applyInterest() {
        System.out.println("=== Apply Interest to All Accounts ===");
        bankService.applyInterestToAll();
    }

    private void closeAccount() {
        System.out.println("=== Close Account ===");
        String accNo = promptString("Account number to close: ");
        try {
            bankService.closeAccount(accNo);
        } catch (AccountNotFoundException e) {
            System.out.println("✘ " + e.getMessage());
        }
    }

    // ------------------------------------------------------------ HELPERS

    private String promptString(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    private double promptDouble(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return Double.parseDouble(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("⚠ Please enter a valid number.");
            }
        }
    }

    private void printMenu() {
        System.out.println("┌──────────────────────────────────────┐");
        System.out.println("│           SIMPLEBANK MENU            │");
        System.out.println("├──────────────────────────────────────┤");
        System.out.println("│  1. Open Savings Account             │");
        System.out.println("│  2. Open Current Account             │");
        System.out.println("│  3. Deposit                          │");
        System.out.println("│  4. Withdraw                         │");
        System.out.println("│  5. Transfer                         │");
        System.out.println("│  6. Check Balance                    │");
        System.out.println("│  7. Transaction History              │");
        System.out.println("│  8. List All Accounts                │");
        System.out.println("│  9. Apply Interest (All Accounts)    │");
        System.out.println("│ 10. Close Account                    │");
        System.out.println("│  0. Exit                             │");
        System.out.println("└──────────────────────────────────────┘");
        System.out.print("Choose an option: ");
    }

    private void printBanner() {
        System.out.println("╔══════════════════════════════════════╗");
        System.out.println("║     SIMPLEBANK — Core Java Demo      ║");
        System.out.println("║  OOP | Collections | Exceptions      ║");
        System.out.println("╚══════════════════════════════════════╝");
        System.out.println();
    }
}