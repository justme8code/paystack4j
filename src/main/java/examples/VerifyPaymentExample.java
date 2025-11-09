package examples;

import com.thompson.paystack.client.PaystackClient;
import com.thompson.paystack.models.response.PaystackResponse;
import com.thompson.paystack.models.response.TransactionData;
import com.thompson.paystack.enums.TransactionStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

class VerifyPaymentExample {

    private static final Logger logger = LoggerFactory.getLogger(VerifyPaymentExample.class);


    public static void main(String[] args) {

        PaystackClient client = new PaystackClient(PaystackEnvKeyLoader.getPaystackSecretKey());

        // Replace with the reference from your payment initialization
        // It's in the console output or you can see it on the payment page
        String reference = "TEST_1234567890"; // CHANGE THIS!

        logger.info("Verifying payment with reference: {}", reference);
        logger.info("=====================================\n");

        try {
            PaystackResponse<TransactionData> response =
                    client.transactions().verify(reference);


            if (response.isStatus()) {
                TransactionData transaction = response.getData();

                // Display transaction details
                logger.info("📋 Transaction Details:");
                logger.info("Status: {}", transaction.getStatus());
                logger.info("Reference: {}" , transaction.getReference());
                logger.info("Amount: ₦ {}" , (transaction.getAmount() / 100.0));
                logger.info("Currency: {}" , transaction.getCurrency());
                logger.info("Channel: {}" , transaction.getChannel());
                logger.info("Customer Email: {}" , transaction.getCustomer().getEmail());
                logger.info("Paid At: {}" , transaction.getPaidAt());


                // Check payment status
                if (transaction.getStatusEnum() == TransactionStatus.SUCCESS) {
                    logger.info("✅ PAYMENT SUCCESSFUL!");
                    logger.info("✅ You can now deliver the product/service");

                    // This is where you would:
                    // 1. Update your database (mark order as paid)
                    // 2. Send confirmation email
                    // 3. Deliver the product/service
                    // 4. Show success page to user

                } else if (transaction.getStatusEnum() == TransactionStatus.FAILED) {
                    logger.info("❌ PAYMENT FAILED!");
                    logger.info("Reason: {}" , transaction.getMessage());

                } else if (transaction.getStatusEnum() == TransactionStatus.ABANDONED) {
                    logger.info("⚠️ PAYMENT ABANDONED");
                    logger.info("Customer didn't complete the payment");

                } else {
                    logger.info("⏳ PAYMENT PENDING");
                }

                // Card details (if available)
                if (transaction.getAuthorization() != null) {
                    logger.info("\n💳 Card Details:");
                    logger.info("Card Type: {}", transaction.getAuthorization().getCardType());
                    logger.info("Last 4 Digits: {}", transaction.getAuthorization().getLast4());
                    logger.info("Bank: {}", transaction.getAuthorization().getBank());
                }

            } else {
                logger.info("❌ Verification failed: {}" , response.getMessage());
            }

        } catch (Exception e) {
            logger.warn("❌ Error verifying payment: {}", e.getMessage());
            logger.warn("Stack Trace: {}", Arrays.stream(e.getStackTrace()).toList());
        }
    }
}