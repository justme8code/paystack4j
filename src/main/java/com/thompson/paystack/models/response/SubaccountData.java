package com.thompson.paystack.models.response;

import com.google.gson.annotations.SerializedName;

/**
 * Subaccount data response
 */
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

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getSubaccountCode() {
        return subaccountCode;
    }

    public void setSubaccountCode(String subaccountCode) {
        this.subaccountCode = subaccountCode;
    }

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPrimaryContactName() {
        return primaryContactName;
    }

    public void setPrimaryContactName(String primaryContactName) {
        this.primaryContactName = primaryContactName;
    }

    public String getPrimaryContactEmail() {
        return primaryContactEmail;
    }

    public void setPrimaryContactEmail(String primaryContactEmail) {
        this.primaryContactEmail = primaryContactEmail;
    }

    public String getPrimaryContactPhone() {
        return primaryContactPhone;
    }

    public void setPrimaryContactPhone(String primaryContactPhone) {
        this.primaryContactPhone = primaryContactPhone;
    }

    public String getSettlementBank() {
        return settlementBank;
    }

    public void setSettlementBank(String settlementBank) {
        this.settlementBank = settlementBank;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public double getPercentageCharge() {
        return percentageCharge;
    }

    public void setPercentageCharge(double percentageCharge) {
        this.percentageCharge = percentageCharge;
    }

    public boolean isVerified() {
        return isVerified;
    }

    public void setVerified(boolean verified) {
        isVerified = verified;
    }

    public String getSettlementSchedule() {
        return settlementSchedule;
    }

    public void setSettlementSchedule(String settlementSchedule) {
        this.settlementSchedule = settlementSchedule;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public String toString() {
        return "SubaccountData{" +
                "subaccountCode='" + subaccountCode + '\'' +
                ", businessName='" + businessName + '\'' +
                ", accountNumber='" + accountNumber + '\'' +
                ", percentageCharge=" + percentageCharge +
                ", active=" + active +
                ", isVerified=" + isVerified +
                ", settlementSchedule='" + settlementSchedule + '\'' +
                '}';
    }
}
