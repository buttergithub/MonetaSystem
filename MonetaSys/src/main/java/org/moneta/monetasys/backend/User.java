package org.moneta.monetasys.backend;

import org.moneta.monetasys.backend.exceptions.InvalidNameException;

public class User {
    private final String name;
    private final String password;
    private final String role;

    public User(String name, String password) {
        this(name, password, "USER");
    }

    public User(String name, String password, String role) {
        if (name == null || name.trim().isEmpty()) {
            throw new InvalidNameException("User name cannot be empty.");
        }

        if (name.trim().length() < 2) {
            throw new InvalidNameException("User name must have at least 2 characters.");
        }

        if (password == null || password.trim().length() < 4) {
            throw new InvalidNameException("Password must have at least 4 characters.");
        }

        this.name = name.trim();
        this.password = password.trim();
        this.role = role == null ? "USER" : role.trim().toUpperCase();
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public String getRole() {
        return role;
    }

    public boolean isAdmin() {
        return role.equals("ADMIN");
    }

    public boolean checkPassword(String enteredPassword) {
        return password.equals(enteredPassword);
    }
}