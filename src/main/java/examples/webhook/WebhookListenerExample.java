package examples.webhook;

import com.thompson.paystack.models.response.TransactionData;
import com.thompson.paystack.models.webhook.WebhookPayload;
import com.thompson.paystack.webhook.WebhookDispatcher;
import com.thompson.paystack.webhook.WebhookListener;
import examples.PaystackEnvKeyLoader;

/**
 * Using WebhookListener Interface
 */
class WebhookListenerExample {

    public static void main(String[] args) {
        String secretKey = PaystackEnvKeyLoader.getPaystackSecretKey();

        // Create dispatcher
        WebhookDispatcher dispatcher = new WebhookDispatcher(secretKey);

        // Register custom listener
        dispatcher.addListener(new MyWebhookListener());

        // In your webhook endpoint, just call:
        // dispatcher.dispatch(payload, signature);
    }

    static class MyWebhookListener implements WebhookListener {

        @Override
        public void onChargeSuccess(TransactionData transaction) {
            System.out.println("💰 Payment received!");
            System.out.println("Amount: ₦" + (transaction.getAmount() / 100.0));
            System.out.println("Reference: " + transaction.getReference());

            // Process order
            processOrder(transaction);
        }

        @Override
        public void onChargeFailed(TransactionData transaction) {
            System.out.println("❌ Payment failed!");
            System.out.println("Reference: " + transaction.getReference());
            System.out.println("Reason: " + transaction.getMessage());

            // Send failure notification
            notifyCustomer(transaction);
        }

        @Override
        public void onTransferSuccess(WebhookPayload payload) {
            System.out.println("💸 Transfer sent successfully!");
            // Handle transfer success
        }

        @Override
        public void onDisputeCreate(WebhookPayload payload) {
            System.out.println("⚠️ Customer dispute created!");
            // Handle dispute - gather evidence
        }

        private void processOrder(TransactionData transaction) {
            // Your order processing logic
        }

        private void notifyCustomer(TransactionData transaction) {
            // Send email/SMS notification
        }
    }
}
