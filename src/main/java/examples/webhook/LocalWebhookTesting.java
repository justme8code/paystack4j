package examples.webhook;

import com.thompson.paystack.models.response.TransactionData;
import com.thompson.paystack.models.webhook.WebhookPayload;
import com.thompson.paystack.webhook.WebhookHandler;
import examples.PaystackEnvKeyLoader;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;

/**
 * Testing Webhook Locally
 */
public class LocalWebhookTesting {

    public static void main(String[] args) {
        // Use a test secret key for demonstration
        // In production, use: System.getenv("PAYSTACK_SECRET_KEY")
        String secretKey = PaystackEnvKeyLoader.getPaystackSecretKey(); // Test key for demonstration
        WebhookHandler handler = new WebhookHandler(secretKey);

        // Sample webhook payload from Paystack
        String samplePayload = "{"
                + "\"event\":\"charge.success\","
                + "\"data\":{"
                + "  \"reference\":\"TEST_123\","
                + "  \"amount\":500000,"
                + "  \"status\":\"success\","
                + "  \"customer\":{\"email\":\"jvmMonster1@gmail.com\"}"
                + "}"
                + "}";

        System.out.println("=== Testing Webhook Parsing ===\n");

        // Parse webhook (without signature for testing)
        WebhookPayload webhook = handler.parseWebhook(samplePayload);

        System.out.println("Event: " + webhook.getEvent());
        System.out.println("Is charge success? " + webhook.isChargeSuccess());

        if (webhook.isChargeSuccess()) {
            TransactionData tx = handler.parseAsTransaction(webhook);
            System.out.println("Reference: " + tx.getReference());
            System.out.println("Amount: ₦" + (tx.getAmount() / 100.0));
            System.out.println("Customer: " + tx.getCustomer().getEmail());
        }

        System.out.println("\n=== Testing Signature Verification ===\n");

        // Test signature verification
        try {
            String signature = computeSignature(samplePayload, secretKey);
            System.out.println("Computed signature: " + signature.substring(0, 20) + "...");

            // Verify it
            boolean isValid = handler.verifySignature(samplePayload, signature);
            System.out.println("Signature valid: " + isValid);

            if (isValid) {
                System.out.println("✅ Signature verification working correctly!");
            }

        } catch (Exception e) {
            System.err.println("Error testing signature: " + e.getMessage());
        }

        System.out.println("\n=== Testing Invalid Signature ===\n");

        // Test with invalid signature
        boolean shouldBeFalse = handler.verifySignature(samplePayload, "invalid_signature");
        System.out.println("Invalid signature rejected: " + !shouldBeFalse);

        if (!shouldBeFalse) {
            System.out.println("✅ Invalid signatures are properly rejected!");
        }
    }

    /**
     * Compute HMAC SHA512 signature (for testing purposes)
     */
    private static String computeSignature(String payload, String secretKey) throws Exception {
        Mac sha512Hmac = Mac.getInstance("HmacSHA512"); // Correct algorithm name
        SecretKeySpec keySpec = new SecretKeySpec(
                secretKey.getBytes(StandardCharsets.UTF_8),
                "HmacSHA512"
        );
        sha512Hmac.init(keySpec);

        byte[] hash = sha512Hmac.doFinal(payload.getBytes(StandardCharsets.UTF_8));

        // Convert to hex
        StringBuilder hexString = new StringBuilder();
        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }

        return hexString.toString();
    }
}