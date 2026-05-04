package org.moneta.monetasys.backend;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

public class DataStorage {
    private static final String FILE_NAME = "moneta_data.txt";

    public static void saveData(Moneta moneta) {
        try {
            PrintWriter writer = new PrintWriter(new FileWriter(FILE_NAME));

            for (Account account : moneta.getAllAccounts()) {
                writer.println(clean(account.toFileFormat()));

                for (String line : account.getTransactionsAsFileLines()) {
                    writer.println(clean(line));
                }
            }

            writer.close();

        } catch (IOException e) {
            throw new RuntimeException("Could not save data to file.");
        }
    }

    public static void loadData(Moneta moneta) {
        File file = new File(FILE_NAME);

        if (!file.exists()) {
            return;
        }

        try {
            moneta.clearAccounts();

            Scanner fileScanner = new Scanner(file);

            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine();
                String[] parts = line.split("\\|", 5);

                if (parts[0].equals("ACCOUNT")) {
                    String userName = parts[1];
                    String password = parts[2];

                    String role = parts.length >= 4 ? parts[3] : "USER";

                    if (role.equals("ADMIN")) {
                        continue;
                    }
                    moneta.createAccount(userName, password);

                } else if (parts[0].equals("TRANSACTION")) {
                    String userName = parts[1];
                    String type = parts[2];
                    double amount = Double.parseDouble(parts[3]);
                    String description = parts[4];

                    Account account = moneta.getAccount(userName);

                    if (type.equals("Income")) {
                        account.addIncome(new Income(amount, description));
                    } else if (type.equals("Expense")) {
                        account.addExpense(new Expense(amount, description));
                    }
                }
            }

            fileScanner.close();

        } catch (IOException e) {
            throw new RuntimeException("Could not read data from file.");
        } catch (Exception e) {
            throw new RuntimeException("Saved data could not be loaded correctly.");
        }
    }

    private static String clean(String value) {
        return value.replace("|", " ").trim();
    }
}