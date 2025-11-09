package com.thompson.paystack.models.response;

import com.google.gson.annotations.SerializedName;
import com.thompson.paystack.enums.TransactionStatus;

/**
 * Complete transaction data returned by verify endpoint
 */
public class TransactionData {
    private long id;
    private String domain;
    private String status;
    private String reference;
    private long amount;
    private String message;

    @SerializedName("gateway_response")
    private String gatewayResponse;

    @SerializedName("paid_at")
    private String paidAt;

    @SerializedName("created_at")
    private String createdAt;

    private String channel;
    private String currency;

    @SerializedName("ip_address")
    private String ipAddress;

    private TransactionMetadata metadata;
    private TransactionAuthorization authorization;
    private TransactionCustomer customer;

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

    public String getStatus() {
        return status;
    }

    public TransactionStatus getStatusEnum() {
        return TransactionStatus.fromString(status);
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getGatewayResponse() {
        return gatewayResponse;
    }

    public void setGatewayResponse(String gatewayResponse) {
        this.gatewayResponse = gatewayResponse;
    }

    public String getPaidAt() {
        return paidAt;
    }

    public void setPaidAt(String paidAt) {
        this.paidAt = paidAt;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public TransactionMetadata getMetadata() {
        return metadata;
    }

    public void setMetadata(TransactionMetadata metadata) {
        this.metadata = metadata;
    }

    public TransactionAuthorization getAuthorization() {
        return authorization;
    }

    public void setAuthorization(TransactionAuthorization authorization) {
        this.authorization = authorization;
    }

    public TransactionCustomer getCustomer() {
        return customer;
    }

    public void setCustomer(TransactionCustomer customer) {
        this.customer = customer;
    }

    @Override
    public String toString() {
        return "TransactionData{" +
                "id=" + id +
                ", status='" + status + '\'' +
                ", reference='" + reference + '\'' +
                ", amount=" + amount +
                ", currency='" + currency + '\'' +
                '}';
    }
}
