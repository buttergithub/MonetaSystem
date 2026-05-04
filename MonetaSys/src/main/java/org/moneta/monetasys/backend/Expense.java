package org.moneta.monetasys.backend;

public class Expense extends Transaction {

    public Expense(double amount, String description) {
        super(amount, description);
    }

    @Override
    public String getTransactionType() {
        return "Expense";
    }

    @Override
    public double calculateEffect(double currentBalance) {
        return currentBalance - getAmount();
    }

    @Override
    public void displayTransaction() {
        System.out.println("Transaction Type: " + getTransactionType());
        System.out.println("This transaction removes money from the account.");
        System.out.println(getTransactionDetails());
    }
}