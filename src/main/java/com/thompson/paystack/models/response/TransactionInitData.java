package com.thompson.paystack.models.response;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

/**
 * Response data for transaction initialization
 */
@Data
public class TransactionInitData {
    @SerializedName("authorization_url")
    private String authorizationUrl;

    @SerializedName("access_code")
    private String accessCode;

    private String reference;
}
