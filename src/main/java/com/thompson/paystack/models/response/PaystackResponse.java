package com.thompson.paystack.models.response;

import lombok.Data;

/**
 * Generic wrapper for all Paystack API responses
 *
 * @param <T> Type of the data object
 */
@Data
public class PaystackResponse<T> {
    private boolean status;
    private String message;
    private T data;
}
