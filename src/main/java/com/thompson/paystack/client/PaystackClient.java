package com.thompson.paystack.client;

import com.thompson.paystack.services.SubaccountService;
import com.thompson.paystack.services.TransactionService;
import com.thompson.paystack.services.TransferService;
import com.thompson.paystack.webhook.WebhookHandler;
import lombok.Getter;
import okhttp3.OkHttpClient;

import java.util.concurrent.TimeUnit;

/**
 * Main Paystack API client
 * <p>
 * Usage:
 * <pre>
 * PaystackClient client = new PaystackClient("sk_test_xxxxx");
 *
 * // Initialize a transaction
 * TransactionInitRequest request = TransactionInitRequest.builder()
 *     .email("[email protected]")
 *     .amount(10000.00)
 *     .build();
 * PaystackResponse&lt;TransactionInitData&gt; response = client.transactions().initialize(request);
 *
 * // Verify a transaction
 * PaystackResponse&lt;TransactionData&gt; verification = client.transactions().verify("ref_123");
 *
 * // Handle webhooks
 * WebhookHandler webhookHandler = client.webhooks();
 * boolean isValid = webhookHandler.verifySignature(payload, signature);
 * </pre>
 */
public class PaystackClient {
    /**
     * -- GETTER --
     *  Get the configuration
     *
     */
    @Getter
    private final PaystackConfig config;
    private final OkHttpClient httpClient;
    private final TransactionService transactionService;
    private final SubaccountService subaccountService;
    private final TransferService transferService;
    private final WebhookHandler webhookHandler;

    /**
     * Create a new Paystack client with default settings
     *
     * @param secretKey Your Paystack secret key (starts with sk_test_ or sk_live_)
     */
    public PaystackClient(String secretKey) {
        this(new PaystackConfig(secretKey));
    }

    /**
     * Create a new Paystack client with custom configuration
     *
     * @param config Paystack configuration
     */
    public PaystackClient(PaystackConfig config) {
        this(config, createDefaultHttpClient());
    }

    /**
     * Create a new Paystack client with custom HTTP client
     *
     * @param config Paystack configuration
     * @param httpClient Custom OkHttpClient
     */
    public PaystackClient(PaystackConfig config, OkHttpClient httpClient) {
        this.config = config;
        this.httpClient = httpClient;
        this.transactionService = new TransactionService(httpClient, config);
        this.subaccountService = new SubaccountService(httpClient, config);
        this.transferService = new TransferService(httpClient, config);
        this.webhookHandler = new WebhookHandler(config.getSecretKey());
    }

    /**
     * Get the transaction service for payment operations
     *
     * @return TransactionService instance
     */
    public TransactionService transactions() {
        return transactionService;
    }

    /**
     * Get the subaccount service for managing subaccounts
     *
     * @return SubaccountService instance
     */
    public SubaccountService subaccounts() {
        return subaccountService;
    }

    /**
     * Get the transfer service for sending money
     *
     * @return TransferService instance
     */
    public TransferService transfers() {
        return transferService;
    }

    /**
     * Get the webhook handler for processing webhooks
     *
     * @return WebhookHandler instance
     */
    public WebhookHandler webhooks() {
        return webhookHandler;
    }

    /**
     * Create default HTTP client with reasonable timeouts
     */
    private static OkHttpClient createDefaultHttpClient() {
        return new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .build();
    }

}