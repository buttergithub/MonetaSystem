package org.moneta.monetasys.ui;

import org.moneta.monetasys.backend.*;
import org.moneta.monetasys.backend.exceptions.MonetaException;
import org.moneta.monetasys.component.MessageHelper;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

public class MonetaController {

    private final Moneta moneta = new Moneta();
    private Account currentAccount;

    @FXML private VBox authPane;
    @FXML private VBox dashboardPane;

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;

    @FXML private TextField amountField;
    @FXML private TextField descriptionField;
    @FXML private TextField searchField;

    @FXML private Label messageLabel;
    @FXML private Label summaryLabel;
    @FXML private Label welcomeLabel;

    @FXML private ListView<String> transactionListView;
    @FXML private ListView<String> accountListView;

    @FXML
    public void initialize() {
        dashboardPane.setVisible(false);
        dashboardPane.setManaged(false);

        try {
            DataStorage.loadData(moneta);
            refreshAccounts();
            MessageHelper.showSuccess(messageLabel, "Welcome. Please sign up or log in.");
        } catch (Exception e) {
            MessageHelper.showError(messageLabel, "No saved data loaded.");
        }
    }

    @FXML
    private void handleSignUp() {
        try {
            String username = usernameField.getText();
            String password = passwordField.getText();

            moneta.createAccount(username, password);
            DataStorage.saveData(moneta);

            refreshAccounts();
            clearAuthInputs();

            MessageHelper.showSuccess(messageLabel, "Account created successfully. You can now log in.");

        } catch (MonetaException e) {
            MessageHelper.showError(messageLabel, e.getMessage());
        }
    }

    @FXML
    private void handleLogin() {
        try {
            String username = usernameField.getText();
            String password = passwordField.getText();

            currentAccount = moneta.login(username, password);

            authPane.setVisible(false);
            authPane.setManaged(false);

            dashboardPane.setVisible(true);
            dashboardPane.setManaged(true);

            welcomeLabel.setText("Welcome, " + currentAccount.getUser().getName());

            if (currentAccount.getUser().isAdmin()) {
                accountListView.setVisible(true);
                accountListView.setManaged(true);
            } else {
                accountListView.setVisible(false);
                accountListView.setManaged(false);
            }


            refreshAccounts();
            refreshTransactions(currentAccount);
            showSummary(currentAccount);
            clearAuthInputs();

            MessageHelper.showSuccess(messageLabel, "Login successful.");

        } catch (MonetaException e) {
            MessageHelper.showError(messageLabel, e.getMessage());
        }
    }

    @FXML
    private void handleLogout() {
        currentAccount = null;

        dashboardPane.setVisible(false);
        dashboardPane.setManaged(false);

        authPane.setVisible(true);
        authPane.setManaged(true);

        transactionListView.getItems().clear();
        summaryLabel.setText("");
        welcomeLabel.setText("");

        MessageHelper.showSuccess(messageLabel, "Logged out successfully.");
    }
    @FXML
    private void requireAdmin() {
        requireLogin();

        if (!currentAccount.getUser().isAdmin()) {
            throw new MonetaException("Only admin can access all accounts.");
        }
    }

    @FXML
    private void handleAddIncome() {
        try {
            requireLogin();

            double amount = Double.parseDouble(amountField.getText());
            String description = descriptionField.getText();

            currentAccount.addIncome(new Income(amount, description));

            refreshTransactions(currentAccount);
            showSummary(currentAccount);
            clearTransactionInputs();

            MessageHelper.showSuccess(messageLabel, "Income added successfully.");

        } catch (NumberFormatException e) {
            MessageHelper.showError(messageLabel, "Please enter a valid amount.");
        } catch (MonetaException e) {
            MessageHelper.showError(messageLabel, e.getMessage());
        }
    }

    @FXML
    private void handleAddExpense() {
        try {
            requireLogin();

            double amount = Double.parseDouble(amountField.getText());
            String description = descriptionField.getText();

            currentAccount.addExpense(new Expense(amount, description));

            refreshTransactions(currentAccount);
            showSummary(currentAccount);
            clearTransactionInputs();

            MessageHelper.showSuccess(messageLabel, "Expense added successfully.");

        } catch (NumberFormatException e) {
            MessageHelper.showError(messageLabel, "Please enter a valid amount.");
        } catch (MonetaException e) {
            MessageHelper.showError(messageLabel, e.getMessage());
        }
    }

    @FXML
    private void handleSearchAccount() {
        try {
            requireLogin();

            Account account;

            if (currentAccount.getUser().isAdmin()) {
                account = moneta.getAccount(searchField.getText());
            } else {
                account = currentAccount;
            }

            showSummary(account);
            refreshTransactions(account);

            MessageHelper.showSuccess(messageLabel, "Account loaded: " + account.getUser().getName());

        } catch (MonetaException e) {
            MessageHelper.showError(messageLabel, e.getMessage());
        }
    }

    @FXML
    private void handleSortAccounts() {
        try {
            requireAdmin();

            accountListView.getItems().clear();

            for (Account account : moneta.getSortedAccounts()) {
                accountListView.getItems().add(account.getUser().getName());
            }

            MessageHelper.showSuccess(messageLabel, "Accounts sorted by name.");

        } catch (MonetaException e) {
            MessageHelper.showError(messageLabel, e.getMessage());
        }
    }

    @FXML
    private void handleRemoveAccount() {
        try {
            requireLogin();

            String username = currentAccount.getUser().getName();
            moneta.removeAccount(username);
            DataStorage.saveData(moneta);

            handleLogout();
            refreshAccounts();

            MessageHelper.showSuccess(messageLabel, "Your account was removed successfully.");

        } catch (MonetaException e) {
            MessageHelper.showError(messageLabel, e.getMessage());
        }
    }

    @FXML
    private void handleSaveData() {
        try {
            DataStorage.saveData(moneta);
            MessageHelper.showSuccess(messageLabel, "Data saved successfully.");
        } catch (Exception e) {
            MessageHelper.showError(messageLabel, "Could not save data.");
        }
    }

    @FXML
    private void handleLoadData() {
        try {
            DataStorage.loadData(moneta);
            refreshAccounts();

            if (currentAccount != null) {
                currentAccount = moneta.getAccount(currentAccount.getUser().getName());
                refreshTransactions(currentAccount);
                showSummary(currentAccount);
            }

            MessageHelper.showSuccess(messageLabel, "Data loaded successfully.");

        } catch (Exception e) {
            MessageHelper.showError(messageLabel, "Could not load data.");
        }
    }

    @FXML
    private void handleAccountSelection() {
        try {
            String selected = accountListView.getSelectionModel().getSelectedItem();

            if (selected != null) {
                Account account = moneta.getAccount(selected);
                showSummary(account);
                refreshTransactions(account);
            }

        } catch (MonetaException e) {
            MessageHelper.showError(messageLabel, e.getMessage());
        }
    }

    private void requireLogin() {
        if (currentAccount == null) {
            throw new MonetaException("Please log in first.");
        }
    }

    private void refreshAccounts() {
        accountListView.getItems().clear();

        for (Account account : moneta.getAllAccounts()) {
            accountListView.getItems().add(account.getUser().getName());
        }
    }

    private void refreshTransactions(Account account) {
        transactionListView.setItems(FXCollections.observableArrayList());

        for (Transaction transaction : account.getTransactions()) {
            transactionListView.getItems().add(transaction.toString());
        }
    }

    private void showSummary(Account account) {
        summaryLabel.setText(account.getSummaryText());
    }

    private void clearAuthInputs() {
        usernameField.clear();
        passwordField.clear();
    }

    private void clearTransactionInputs() {
        amountField.clear();
        descriptionField.clear();
    }
}