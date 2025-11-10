package com.thompson.paystack.webhook;

import com.thompson.paystack.enums.WebhookEvent;
import com.thompson.paystack.models.response.TransactionData;
import com.thompson.paystack.models.webhook.WebhookPayload;

import java.util.ArrayList;
import java.util.List;

/**
 * Dispatcher for routing webhook events to registered listeners
 */
public class WebhookDispatcher {
    private final WebhookHandler handler;
    private final List<WebhookListener> listeners;

    /**
     * Create a new webhook dispatcher
     *
     * @param secretKey Your Paystack secret key
     */
    public WebhookDispatcher(String secretKey) {
        this.handler = new WebhookHandler(secretKey);
        this.listeners = new ArrayList<WebhookListener>();
    }

    /**
     * Register a webhook listener
     *
     * @param listener Listener to register
     */
    public void addListener(WebhookListener listener) {
        if (listener != null) {
            listeners.add(listener);
        }
    }

    /**
     * Remove a webhook listener
     *
     * @param listener Listener to remove
     */
    public void removeListener(WebhookListener listener) {
        listeners.remove(listener);
    }

    /**
     * Process a webhook and notify all registered listeners
     *
     * @param payload Raw webhook payload
     * @param signature Value from x-paystack-signature header
     * @return true if webhook was valid and processed, false if invalid signature
     */
    public boolean dispatch(String payload, String signature) {
        // Verify signature
        WebhookPayload webhookPayload = handler.verifyAndParse(payload, signature);

        if (webhookPayload == null) {
            // Invalid signature
            return false;
        }

        // Dispatch to listeners
        WebhookEvent event = webhookPayload.getEventEnum();

        switch (event) {
            case CHARGE_SUCCESS:
                TransactionData successTx = handler.parseAsTransaction(webhookPayload);
                notifyChargeSuccess(successTx);
                break;

            case CHARGE_FAILED:
                TransactionData failedTx = handler.parseAsTransaction(webhookPayload);
                notifyChargeFailed(failedTx);
                break;

            case TRANSFER_SUCCESS:
                notifyTransferSuccess(webhookPayload);
                break;

            case TRANSFER_FAILED:
                notifyTransferFailed(webhookPayload);
                break;

            case SUBSCRIPTION_CREATE:
                notifySubscriptionCreate(webhookPayload);
                break;

            case SUBSCRIPTION_DISABLE:
                notifySubscriptionDisable(webhookPayload);
                break;

            case DISPUTE_CREATE:
                notifyDisputeCreate(webhookPayload);
                break;

            case DISPUTE_RESOLVE:
                notifyDisputeResolve(webhookPayload);
                break;

            default:
                notifyOtherEvent(webhookPayload);
                break;
        }

        return true;
    }

    private void notifyChargeSuccess(TransactionData transaction) {
        for (WebhookListener listener : listeners) {
            try {
                listener.onChargeSuccess(transaction);
            } catch (Exception e) {
                // Log error but continue notifying other listeners
                System.err.println("Error in webhook listener: " + e.getMessage());
            }
        }
    }

    private void notifyChargeFailed(TransactionData transaction) {
        for (WebhookListener listener : listeners) {
            try {
                listener.onChargeFailed(transaction);
            } catch (Exception e) {
                System.err.println("Error in webhook listener: " + e.getMessage());
            }
        }
    }

    private void notifyTransferSuccess(WebhookPayload payload) {
        for (WebhookListener listener : listeners) {
            try {
                listener.onTransferSuccess(payload);
            } catch (Exception e) {
                System.err.println("Error in webhook listener: " + e.getMessage());
            }
        }
    }

    private void notifyTransferFailed(WebhookPayload payload) {
        for (WebhookListener listener : listeners) {
            try {
                listener.onTransferFailed(payload);
            } catch (Exception e) {
                System.err.println("Error in webhook listener: " + e.getMessage());
            }
        }
    }

    private void notifySubscriptionCreate(WebhookPayload payload) {
        for (WebhookListener listener : listeners) {
            try {
                listener.onSubscriptionCreate(payload);
            } catch (Exception e) {
                System.err.println("Error in webhook listener: " + e.getMessage());
            }
        }
    }

    private void notifySubscriptionDisable(WebhookPayload payload) {
        for (WebhookListener listener : listeners) {
            try {
                listener.onSubscriptionDisable(payload);
            } catch (Exception e) {
                System.err.println("Error in webhook listener: " + e.getMessage());
            }
        }
    }

    private void notifyDisputeCreate(WebhookPayload payload) {
        for (WebhookListener listener : listeners) {
            try {
                listener.onDisputeCreate(payload);
            } catch (Exception e) {
                System.err.println("Error in webhook listener: " + e.getMessage());
            }
        }
    }

    private void notifyDisputeResolve(WebhookPayload payload) {
        for (WebhookListener listener : listeners) {
            try {
                listener.onDisputeResolve(payload);
            } catch (Exception e) {
                System.err.println("Error in webhook listener: " + e.getMessage());
            }
        }
    }

    private void notifyOtherEvent(WebhookPayload payload) {
        for (WebhookListener listener : listeners) {
            try {
                listener.onOtherEvent(payload);
            } catch (Exception e) {
                System.err.println("Error in webhook listener: " + e.getMessage());
            }
        }
    }
}