package com.thompson.paystack.webhook;

import com.thompson.paystack.models.response.TransactionData;
import com.thompson.paystack.models.webhook.WebhookPayload;

/**
 * Listener interface for handling webhook events
 * Implement this interface to handle specific webhook events
 */
public interface WebhookListener {

    /**
     * Called when a charge succeeds (payment completed)
     *
     * @param transaction Transaction data from webhook
     */
    default void onChargeSuccess(TransactionData transaction) {
        // Override to handle
    }

    /**
     * Called when a charge fails (payment failed)
     *
     * @param transaction Transaction data from webhook
     */
    default void onChargeFailed(TransactionData transaction) {
        // Override to handle
    }

    /**
     * Called when a transfer succeeds
     *
     * @param payload Raw webhook payload
     */
    default void onTransferSuccess(WebhookPayload payload) {
        // Override to handle
    }

    /**
     * Called when a transfer fails
     *
     * @param payload Raw webhook payload
     */
    default void onTransferFailed(WebhookPayload payload) {
        // Override to handle
    }

    /**
     * Called when a subscription is created
     *
     * @param payload Raw webhook payload
     */
    default void onSubscriptionCreate(WebhookPayload payload) {
        // Override to handle
    }

    /**
     * Called when a subscription is disabled
     *
     * @param payload Raw webhook payload
     */
    default void onSubscriptionDisable(WebhookPayload payload) {
        // Override to handle
    }

    /**
     * Called when a dispute is created
     *
     * @param payload Raw webhook payload
     */
    default void onDisputeCreate(WebhookPayload payload) {
        // Override to handle
    }

    /**
     * Called when a dispute is resolved
     *
     * @param payload Raw webhook payload
     */
    default void onDisputeResolve(WebhookPayload payload) {
        // Override to handle
    }

    /**
     * Called for any webhook event not handled by specific methods
     *
     * @param payload Raw webhook payload
     */
    default void onOtherEvent(WebhookPayload payload) {
        // Override to handle
    }
}