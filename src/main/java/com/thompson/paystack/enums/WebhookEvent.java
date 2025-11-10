package com.thompson.paystack.enums;

/**
 * Webhook event types sent by Paystack
 */
public enum WebhookEvent {
    // Charge events
    CHARGE_SUCCESS("charge.success"),
    CHARGE_FAILED("charge.failed"),

    // Transfer events
    TRANSFER_SUCCESS("transfer.success"),
    TRANSFER_FAILED("transfer.failed"),
    TRANSFER_REVERSED("transfer.reversed"),

    // Subscription events
    SUBSCRIPTION_CREATE("subscription.create"),
    SUBSCRIPTION_DISABLE("subscription.disable"),
    SUBSCRIPTION_NOT_RENEW("subscription.not_renew"),

    // Invoice events
    INVOICE_CREATE("invoice.create"),
    INVOICE_UPDATE("invoice.update"),
    INVOICE_PAYMENT_FAILED("invoice.payment_failed"),

    // Customer identification events
    CUSTOMERIDENTIFICATION_SUCCESS("customeridentification.success"),
    CUSTOMERIDENTIFICATION_FAILED("customeridentification.failed"),

    // Dispute events
    DISPUTE_CREATE("dispute.create"),
    DISPUTE_REMIND("dispute.remind"),
    DISPUTE_RESOLVE("dispute.resolve"),

    // Refund events
    REFUND_FAILED("refund.failed"),
    REFUND_PENDING("refund.pending"),
    REFUND_PROCESSED("refund.processed"),
    REFUND_PROCESSING("refund.processing"),

    // Unknown event
    UNKNOWN("unknown");

    private final String value;

    WebhookEvent(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    /**
     * Parse event type from string
     *
     * @param value Event string from webhook
     * @return WebhookEvent enum value
     */
    public static WebhookEvent fromString(String value) {
        if (value == null) {
            return UNKNOWN;
        }

        for (WebhookEvent event : WebhookEvent.values()) {
            if (event.value.equalsIgnoreCase(value)) {
                return event;
            }
        }

        return UNKNOWN;
    }

    /**
     * Check if this is a charge event
     */
    public boolean isChargeEvent() {
        return this == CHARGE_SUCCESS || this == CHARGE_FAILED;
    }

    /**
     * Check if this is a transfer event
     */
    public boolean isTransferEvent() {
        return this == TRANSFER_SUCCESS ||
                this == TRANSFER_FAILED ||
                this == TRANSFER_REVERSED;
    }

    /**
     * Check if this is a subscription event
     */
    public boolean isSubscriptionEvent() {
        return this == SUBSCRIPTION_CREATE ||
                this == SUBSCRIPTION_DISABLE ||
                this == SUBSCRIPTION_NOT_RENEW;
    }

    /**
     * Check if this is a dispute event
     */
    public boolean isDisputeEvent() {
        return this == DISPUTE_CREATE ||
                this == DISPUTE_REMIND ||
                this == DISPUTE_RESOLVE;
    }

    @Override
    public String toString() {
        return value;
    }
}