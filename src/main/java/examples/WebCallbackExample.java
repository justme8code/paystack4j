package examples;

import com.thompson.paystack.client.PaystackClient;
import com.thompson.paystack.enums.TransactionStatus;
import com.thompson.paystack.models.response.PaystackResponse;
import com.thompson.paystack.models.response.TransactionData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

/**
 * Example: Verifying payment in a web application callback
 */
class WebCallbackExample {
    private static final Logger logger = LoggerFactory.getLogger(WebCallbackExample.class);


    // This would be a Spring/Servlet controller method
    public void handlePaymentCallback(String reference) {
        PaystackClient client = new PaystackClient(PaystackEnvKeyLoader.getPaystackSecretKey());

        try {
            PaystackResponse<TransactionData> response = client.transactions().verify(reference);

            if (response.isStatus() && response.getData().getStatusEnum() == TransactionStatus.SUCCESS) {
                // Payment successful
                long amountPaid = response.getData().getAmount(); // Amount in kobo

                // Update your database
                // Send confirmation email
                // Deliver product/service

                logger.info("Payment verified successfully!");
            } else {
                // Payment failed or pending
                logger.info("Payment not successful");
            }

        } catch (Exception e) {
            logger.error("Error verifying payment: {}" , e.getMessage());
            logger.warn("Stack Trace: {}", Arrays.stream(e.getStackTrace()).toList());
        }
    }
}
