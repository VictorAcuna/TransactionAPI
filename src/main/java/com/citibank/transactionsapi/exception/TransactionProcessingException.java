package com.citibank.transactionsapi.exception;

public class TransactionProcessingException extends RuntimeException {
    public TransactionProcessingException(String message) {
        super(message);
    }

    public TransactionProcessingException(String message, Throwable cause) {
        super(message, cause);
    }
}