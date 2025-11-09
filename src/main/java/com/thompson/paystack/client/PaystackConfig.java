package com.thompson.paystack.client;

/**
 * Configuration class for Paystack API client
 */
public class PaystackConfig {
    private static final String DEFAULT_BASE_URL = "https://api.paystack.co";

    private final String secretKey;
    private final String baseUrl;

    /**
     * Creates a new PaystackConfig with default base URL
     *
     * @param secretKey Your Paystack secret key (starts with sk_)
     */
    public PaystackConfig(String secretKey) {
        this(secretKey, DEFAULT_BASE_URL);
    }

    /**
     * Creates a new PaystackConfig with custom base URL (useful for testing)
     *
     * @param secretKey Your Paystack secret key
     * @param baseUrl Custom base URL
     */
    public PaystackConfig(String secretKey, String baseUrl) {
        if (secretKey == null || secretKey.trim().isEmpty()) {
            throw new IllegalArgumentException("Secret key cannot be null or empty");
        }
        this.secretKey = secretKey;
        this.baseUrl = baseUrl;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    /**
     * Get the Authorization header value
     *
     * @return Bearer token string
     */
    public String getAuthorizationHeader() {
        return "Bearer " + secretKey;
    }
}