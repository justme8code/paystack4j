package com.thompson.paystack.models.response;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

/**
 * Subaccount data response
 */
@Data
public class SubaccountData {
    private long id;
    private String domain;

    @SerializedName("subaccount_code")
    private String subaccountCode;

    @SerializedName("business_name")
    private String businessName;

    private String description;

    @SerializedName("primary_contact_name")
    private String primaryContactName;

    @SerializedName("primary_contact_email")
    private String primaryContactEmail;

    @SerializedName("primary_contact_phone")
    private String primaryContactPhone;

    @SerializedName("settlement_bank")
    private String settlementBank;

    @SerializedName("account_number")
    private String accountNumber;

    @SerializedName("percentage_charge")
    private Double percentageCharge;

    @SerializedName("is_verified")
    private Boolean isVerified;

    @SerializedName("settlement_schedule")
    private String settlementSchedule;

    @SerializedName("active")
    private Boolean active;

    @SerializedName("created_at")
    private String createdAt;

    @SerializedName("updated_at")
    private String updatedAt;
}
