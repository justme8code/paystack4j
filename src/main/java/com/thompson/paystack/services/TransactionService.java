package com.thompson.paystack.services;

import com.google.gson.reflect.TypeToken;
import com.thompson.paystack.client.PaystackConfig;
import com.thompson.paystack.exceptions.PaystackApiException;
import com.thompson.paystack.exceptions.PaystackException;
import com.thompson.paystack.models.request.ChargeAuthorizationRequest;
import com.thompson.paystack.models.request.TransactionInitRequest;
import com.thompson.paystack.models.response.PaystackResponse;
import com.thompson.paystack.models.response.TransactionData;
import com.thompson.paystack.models.response.TransactionInitData;
import com.thompson.paystack.utils.JsonUtils;
import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.reflect.Type;

/**
 * Service for handling Paystack transaction operations
 */
public class TransactionService {
    private static final Logger LOGGER = LoggerFactory.getLogger(TransactionService.class);
    private static final MediaType JSON = MediaType.get("application/json; charset=utf-8");

    private final OkHttpClient httpClient;
    private final PaystackConfig config;

    public TransactionService(OkHttpClient httpClient, PaystackConfig config) {
        this.httpClient = httpClient;
        this.config = config;
    }

    /**
     * Initialize a transaction
     *
     * @param request Transaction initialization request
     * @return Response containing authorization URL and reference
     * @throws PaystackException if request fails
     */
    public PaystackResponse<TransactionInitData> initialize(TransactionInitRequest request) {
        String url = config.getBaseUrl() + "/transaction/initialize";
        String jsonBody = JsonUtils.toJson(request);

        Request httpRequest = new Request.Builder()
                .url(url)
                .addHeader("Authorization", config.getAuthorizationHeader())
                .addHeader("Content-Type", "application/json")
                .post(RequestBody.create(jsonBody, JSON))
                .build();

        try (Response response = httpClient.newCall(httpRequest).execute()) {
            String responseBody = response.body().string();

            if (!response.isSuccessful()) {
                throw new PaystackApiException(
                        "Failed to initialize transaction: " + response.message(),
                        response.code(),
                        responseBody
                );
            }

            Type type = new TypeToken<PaystackResponse<TransactionInitData>>() {
            }.getType();
            PaystackResponse<TransactionInitData> paystackResponse = JsonUtils.getGson().fromJson(responseBody, type);

            if (!paystackResponse.isStatus()) {
                throw new PaystackApiException(
                        "API returned error: " + paystackResponse.getMessage(),
                        response.code(),
                        responseBody
                );
            }

            return paystackResponse;
        } catch (IOException e) {
            throw new PaystackException("Network error while initializing transaction", e);
        }
    }

    /**
     * Verify a transaction using its reference
     *
     * @param reference Transaction reference
     * @return Response containing complete transaction details
     * @throws PaystackException if request fails
     */
    public PaystackResponse<TransactionData> verify(String reference) {
        if (reference == null || reference.trim().isEmpty()) {
            throw new IllegalArgumentException("Reference cannot be null or empty");
        }

        String url = config.getBaseUrl() + "/transaction/verify/" + reference;

        Request httpRequest = new Request.Builder()
                .url(url)
                .addHeader("Authorization", config.getAuthorizationHeader())
                .get()
                .build();

        try (Response response = httpClient.newCall(httpRequest).execute()) {
            String responseBody = response.body().string();
            LOGGER.debug(responseBody);
            if (!response.isSuccessful()) {
                throw new PaystackApiException(
                        "Failed to verify transaction: " + response.message(),
                        response.code(),
                        responseBody
                );
            }

            Type type = new TypeToken<PaystackResponse<TransactionData>>() {}.getType();
            PaystackResponse<TransactionData> paystackResponse = JsonUtils.getGson().fromJson(responseBody, type);
            LOGGER.debug("Paystack response: {}", paystackResponse);
            if (!paystackResponse.isStatus()) {
                throw new PaystackApiException(
                        "API returned error: " + paystackResponse.getMessage(),
                        response.code(),
                        responseBody
                );
            }

            return paystackResponse;
        } catch (IOException e) {
            throw new PaystackException("Network error while verifying transaction", e);
        }

    }

    /**
     * Charge a saved authorization (repeat charge without customer input)
     *
     * @param request Charge authorization request
     * @return Response containing transaction data
     * @throws PaystackException if request fails
     */
    public PaystackResponse<TransactionData> chargeAuthorization(ChargeAuthorizationRequest request) {
        String url = config.getBaseUrl() + "/transaction/charge_authorization";
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
                        "Failed to charge authorization: " + response.message(),
                        response.code(),
                        responseBody
                );
            }

            Type type = new TypeToken<PaystackResponse<TransactionData>>(){}.getType();
            PaystackResponse<TransactionData> paystackResponse = JsonUtils.getGson().fromJson(responseBody, type);

            if (!paystackResponse.isStatus()) {
                throw new PaystackApiException(
                        "API returned error: " + paystackResponse.getMessage(),
                        response.code(),
                        responseBody
                );
            }

            return paystackResponse;

        } catch (IOException e) {
            throw new PaystackException("Network error while charging authorization", e);
        }
    }

}
