package com.thompson.paystack.services;

import com.google.gson.reflect.TypeToken;
import com.thompson.paystack.client.PaystackConfig;
import com.thompson.paystack.exceptions.PaystackApiException;
import com.thompson.paystack.exceptions.PaystackException;
import com.thompson.paystack.models.request.SubaccountCreateRequest;
import com.thompson.paystack.models.response.PaystackResponse;
import com.thompson.paystack.models.response.SubaccountData;
import com.thompson.paystack.utils.JsonUtils;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

/**
 * Service for handling Paystack subaccount operations
 */
public class SubaccountService {
    private static final Logger log = LoggerFactory.getLogger(SubaccountService.class);
    private static final MediaType JSON = MediaType.get("application/json; charset=utf-8");

    private final OkHttpClient httpClient;
    private final PaystackConfig config;

    public SubaccountService(OkHttpClient httpClient, PaystackConfig config) {
        this.httpClient = httpClient;
        this.config = config;
    }

    /**
     * Create a new subaccount
     *
     * @param request Subaccount creation request
     * @return Response containing subaccount details and code
     * @throws PaystackException if request fails
     */
    public PaystackResponse<SubaccountData> create(SubaccountCreateRequest request) {
        String url = config.getBaseUrl() + "/subaccount";
        String jsonBody = JsonUtils.toJson(request);
        log.debug("Create Subaccount request: {}", jsonBody);

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
                        "Failed to create subaccount: " + response.message(),
                        response.code(),
                        responseBody
                );
            }

            return getSubaccountDataPaystackResponse(response, responseBody);

        } catch (IOException e) {
            throw new PaystackException("Network error while creating subaccount", e);
        }
    }

    /**
     * Fetch details of a subaccount
     *
     * @param subaccountCode The subaccount code (e.g., ACCT_xxxxxxxxx)
     * @return Response containing subaccount details
     * @throws PaystackException if request fails
     */
    public PaystackResponse<SubaccountData> get(String subaccountCode) {
        if (subaccountCode == null || subaccountCode.trim().isEmpty()) {
            throw new IllegalArgumentException("Subaccount code cannot be null or empty");
        }

        String url = config.getBaseUrl() + "/subaccount/" + subaccountCode;

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
                        "Failed to fetch subaccount: " + response.message(),
                        response.code(),
                        responseBody
                );
            }

            return getSubaccountDataPaystackResponse(response, responseBody);

        } catch (IOException e) {
            throw new PaystackException("Network error while fetching subaccount", e);
        }
    }

    @NotNull
    private <T> PaystackResponse<T> getSubaccountDataPaystackResponse(Response response, String responseBody) {
        Type type = new TypeToken<PaystackResponse<T>>() {
        }.getType();
        PaystackResponse<T> paystackResponse = JsonUtils.getGson().fromJson(responseBody, type);

        if (!paystackResponse.isStatus()) {
            throw new PaystackApiException(
                    "API returned error: " + paystackResponse.getMessage(),
                    response.code(),
                    responseBody
            );
        }

        return paystackResponse;
    }

    /**
     * Fetch all subaccount
     *
     * @return Response containing lists of subaccounts
     * @throws PaystackException if request fails
     */
    public PaystackResponse<List<SubaccountData>> getAll() {
        String url = config.getBaseUrl() + "/subaccount";

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
                        "Failed to fetch subaccount: " + response.message(),
                        response.code(),
                        responseBody
                );
            }

            PaystackResponse<List<SubaccountData>> paystackResponse = getSubaccountDataPaystackResponse(response, responseBody);

            if (!paystackResponse.isStatus()) {
                throw new PaystackApiException(
                        "API returned error: " + paystackResponse.getMessage(),
                        response.code(),
                        responseBody
                );
            }

            return paystackResponse;

        } catch (IOException e) {
            throw new PaystackException("Network error while fetching subaccount", e);
        }
    }

    /**
     * Updates details of a subaccount
     *
     * @param subaccountCodeOrId The subaccount code or Id (e.g., ACCT_xxxxxxxxx)
     * @param request The subaccount updated pay load
     * @return Response containing subaccount details
     * @throws PaystackException if request fails
     */
    public PaystackResponse<SubaccountData> update(String subaccountCodeOrId, SubaccountCreateRequest request) {
        String url = config.getBaseUrl() + "/subaccount/" + subaccountCodeOrId;
        String jsonBody = JsonUtils.toJson(request);
        log.debug("Update Subaccount request: {}", jsonBody);

        Request httpRequest = new Request.Builder()
                .url(url)
                .addHeader("Authorization", config.getAuthorizationHeader())
                .addHeader("Content-Type", "application/json")
                .put(RequestBody.create(jsonBody, JSON))
                .build();

        try {
            Response response = httpClient.newCall(httpRequest).execute();
            String responseBody = response.body().string();

            if (!response.isSuccessful()) {
                throw new PaystackApiException(
                        "Failed to update subaccount: " + response.message(),
                        response.code(),
                        responseBody
                );
            }

            return getSubaccountDataPaystackResponse(response, responseBody);

        } catch (IOException e) {
            throw new PaystackException("Network error while updating subaccount", e);
        }
    }

}
