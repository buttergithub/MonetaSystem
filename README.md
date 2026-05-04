Moneta 

Overview

Moneta is a multi-user budget management system that enables users to track income, expenses, and account balances with data persistence across sessions. It integrates a JavaFX user interface with a structured backend built using OOP, collections, exception handling, and file I/O.

Key Features
Multi-user support with account-based access

Role-based control
Admin: view, search, and sort all accounts
Users: access only their own data

Financial tracking
Add income and expenses
Automatic balance calculation
Transaction history per account

JavaFX interface
Input fields, action buttons, and ListView displays
Scrollable layout
UI separated from business logic

Technical Implementation
OOP:
User, Account, Transaction (abstract), Income, Expense
→ Uses encapsulation, inheritance, and polymorphism
Collections:
Map<String, Account> → stores users
List<Transaction> → stores account transactions
Generics:
Repository<T> used for reusable and type-safe data handling
Exception Handling:
Custom exceptions enforce rules and prevent system crashes
File I/O:
Data stored in moneta_data.txt and reloaded at startup

Project Structure
org.moneta.monetasys
├── backend
├── ui
├── component
├── MainApp.java
└── resources (moneta-view.fxml)

Run the Application

Requirements

Java JDK 17+
Maven
JavaFX

Run

mvn javafx:run
