package com.thompson.paystack.models.request;

import com.thompson.paystack.enums.Currency;
import com.thompson.paystack.utils.AmountUtils;
import lombok.Data;

import java.math.BigDecimal;

/**
 * Request model for initiating a transfer
 */
@Data
public class TransferInitRequest {
    private String source = "balance"; // Default source
    private String reason;
    private long amount;  // Amount in kobo
    private String recipient;
    private String currency;
    private String reference;

    public static class Builder {
        private final TransferInitRequest request = new TransferInitRequest();

        public Builder amount(BigDecimal amount) {
            request.amount = AmountUtils.toKobo(amount);
            return this;
        }

        public Builder amount(double amount) {
            request.amount = AmountUtils.toKobo(amount);
            return this;
        }

        public Builder amountInKobo(long kobo) {
            request.amount = kobo;
            return this;
        }

        public Builder recipient(String recipientCode) {
            request.recipient = recipientCode;
            return this;
        }

        public Builder reason(String reason) {
            request.reason = reason;
            return this;
        }

        public Builder currency(Currency currency) {
            request.currency = currency.getCode();
            return this;
        }

        public Builder currency(String currency) {
            request.currency = currency;
            return this;
        }

        public Builder reference(String reference) {
            request.reference = reference;
            return this;
        }

        public Builder source(String source) {
            request.source = source;
            return this;
        }

        public TransferInitRequest build() {
            if (request.amount <= 0) {
                throw new IllegalArgumentException("Amount must be greater than 0");
            }
            if (request.recipient == null || request.recipient.trim().isEmpty()) {
                throw new IllegalArgumentException("Recipient code is required");
            }
            if (request.reason == null || request.reason.trim().isEmpty()) {
                throw new IllegalArgumentException("Reason is required");
            }
            return request;
        }
    }

    public static Builder builder() {
        return new Builder();
    }
}
