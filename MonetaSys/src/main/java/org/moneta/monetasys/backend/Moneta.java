package org.moneta.monetasys.backend;

import org.moneta.monetasys.backend.exceptions.AccountNotFoundException;
import org.moneta.monetasys.backend.exceptions.AuthenticationException;
import org.moneta.monetasys.backend.exceptions.DuplicateAccountException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

public class Moneta {

    private final Repository<Account> accountRepository;

    public Moneta() {
        this.accountRepository = new Repository<>();

        User admin = new User("admin", "admin123", "ADMIN");
        Account adminAccount = new Account(admin);
        accountRepository.add("admin", adminAccount);
    }

    public void createAccount(String name, String password) {
        String key = name.trim().toLowerCase();

        if (accountRepository.contains(key)) {
            throw new DuplicateAccountException("An account with this name already exists.");
        }

        User user = new User(name, password,"USER");
        Account account = new Account(user);
        accountRepository.add(key, account);
    }

    public Account login(String name, String password) {
        Account account = getAccount(name);

        if (!account.getUser().checkPassword(password)) {
            throw new AuthenticationException("Incorrect password.");
        }

        return account;
    }

    public Account getAccount(String name) {
        String key = name.trim().toLowerCase();

        if (!accountRepository.contains(key)) {
            throw new AccountNotFoundException("Account not found for user: " + name);
        }

        return accountRepository.findByKey(key);
    }

    public void removeAccount(String name) {
        String key = name.trim().toLowerCase();

        if (!accountRepository.contains(key)) {
            throw new AccountNotFoundException("Cannot remove account. User not found: " + name);
        }

        accountRepository.remove(key);
    }

    public Collection<Account> getAllAccounts() {
        return accountRepository.findAll();
    }

    public List<Account> getSortedAccounts() {
        List<Account> sortedAccounts = new ArrayList<>(accountRepository.findAll());
        sortedAccounts.sort(Comparator.comparing(account -> account.getUser().getName().toLowerCase()));
        return sortedAccounts;
    }

    public void clearAccounts() {
        accountRepository.clear();
    }

    public int getAccountCount() {
        return accountRepository.size();
    }
}