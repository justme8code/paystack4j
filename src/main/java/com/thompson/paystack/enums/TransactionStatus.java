package com.thompson.paystack.enums;

/**
 * Transaction status values
 */
public enum TransactionStatus {
    SUCCESS("success"),
    FAILED("failed"),
    ABANDONED("abandoned"),
    PENDING("pending");

    private final String value;

    TransactionStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static TransactionStatus fromString(String value) {
        for (TransactionStatus status : TransactionStatus.values()) {
            if (status.value.equalsIgnoreCase(value)) {
                return status;
            }
        }
        return PENDING; // Default to pending if unknown
    }

    @Override
    public String toString() {
        return value;
    }

}
