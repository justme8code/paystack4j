package com.thompson.paystack.models.request;

import com.google.gson.annotations.SerializedName;
import com.thompson.paystack.enums.Bearer;
import com.thompson.paystack.enums.Currency;
import com.thompson.paystack.utils.AmountUtils;
import lombok.Data;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * Request model for initializing a transaction
 */
@Data
public class TransactionInitRequest {
    private String email;
    private long amount;  // Amount in kobo
    private String currency;
    private String reference;

    @SerializedName("callback_url")
    private String callbackUrl;

    private String subaccount;

    @SerializedName("transaction_charge")
    private Long transactionCharge;  // Platform fee in kobo

    private String bearer;
    private Map<String, Object> metadata;

    /**
     * Builder for TransactionInitRequest
     */
    public static class Builder {
        private final TransactionInitRequest request = new TransactionInitRequest();

        /**
         * Set customer email (required)
         */
        public Builder email(String email) {
            request.email = email;
            return this;
        }

        /**
         * Set amount in major currency units (e.g., 100.50 NGN)
         * Will be automatically converted to kobo
         */
        public Builder amount(BigDecimal amount) {
            request.amount = AmountUtils.toKobo(amount);
            return this;
        }

        /**
         * Set amount in major currency units (e.g., 100.50)
         * Will be automatically converted to kobo
         */
        public Builder amount(double amount) {
            request.amount = AmountUtils.toKobo(amount);
            return this;
        }

        /**
         * Set amount directly in kobo (use only if you know what you're doing)
         */
        public Builder amountInKobo(long kobo) {
            request.amount = kobo;
            return this;
        }

        /**
         * Set currency (default is NGN)
         */
        public Builder currency(Currency currency) {
            request.currency = currency.getCode();
            return this;
        }

        /**
         * Set currency by string code
         */
        public Builder currency(String currency) {
            request.currency = currency;
            return this;
        }

        /**
         * Set unique transaction reference
         */
        public Builder reference(String reference) {
            request.reference = reference;
            return this;
        }

        /**
         * Set callback URL where customer will be redirected after payment
         */
        public Builder callbackUrl(String callbackUrl) {
            request.callbackUrl = callbackUrl;
            return this;
        }

        /**
         * Set subaccount code for split payments
         */
        public Builder subaccount(String subaccountCode) {
            request.subaccount = subaccountCode;
            return this;
        }

        /**
         * Set platform fee in major currency units
         * This is the flat fee your platform will receive
         */
        public Builder transactionCharge(BigDecimal charge) {
            request.transactionCharge = AmountUtils.toKobo(charge);
            return this;
        }

        /**
         * Set platform fee in major currency units
         */
        public Builder transactionCharge(double charge) {
            request.transactionCharge = AmountUtils.toKobo(charge);
            return this;
        }

        /**
         * Set platform fee directly in kobo
         */
        public Builder transactionChargeInKobo(long kobo) {
            request.transactionCharge = kobo;
            return this;
        }

        /**
         * Set who bears the Paystack transaction charges
         */
        public Builder bearer(Bearer bearer) {
            request.bearer = bearer.getValue();
            return this;
        }

        /**
         * Add custom metadata
         */
        public Builder metadata(Map<String, Object> metadata) {
            request.metadata = metadata;
            return this;
        }

        /**
         * Add a single metadata field
         */
        public Builder addMetadata(String key, Object value) {
            if (request.metadata == null) {
                request.metadata = new HashMap<>();
            }
            request.metadata.put(key, value);
            return this;
        }

        /**
         * Build the request
         */
        public TransactionInitRequest build() {
            if (request.email == null || request.email.trim().isEmpty()) {
                throw new IllegalArgumentException("Email is required");
            }
            if (request.amount <= 0) {
                throw new IllegalArgumentException("Amount must be greater than 0");
            }
            return request;
        }
    }

    /**
     * Create a new builder
     */
    public static Builder builder() {
        return new Builder();
    }
}
