package com.thompson.paystack.models.webhook;

import com.google.gson.JsonObject;
import com.thompson.paystack.enums.WebhookEvent;
import lombok.Data;

/**
 * Represents a webhook payload received from Paystack
 */
@Data
public class WebhookPayload {
    private String event;
    private JsonObject data;

    /**
     * Get the event as an enum
     *
     * @return WebhookEvent enum value
     */
    public WebhookEvent getEventEnum() {
        return WebhookEvent.fromString(event);
    }

    /**
     * Get the raw JSON string of the data
     *
     * @return JSON string
     */
    public String getDataAsString() {
        return data != null ? data.toString() : null;
    }

    /**
     * Check if this is a charge success event
     */
    public boolean isChargeSuccess() {
        return getEventEnum() == WebhookEvent.CHARGE_SUCCESS;
    }

    /**
     * Check if this is a charge failed event
     */
    public boolean isChargeFailed() {
        return getEventEnum() == WebhookEvent.CHARGE_FAILED;
    }

    /**
     * Check if this is a transfer success event
     */
    public boolean isTransferSuccess() {
        return getEventEnum() == WebhookEvent.TRANSFER_SUCCESS;
    }

    /**
     * Check if this is a subscription event
     */
    public boolean isSubscriptionEvent() {
        return getEventEnum().isSubscriptionEvent();
    }
}
