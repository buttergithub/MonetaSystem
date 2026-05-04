package org.moneta.monetasys.backend.exceptions;

public class AccountNotFoundException extends MonetaException {
    public AccountNotFoundException(String message) {
        super(message);
    }
}