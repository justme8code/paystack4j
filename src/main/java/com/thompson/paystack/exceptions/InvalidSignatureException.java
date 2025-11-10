package com.thompson.paystack.exceptions;

public class InvalidSignatureException extends PaystackException{
    public InvalidSignatureException(String message) {
        super(message);
    }

    public InvalidSignatureException(String message, Throwable cause) {
        super(message, cause);
    }
}
