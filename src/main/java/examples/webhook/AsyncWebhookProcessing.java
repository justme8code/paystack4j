package examples.webhook;

import com.thompson.paystack.models.webhook.WebhookPayload;
import com.thompson.paystack.webhook.WebhookHandler;

/**
 * Advanced - Async Processing with Queue
 */
class AsyncWebhookProcessing {

    // @RestController
    public class WebhookController {

        private final WebhookHandler webhookHandler;
        // private final Queue<WebhookPayload> webhookQueue;

        public WebhookController() {
            String secretKey = System.getenv("PAYSTACK_SECRET_KEY");
            this.webhookHandler = new WebhookHandler(secretKey);
            // this.webhookQueue = new LinkedBlockingQueue<>();
        }

        // @PostMapping("/webhook")
        public String handleWebhook(String payload, String signature) {

            // Verify signature
            WebhookPayload webhook = webhookHandler.verifyAndParse(payload, signature);

            if (webhook == null) {
                return "Invalid signature";
            }

            // Add to queue for async processing
            // webhookQueue.add(webhook);

            // Return 200 immediately (Paystack won't retry)
            return "Accepted";
        }

        // Background worker processes queue
        // @Scheduled(fixedDelay = 1000)
        public void processWebhooks() {
            // WebhookPayload webhook = webhookQueue.poll();
            // if (webhook != null) {
            //     processWebhook(webhook);
            // }
        }

        private void processWebhook(WebhookPayload webhook) {
            // Heavy processing here without blocking webhook endpoint
        }
    }
}
