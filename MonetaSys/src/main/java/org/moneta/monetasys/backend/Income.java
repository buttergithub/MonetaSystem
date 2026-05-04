package org.moneta.monetasys.backend;

public class Income extends Transaction {

    public Income(double amount, String description) {
        super(amount, description);
    }

    @Override
    public String getTransactionType() {
        return "Income";
    }

    @Override
    public double calculateEffect(double currentBalance) {
        return currentBalance + getAmount();
    }

    @Override
    public void displayTransaction() {
        System.out.println("Transaction Type: " + getTransactionType());
        System.out.println("This transaction adds money to the account.");
        System.out.println(getTransactionDetails());
    }
}