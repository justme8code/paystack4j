package com.thompson.paystack.exceptions;

/**
 * Base exception for all Paystack-related errors
 */
public class PaystackException extends RuntimeException {

    public PaystackException(String message) {
        super(message);
    }

    public PaystackException(String message, Throwable cause) {
        super(message, cause);
    }
}