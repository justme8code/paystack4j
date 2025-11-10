package examples.webhook;

import com.thompson.paystack.models.response.TransactionData;
import com.thompson.paystack.webhook.WebhookDispatcher;
import com.thompson.paystack.webhook.WebhookListener;

/**
 * Multiple Listeners for Different Purposes
 */
class MultipleListenersExample {

    public static void main(String[] args) {
        String secretKey = System.getenv("PAYSTACK_SECRET_KEY");
        WebhookDispatcher dispatcher = new WebhookDispatcher(secretKey);

        // Register multiple listeners
        dispatcher.addListener(new OrderProcessingListener());
        dispatcher.addListener(new EmailNotificationListener());
        dispatcher.addListener(new AnalyticsListener());

        // All listeners will be notified of events
    }

    static class OrderProcessingListener implements WebhookListener {
        @Override
        public void onChargeSuccess(TransactionData transaction) {
            // Update order status, deliver product
            System.out.println("Processing order...");
        }
    }

    static class EmailNotificationListener implements WebhookListener {
        @Override
        public void onChargeSuccess(TransactionData transaction) {
            // Send confirmation email
            System.out.println("Sending email to: " +
                    transaction.getCustomer().getEmail());
        }
    }

    static class AnalyticsListener implements WebhookListener {
        @Override
        public void onChargeSuccess(TransactionData transaction) {
            // Track analytics
            System.out.println("Recording analytics for: " +
                    transaction.getReference());
        }
    }
}
