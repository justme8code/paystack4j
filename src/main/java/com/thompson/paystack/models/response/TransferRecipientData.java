package com.thompson.paystack.models.response;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

/**
 * Transfer recipient data
 */
@Data
public class TransferRecipientData {
    private long id;
    private String name;
    private String type;
    private String currency;

    @SerializedName("recipient_code")
    private String recipientCode;

    @SerializedName("account_number")
    private String accountNumber;

    @SerializedName("bank_code")
    private String bankCode;

    @SerializedName("bank_name")
    private String bankName;

    private boolean active;

    @SerializedName("created_at")
    private String createdAt;
}
