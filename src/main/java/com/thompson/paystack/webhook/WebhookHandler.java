package com.thompson.paystack.webhook;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.thompson.paystack.models.response.TransactionData;
import com.thompson.paystack.models.webhook.WebhookPayload;
import com.thompson.paystack.utils.JsonUtils;

/**
 * Main handler for processing Paystack webhooks
 */
public class WebhookHandler {
    private final WebhookSignatureVerifier signatureVerifier;
    private final Gson gson;

    /**
     * Create a new webhook handler
     *
     * @param secretKey Your Paystack secret key
     */
    public WebhookHandler(String secretKey) {
        this.signatureVerifier = new WebhookSignatureVerifier(secretKey);
        this.gson = JsonUtils.getGson();
    }

    /**
     * Verify that a webhook is authentic
     *
     * @param payload Raw webhook payload (request body)
     * @param signature Value from x-paystack-signature header
     * @return true if webhook is from Paystack, false otherwise
     */
    public boolean verifySignature(String payload, String signature) {
        return signatureVerifier.verifySignature(payload, signature);
    }

    /**
     * Parse a webhook payload into a WebhookPayload object
     *
     * @param payload Raw webhook JSON payload
     * @return Parsed WebhookPayload
     * @throws JsonSyntaxException if payload is invalid JSON
     */
    public WebhookPayload parseWebhook(String payload) {
        return gson.fromJson(payload, WebhookPayload.class);
    }

    /**
     * Verify AND parse a webhook in one call
     *
     * @param payload Raw webhook payload
     * @param signature Value from x-paystack-signature header
     * @return Parsed WebhookPayload if signature is valid, null otherwise
     */
    public WebhookPayload verifyAndParse(String payload, String signature) {
        if (verifySignature(payload, signature)) {
            return null;
        }

        return parseWebhook(payload);
    }

    /**
     * Parse webhook data as TransactionData
     * Useful for charge.success and charge.failed events
     *
     * @param webhookPayload The webhook payload
     * @return TransactionData object
     */
    public TransactionData parseAsTransaction(WebhookPayload webhookPayload) {
        if (webhookPayload == null || webhookPayload.getData() == null) {
            return null;
        }

        return gson.fromJson(webhookPayload.getData(), TransactionData.class);
    }

    /**
     * Complete helper method: verify, parse, and extract transaction data
     *
     * @param payload Raw webhook payload
     * @param signature Value from x-paystack-signature header
     * @return TransactionData if valid charge event, null otherwise
     */
    public TransactionData verifyAndParseTransaction(String payload, String signature) {
        WebhookPayload webhookPayload = verifyAndParse(payload, signature);

        if (webhookPayload == null || !webhookPayload.getEventEnum().isChargeEvent()) {
            return null;
        }

        return parseAsTransaction(webhookPayload);
    }
}