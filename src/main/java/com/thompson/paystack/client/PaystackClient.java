package com.thompson.paystack.client;

import com.thompson.paystack.services.SubaccountService;
import com.thompson.paystack.services.TransactionService;
import okhttp3.OkHttpClient;

import java.util.concurrent.TimeUnit;

/**
 * Main Paystack API client
 *
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
 * </pre>
 */
public class PaystackClient {
    private final PaystackConfig config;
    private final OkHttpClient httpClient;
    private final TransactionService transactionService;
    private final SubaccountService subaccountService;

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
     * Create default HTTP client with reasonable timeouts
     */
    private static OkHttpClient createDefaultHttpClient() {
        return new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .build();
    }

    /**
     * Get the configuration
     *
     * @return PaystackConfig instance
     */
    public PaystackConfig getConfig() {
        return config;
    }
}