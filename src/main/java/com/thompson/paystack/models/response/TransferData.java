package com.thompson.paystack.models.response;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

/**
 * Transfer response data
 */
@Data
public class TransferData {
    private long id;
    private String reference;
    private String source;
    private String reason;
    private long amount;
    private String currency;
    private String status;

    @SerializedName("transfer_code")
    private String transferCode;

    @SerializedName("recipient")
    private TransferRecipientData recipient;

    @SerializedName("created_at")
    private String createdAt;

    @SerializedName("updated_at")
    private String updatedAt;
}
