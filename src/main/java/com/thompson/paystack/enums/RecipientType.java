package com.thompson.paystack.enums;

/**
 * Transfer recipient types
 */
public enum RecipientType {
    NUBAN("nuban"),           // Nigerian bank account
    MOBILE_MONEY("mobile_money"), // Mobile money (Ghana, Kenya, etc.)
    BASA("basa"),             // South African bank account
    AUTHORIZATION("authorization"); // Card authorization

    private final String value;

    RecipientType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return value;
    }
}