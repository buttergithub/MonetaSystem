package org.moneta.monetasys.backend.exceptions;

public class InsufficientBalanceException extends MonetaException {
    public InsufficientBalanceException(String message) {
        super(message);
    }
}