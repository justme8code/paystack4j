package com.thompson.paystack.models.response;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

/**
 * Transaction authorization details
 */
@Data
public class TransactionAuthorization {
    @SerializedName("authorization_code")
    private String authorizationCode;

    private String bin;
    private String last4;

    @SerializedName("exp_month")
    private String expMonth;

    @SerializedName("exp_year")
    private String expYear;

    private String channel;

    @SerializedName("card_type")
    private String cardType;

    private String bank;

    @SerializedName("country_code")
    private String countryCode;

    private String brand;
    private boolean reusable;
}
