package com.thompson.paystack.utils;

import java.math.BigDecimal;

/**
 * Utility class for handling currency amount conversions
 */
public class AmountUtils {

    /**
     * Convert amount from major currency unit (e.g., Naira) to subunit (e.g., Kobo)
     * Paystack requires amounts in the smallest currency unit (kobo for NGN)
     *
     * @param amount Amount in major units (e.g., 100.50 NGN)
     * @return Amount in subunits (e.g., 10050 kobo)
     */
    public static long toKobo(BigDecimal amount) {
        if (amount == null) {
            throw new IllegalArgumentException("Amount cannot be null");
        }
        if (amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Amount cannot be negative");
        }
        return amount.multiply(new BigDecimal("100")).longValue();
    }

    /**
     * Convert amount from major currency unit to subunit
     *
     * @param amount Amount in major units
     * @return Amount in subunits
     */
    public static long toKobo(double amount) {
        return toKobo(BigDecimal.valueOf(amount));
    }

    /**
     * Convert amount from subunit (kobo) to major unit (Naira)
     *
     * @param kobo Amount in kobo
     * @return Amount in major units
     */
    public static BigDecimal fromKobo(long kobo) {
        return new BigDecimal(kobo).divide(new BigDecimal("100"));
    }
}

