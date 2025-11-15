package com.thompson.paystack.models.request;

import com.google.gson.annotations.SerializedName;
import com.thompson.paystack.enums.Currency;
import com.thompson.paystack.utils.AmountUtils;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * Request model for charging a saved authorization
 */
public class ChargeAuthorizationRequest {
    @SerializedName("authorization_code")
    private String authorizationCode;

    private String email;
    private long amount;  // Amount in kobo
    private String currency;
    private String reference;
    private Map<String, Object> metadata;

    private ChargeAuthorizationRequest() {
    }

    public String getAuthorizationCode() {
        return authorizationCode;
    }

    public String getEmail() {
        return email;
    }

    public long getAmount() {
        return amount;
    }

    public String getCurrency() {
        return currency;
    }

    public String getReference() {
        return reference;
    }

    public Map<String, Object> getMetadata() {
        return metadata;
    }

    public static class Builder {
        private final ChargeAuthorizationRequest request = new ChargeAuthorizationRequest();

        public Builder authorizationCode(String authorizationCode) {
            request.authorizationCode = authorizationCode;
            return this;
        }

        public Builder email(String email) {
            request.email = email;
            return this;
        }

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

        public Builder metadata(Map<String, Object> metadata) {
            request.metadata = metadata;
            return this;
        }

        public Builder addMetadata(String key, Object value) {
            if (request.metadata == null) {
                request.metadata = new HashMap<String, Object>();
            }
            request.metadata.put(key, value);
            return this;
        }

        public ChargeAuthorizationRequest build() {
            if (request.authorizationCode == null || request.authorizationCode.trim().isEmpty()) {
                throw new IllegalArgumentException("Authorization code is required");
            }
            if (request.email == null || request.email.trim().isEmpty()) {
                throw new IllegalArgumentException("Email is required");
            }
            if (request.amount <= 0) {
                throw new IllegalArgumentException("Amount must be greater than 0");
            }
            return request;
        }
    }

    public static Builder builder() {
        return new Builder();
    }
}