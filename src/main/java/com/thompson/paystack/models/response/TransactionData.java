package com.thompson.paystack.models.response;

import com.google.gson.annotations.SerializedName;
import com.thompson.paystack.enums.TransactionStatus;
import lombok.Data;

/**
 * Complete transaction data returned by verify endpoint
 */
@Data
public class TransactionData {
    private Long id;
    private String domain;
    private String status;
    private String reference;
    private Long amount;
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

    public TransactionStatus getStatusEnum() {
        return TransactionStatus.fromString(status);
    }
}
