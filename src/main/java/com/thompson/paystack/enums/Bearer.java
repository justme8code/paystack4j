package com.thompson.paystack.enums;

/**
 * Who bears the Paystack transaction charges
 */
public enum Bearer {
    ACCOUNT("account"),      // Your platform pays fees
    SUBACCOUNT("subaccount"); // Subaccount owner pays fees

    private final String value;

    Bearer(String value) {
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
