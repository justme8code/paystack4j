package com.thompson.paystack.models.request;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

/**
 * Request model for creating a subaccount
 */
@Data
public class SubaccountCreateRequest {
    @SerializedName("business_name")
    private String businessName;

    @SerializedName("settlement_bank")
    private String settlementBank;

    @SerializedName("account_number")
    private String accountNumber;

    @SerializedName("percentage_charge")
    private Double percentageCharge;

    private String description;

    @SerializedName("primary_contact_email")
    private String primaryContactEmail;

    @SerializedName("primary_contact_name")
    private String primaryContactName;

    @SerializedName("primary_contact_phone")
    private String primaryContactPhone;

    @SerializedName("active")
    private Boolean active;

    @SerializedName("is_verified")
    private Boolean verified;

    /**
     * Builder for SubaccountCreateRequest
     */
    public static class Builder {
        private final SubaccountCreateRequest request = new SubaccountCreateRequest();

        /**
         * Set business name (required)
         */
        public Builder businessName(String businessName) {
            request.businessName = businessName;
            return this;
        }

        /**
         * Set settlement bank code (required)
         * Use bank code like "058" for GTBank
         */
        public Builder settlementBank(String bankCode) {
            request.settlementBank = bankCode;
            return this;
        }

        /**
         * Set account number (required)
         */
        public Builder accountNumber(String accountNumber) {
            request.accountNumber = accountNumber;
            return this;
        }

        /**
         * Set percentage charge (default split percentage)
         * Note: This can be overridden per transaction using transaction_charge
         */
        public Builder percentageCharge(double percentage) {
            request.percentageCharge = percentage;
            return this;
        }

        /**
         * Set description
         */
        public Builder description(String description) {
            request.description = description;
            return this;
        }

        /**
         * Set primary contact email
         */
        public Builder primaryContactEmail(String email) {
            request.primaryContactEmail = email;
            return this;
        }

        /**
         * Set primary contact name
         */
        public Builder primaryContactName(String name) {
            request.primaryContactName = name;
            return this;
        }

        /**
         * Set primary contact phone
         */
        public Builder primaryContactPhone(String phone) {
            request.primaryContactPhone = phone;
            return this;
        }

        /**
         * Set active status
         */
        public Builder active(boolean isActive) {
            request.active = isActive;
            return this;
        }

        /**
         * Set isVerified
         */
        public Builder verified(boolean isVerified) {
            request.verified = isVerified;
            return this;
        }

        /**
         * Build the request
         */
        public SubaccountCreateRequest build() {
            if (request.businessName == null || request.businessName.trim().isEmpty()) {
                throw new IllegalArgumentException("Business name is required");
            }
            if (request.settlementBank == null || request.settlementBank.trim().isEmpty()) {
                throw new IllegalArgumentException("Settlement bank is required");
            }
            if (request.accountNumber == null || request.accountNumber.trim().isEmpty()) {
                throw new IllegalArgumentException("Account number is required");
            }
            if (request.percentageCharge != null && (request.percentageCharge < 0 || request.percentageCharge > 100)) {
                throw new IllegalArgumentException("Percentage charge must be between 0 and 100");
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
