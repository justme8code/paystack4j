package com.thompson.paystack.exceptions;

/**
 * Exception thrown when API returns an error response
 */
public class PaystackApiException extends PaystackException {
    private final int statusCode;
    private final String responseBody;

    public PaystackApiException(String message, int statusCode, String responseBody) {
        super(message);
        this.statusCode = statusCode;
        this.responseBody = responseBody;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getResponseBody() {
        return responseBody;
    }

    @Override
    public String toString() {
        return "PaystackApiException{" +
                "message='" + getMessage() + '\'' +
                ", statusCode=" + statusCode +
                ", responseBody='" + responseBody + '\'' +
                '}';
    }
}
