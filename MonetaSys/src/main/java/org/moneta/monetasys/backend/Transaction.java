package org.moneta.monetasys.backend;

public abstract class Transaction {
    private final double amount;
    private final String description;

    public Transaction(double amount, String description) {
        this.amount = amount;
        this.description = description;
    }

    public double getAmount() {
        return amount;
    }

    public String getDescription() {
        return description;
    }

    public String getTransactionDetails() {
        return "Description: " + description + ", Amount: " + amount;
    }

    public abstract String getTransactionType();

    public abstract double calculateEffect(double currentBalance);

    public abstract void displayTransaction();

    @Override
    public String toString() {
        return getTransactionType() + "  |  " + description + "  |  " + amount;
    }
}