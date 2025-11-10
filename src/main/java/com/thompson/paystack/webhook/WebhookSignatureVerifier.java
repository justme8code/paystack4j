package com.thompson.paystack.webhook;

import com.thompson.paystack.exceptions.InvalidSignatureException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Utility class for verifying Paystack webhook signatures
 */
public class WebhookSignatureVerifier {
    private static final String HMAC_SHA512 = "HmacSHA512";

    private final String secretKey;

    /**
     * Create a new webhook signature verifier
     *
     * @param secretKey Your Paystack secret key
     */
    public WebhookSignatureVerifier(String secretKey) {
        if (secretKey == null || secretKey.trim().isEmpty()) {
            throw new IllegalArgumentException("Secret key cannot be null or empty");
        }
        this.secretKey = secretKey;
    }

    /**
     * Verify that a webhook came from Paystack by checking the signature
     *
     * @param payload The raw webhook payload (request body as string)
     * @param signature The signature from the x-paystack-signature header
     * @return true if signature is valid, false otherwise
     */
    public boolean verifySignature(String payload, String signature) {
        if (payload == null || signature == null) {
            throw new InvalidSignatureException("Payload or signature cannot be null");
        }

        try {
            String computedSignature = computeSignature(payload);
            return secureCompare(computedSignature, signature);

        } catch (Exception e) {
            // If signature computation fails, reject the webhook
            throw new InvalidSignatureException(e.getMessage());
        }
    }

    /**
     * Compute the HMAC SHA512 signature of the payload
     *
     * @param payload The webhook payload
     * @return Hex-encoded signature
     */
    private String computeSignature(String payload)
            throws NoSuchAlgorithmException, InvalidKeyException {

        Mac sha512Hmac = Mac.getInstance(HMAC_SHA512);
        SecretKeySpec keySpec = new SecretKeySpec(
                secretKey.getBytes(StandardCharsets.UTF_8),
                HMAC_SHA512
        );
        sha512Hmac.init(keySpec);

        byte[] hash = sha512Hmac.doFinal(payload.getBytes(StandardCharsets.UTF_8));

        return bytesToHex(hash);
    }

    /**
     * Convert byte array to hex string
     *
     * @param bytes Byte array
     * @return Hex string
     */
    private String bytesToHex(byte[] bytes) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : bytes) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }

    /**
     * Constant-time string comparison to prevent timing attacks
     *
     * @param a First string
     * @param b Second string
     * @return true if strings are equal
    /**
     * Constant-time comparison using JDK built-in method.
     */
    private boolean secureCompare(String a, String b) {
        if (a == null || b == null) return false;
        return MessageDigest.isEqual(
                a.getBytes(StandardCharsets.US_ASCII),
                b.getBytes(StandardCharsets.US_ASCII)
        );
    }
}