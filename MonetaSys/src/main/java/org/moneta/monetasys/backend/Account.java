package org.moneta.monetasys.backend;

import org.moneta.monetasys.backend.exceptions.InsufficientBalanceException;
import org.moneta.monetasys.backend.exceptions.InvalidAmountException;

import java.util.ArrayList;
import java.util.List;

public class Account {
    private final User user;
    private int transactionCount;
    private int incomeCount;
    private int expenseCount;

    private final List<Transaction> transactions;

    public Account(User user) {
        this.user = user;
        this.transactionCount = 0;
        this.incomeCount = 0;
        this.expenseCount = 0;
        this.transactions = new ArrayList<>();
    }

    public User getUser() {
        return user;
    }

    public int getTransactionCount() {
        return transactionCount;
    }

    public int getIncomeCount() {
        return incomeCount;
    }

    public int getExpenseCount() {
        return expenseCount;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void addIncome(Income income) {
        validateTransactionDescription(income.getDescription());

        if (income.getAmount() <= 0) {
            throw new InvalidAmountException("Income amount must be greater than 0.");
        }

        transactions.add(income);
        transactionCount++;
        incomeCount++;
    }

    public void addExpense(Expense expense) {
        validateTransactionDescription(expense.getDescription());

        if (expense.getAmount() <= 0) {
            throw new InvalidAmountException("Expense amount must be greater than 0.");
        }

        if (expense.getAmount() > getBalance()) {
            throw new InsufficientBalanceException(
                    "Expense exceeds available balance. Current balance is " + getBalance() + "."
            );
        }

        transactions.add(expense);
        transactionCount++;
        expenseCount++;
    }

    public double getTotalIncome() {
        double totalIncome = 0;

        for (Transaction transaction : transactions) {
            if (transaction.getTransactionType().equals("Income")) {
                totalIncome += transaction.getAmount();
            }
        }

        return totalIncome;
    }

    public double getTotalExpense() {
        double totalExpense = 0;

        for (Transaction transaction : transactions) {
            if (transaction.getTransactionType().equals("Expense")) {
                totalExpense += transaction.getAmount();
            }
        }

        return totalExpense;
    }

    public double getBalance() {
        double balance = 0;

        for (Transaction transaction : transactions) {
            balance = transaction.calculateEffect(balance);
        }

        return balance;
    }

    public String getSummaryText() {
        return "User: " + user.getName()
                + "\nTotal Income: " + getTotalIncome()
                + "\nTotal Expense: " + getTotalExpense()
                + "\nRemaining Balance: " + getBalance()
                + "\nTotal Transactions: " + transactionCount
                + "\nIncome Entries: " + incomeCount
                + "\nExpense Entries: " + expenseCount;
    }

    public String toFileFormat() {
        return "ACCOUNT|" + user.getName() + "|" + user.getPassword();
    }

    public List<String> getTransactionsAsFileLines() {
        List<String> lines = new ArrayList<>();

        for (Transaction transaction : transactions) {
            String line = "TRANSACTION|"
                    + user.getName() + "|"
                    + transaction.getTransactionType() + "|"
                    + transaction.getAmount() + "|"
                    + transaction.getDescription();

            lines.add(line);
        }

        return lines;
    }

    private void validateTransactionDescription(String description) {
        if (description == null || description.trim().isEmpty()) {
            throw new InvalidAmountException("Transaction description cannot be empty.");
        }
    }
}