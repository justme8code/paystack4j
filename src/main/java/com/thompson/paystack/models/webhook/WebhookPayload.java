package com.thompson.paystack.models.webhook;

import com.google.gson.JsonObject;
import com.thompson.paystack.enums.WebhookEvent;

/**
 * Represents a webhook payload received from Paystack
 */
public class WebhookPayload {
    private String event;
    private JsonObject data;

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    /**
     * Get the event as an enum
     *
     * @return WebhookEvent enum value
     */
    public WebhookEvent getEventEnum() {
        return WebhookEvent.fromString(event);
    }

    public JsonObject getData() {
        return data;
    }

    public void setData(JsonObject data) {
        this.data = data;
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

    @Override
    public String toString() {
        return "WebhookPayload{" +
                "event='" + event + '\'' +
                ", data=" + data +
                '}';
    }
}