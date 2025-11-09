package com.thompson.paystack.exceptions;

/**
 * Exception thrown when authentication fails
 */
public class PaystackAuthException extends PaystackException {

    public PaystackAuthException(String message) {
        super(message);
    }

    public PaystackAuthException(String message, Throwable cause) {
        super(message, cause);
    }
}
