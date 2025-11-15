package com.thompson.paystack.services;

import com.google.gson.reflect.TypeToken;
import com.thompson.paystack.client.PaystackConfig;
import com.thompson.paystack.exceptions.PaystackApiException;
import com.thompson.paystack.exceptions.PaystackException;
import com.thompson.paystack.models.request.TransferInitRequest;
import com.thompson.paystack.models.request.TransferRecipientRequest;
import com.thompson.paystack.models.response.*;
import com.thompson.paystack.utils.JsonUtils;
import okhttp3.*;

import java.io.IOException;
import java.lang.reflect.Type;

/**
 * Service for handling Paystack transfer operations
 */
public class TransferService {
    private static final MediaType JSON = MediaType.get("application/json; charset=utf-8");

    private final OkHttpClient httpClient;
    private final PaystackConfig config;

    public TransferService(OkHttpClient httpClient, PaystackConfig config) {
        this.httpClient = httpClient;
        this.config = config;
    }

    /**
     * Create a transfer recipient
     *
     * @param request Recipient creation request
     * @return Response containing recipient code
     */
    public PaystackResponse<TransferRecipientData> createRecipient(TransferRecipientRequest request) {
        String url = config.getBaseUrl() + "/transferrecipient";
        String jsonBody = JsonUtils.toJson(request);

        Request httpRequest = new Request.Builder()
                .url(url)
                .addHeader("Authorization", config.getAuthorizationHeader())
                .addHeader("Content-Type", "application/json")
                .post(RequestBody.create(jsonBody, JSON))
                .build();

        try {
            Response response = httpClient.newCall(httpRequest).execute();
            String responseBody = response.body().string();

            if (!response.isSuccessful()) {
                throw new PaystackApiException(
                        "Failed to create recipient: " + response.message(),
                        response.code(),
                        responseBody
                );
            }

            Type type = new TypeToken<PaystackResponse<TransferRecipientData>>(){}.getType();
            PaystackResponse<TransferRecipientData> paystackResponse =
                    JsonUtils.getGson().fromJson(responseBody, type);

            if (!paystackResponse.isStatus()) {
                throw new PaystackApiException(
                        "API returned error: " + paystackResponse.getMessage(),
                        response.code(),
                        responseBody
                );
            }

            return paystackResponse;

        } catch (IOException e) {
            throw new PaystackException("Network error while creating recipient", e);
        }
    }

    /**
     * Initiate a transfer
     *
     * @param request Transfer initialization request
     * @return Response containing transfer details
     */
    public PaystackResponse<TransferData> initiate(TransferInitRequest request) {
        String url = config.getBaseUrl() + "/transfer";
        String jsonBody = JsonUtils.toJson(request);

        Request httpRequest = new Request.Builder()
                .url(url)
                .addHeader("Authorization", config.getAuthorizationHeader())
                .addHeader("Content-Type", "application/json")
                .post(RequestBody.create(jsonBody, JSON))
                .build();

        try {
            Response response = httpClient.newCall(httpRequest).execute();
            String responseBody = response.body().string();

            if (!response.isSuccessful()) {
                throw new PaystackApiException(
                        "Failed to initiate transfer: " + response.message(),
                        response.code(),
                        responseBody
                );
            }

            Type type = new TypeToken<PaystackResponse<TransferData>>(){}.getType();
            PaystackResponse<TransferData> paystackResponse =
                    JsonUtils.getGson().fromJson(responseBody, type);

            if (!paystackResponse.isStatus()) {
                throw new PaystackApiException(
                        "API returned error: " + paystackResponse.getMessage(),
                        response.code(),
                        responseBody
                );
            }

            return paystackResponse;

        } catch (IOException e) {
            throw new PaystackException("Network error while initiating transfer", e);
        }
    }

    /**
     * Verify a transfer
     *
     * @param reference Transfer reference
     * @return Response containing transfer details
     */
    public PaystackResponse<TransferData> verify(String reference) {
        if (reference == null || reference.trim().isEmpty()) {
            throw new IllegalArgumentException("Reference cannot be null or empty");
        }

        String url = config.getBaseUrl() + "/transfer/verify/" + reference;

        Request httpRequest = new Request.Builder()
                .url(url)
                .addHeader("Authorization", config.getAuthorizationHeader())
                .get()
                .build();

        try {
            Response response = httpClient.newCall(httpRequest).execute();
            String responseBody = response.body().string();

            if (!response.isSuccessful()) {
                throw new PaystackApiException(
                        "Failed to verify transfer: " + response.message(),
                        response.code(),
                        responseBody
                );
            }

            Type type = new TypeToken<PaystackResponse<TransferData>>(){}.getType();
            PaystackResponse<TransferData> paystackResponse =
                    JsonUtils.getGson().fromJson(responseBody, type);

            if (!paystackResponse.isStatus()) {
                throw new PaystackApiException(
                        "API returned error: " + paystackResponse.getMessage(),
                        response.code(),
                        responseBody
                );
            }

            return paystackResponse;

        } catch (IOException e) {
            throw new PaystackException("Network error while verifying transfer", e);
        }
    }
}