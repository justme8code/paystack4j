package examples.webhook;

import com.thompson.paystack.client.PaystackClient;
import com.thompson.paystack.enums.TransactionStatus;
import com.thompson.paystack.models.response.PaystackResponse;
import com.thompson.paystack.models.response.TransactionData;
import com.thompson.paystack.models.webhook.WebhookPayload;
import com.thompson.paystack.webhook.*;
import examples.PaystackEnvKeyLoader;

/**
 * Simple Webhook Verification (Spring Boot)
 */
public class SpringBootWebhookExample {

    // @RestController
    // @RequestMapping("/api/paystack")
    public class PaystackWebhookController {

        private final WebhookHandler webhookHandler;
        private final PaystackClient paystackClient;

        public PaystackWebhookController() {
            String secretKey = PaystackEnvKeyLoader.getPaystackSecretKey();
            this.webhookHandler = new WebhookHandler(secretKey);
            this.paystackClient = new PaystackClient(secretKey);
        }

        // @PostMapping("/webhook")
        public String handleWebhook(
                // @RequestBody String payload,
                // @RequestHeader("x-paystack-signature") String signature
                String payload, String signature) {

            // Step 1: Verify signature
            if (webhookHandler.verifySignature(payload, signature)) {
                System.err.println("Invalid webhook signature!");
                return "Invalid signature";
                // return ResponseEntity.status(401).body("Invalid signature");
            }

            // Step 2: Parse webhook
            WebhookPayload webhook = webhookHandler.parseWebhook(payload);

            // Step 3: Handle different events
            if (webhook.isChargeSuccess()) {
                TransactionData transaction = webhookHandler.parseAsTransaction(webhook);
                handleSuccessfulPayment(transaction);
            } else if (webhook.isChargeFailed()) {
                TransactionData transaction = webhookHandler.parseAsTransaction(webhook);
                handleFailedPayment(transaction);
            }

            // Step 4: Always return 200 OK
            return "Webhook received";
            // return ResponseEntity.ok("Webhook received");
        }

        private void handleSuccessfulPayment(TransactionData transaction) {
            String reference = transaction.getReference();

            // IMPORTANT: Always verify with Paystack API
            PaystackResponse<TransactionData> response =
                    paystackClient.transactions().verify(reference);

            TransactionData verified = response.getData();

            if (verified.getStatusEnum() == TransactionStatus.SUCCESS) {
                // Check if already processed (prevent double fulfillment)
                if (isAlreadyProcessed(reference)) {
                    System.out.println("Already processed: " + reference);
                    return;
                }

                // Process the order
                System.out.println("Processing payment: " + reference);
                System.out.println("Amount: ₦" + (transaction.getAmount() / 100.0));
                System.out.println("Customer: " + transaction.getCustomer().getEmail());

                // 1. Update order status in database
                // updateOrderStatus(reference, "paid");

                // 2. Send confirmation email
                // sendConfirmationEmail(transaction.getCustomer().getEmail());

                // 3. Deliver product/service
                // deliverProduct(reference);

                // 4. Mark as processed
                markAsProcessed(reference);

                System.out.println("✅ Payment processed successfully!");
            }
        }

        private void handleFailedPayment(TransactionData transaction) {
            System.out.println("Payment failed: " + transaction.getReference());
            System.out.println("Reason: " + transaction.getMessage());

            // 1. Update order status
            // updateOrderStatus(transaction.getReference(), "failed");

            // 2. Notify customer
            // sendFailureNotification(transaction.getCustomer().getEmail());
        }

        private boolean isAlreadyProcessed(String reference) {
            // Check your database
            // return orderRepository.isProcessed(reference);
            return false;
        }

        private void markAsProcessed(String reference) {
            // Mark in your database
            // orderRepository.markAsProcessed(reference);
        }
    }
}

// ============================================

// ============================================

// ============================================

// ============================================

