package com.bank;

import com.bank.repository.AccountRepository;
import com.bank.service.BankService;
import com.bank.ui.BankCLI;

public class App {
    public static void main(String[] args) {
        AccountRepository repository = new AccountRepository();
        BankService service = new BankService(repository);
        BankCLI cli = new BankCLI(service);
        cli.start();
    }
}