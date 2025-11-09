package com.thompson.paystack.enums;

/**
 * Supported currencies on Paystack
 */
public enum Currency {
    NGN("NGN"),  // Nigerian Naira
    GHS("GHS"),  // Ghanaian Cedi
    ZAR("ZAR"),  // South African Rand
    USD("USD");  // US Dollar

    private final String code;

    Currency(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    @Override
    public String toString() {
        return code;
    }
}