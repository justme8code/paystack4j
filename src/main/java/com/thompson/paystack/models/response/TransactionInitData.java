package com.thompson.paystack.models.response;

import com.google.gson.annotations.SerializedName;

/**
 * Response data for transaction initialization
 */
public class TransactionInitData {
    @SerializedName("authorization_url")
    private String authorizationUrl;

    @SerializedName("access_code")
    private String accessCode;

    private String reference;

    public String getAuthorizationUrl() {
        return authorizationUrl;
    }

    public void setAuthorizationUrl(String authorizationUrl) {
        this.authorizationUrl = authorizationUrl;
    }

    public String getAccessCode() {
        return accessCode;
    }

    public void setAccessCode(String accessCode) {
        this.accessCode = accessCode;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    @Override
    public String toString() {
        return "TransactionInitData{" +
                "authorizationUrl='" + authorizationUrl + '\'' +
                ", accessCode='" + accessCode + '\'' +
                ", reference='" + reference + '\'' +
                '}';
    }
}
