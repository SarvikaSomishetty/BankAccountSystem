# Bank Account System

A simple Core Java console application that simulates basic banking operations using OOP, collections, and custom exception handling.

## Features
- Open savings and current accounts
- Deposit, withdraw, and transfer money
- Track transaction history per account
- Apply interest to active accounts
- Close accounts and list all accounts

## Project Structure
- `src/main/java/com/bank/model` - domain models (`Account`, account types, `Transaction`)
- `src/main/java/com/bank/service` - business logic (`BankService`)
- `src/main/java/com/bank/repository` - in-memory data storage
- `src/main/java/com/bank/ui` - console menu and user interaction
- `src/main/java/com/bank/App.java` - application entry point

## Requirements
- Java 17+ (or Java 11+ with compatible setup)

## Run Locally (Windows PowerShell)
```powershell
$files = Get-ChildItem -Path "src/main/java" -Recurse -Filter *.java | ForEach-Object { $_.FullName }
javac -encoding UTF-8 -d "bin" $files
java -cp "bin" com.bank.App
```

## Notes
- Data is stored in memory only (no database), so all data resets when the app stops.
