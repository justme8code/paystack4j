package com.thompson.paystack.models.request;

import com.google.gson.annotations.SerializedName;
import lombok.Builder;
import lombok.Data;

/**
 * Request model for creating a transfer recipient
 */
@Data
@Builder
public class TransferRecipientRequest {
    private String type;
    private String name;

    @SerializedName("account_number")
    private String accountNumber;

    @SerializedName("bank_code")
    private String bankCode;

    private String currency;
    private String description;

}
