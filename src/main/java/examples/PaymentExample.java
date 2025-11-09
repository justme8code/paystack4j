package examples;

import com.thompson.paystack.client.PaystackClient;
import com.thompson.paystack.enums.Bearer;
import com.thompson.paystack.enums.Currency;
import com.thompson.paystack.enums.TransactionStatus;
import com.thompson.paystack.models.request.SubaccountCreateRequest;
import com.thompson.paystack.models.request.TransactionInitRequest;
import com.thompson.paystack.models.response.PaystackResponse;
import com.thompson.paystack.models.response.SubaccountData;
import com.thompson.paystack.models.response.TransactionData;
import com.thompson.paystack.models.response.TransactionInitData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;


/**
 * Complete example showing how to use the Paystack library
 * <p>
 * Scenario: Person A wants to pay Person B ₦10,000
 * Your platform takes 10% (₦1,000)
 * Person B receives ₦9,000
 */
 class PaymentExample {
    private static final Logger logger = LoggerFactory.getLogger(PaymentExample.class);

    public static void main(String[] args) {
        // Initialize the client with your secret key
        PaystackClient client = new PaystackClient("");

        // ===== STEP 1: Create Subaccount for Person B (Do this once per seller) =====
        logger.info("=== Creating Subaccount for Person B ===");
        SubaccountData personBSubaccount = createSubaccountForSeller(client);
        logger.info("Subaccount created: {}" , personBSubaccount.getSubaccountCode());

        // ===== STEP 2: Initialize Payment (When Person A wants to pay) =====
        logger.info("=== Initializing Payment ===");

        BigDecimal totalAmount = new BigDecimal("10000.00"); // ₦10,000
        BigDecimal platformFee = new BigDecimal("1000.00");  // Your 10% = ₦1,000
        // Person B will receive ₦9,000 automatically

        TransactionInitRequest request = TransactionInitRequest.builder()
                .email("[email protected]")  // Person A's email
                .amount(totalAmount)                     // Total amount
                .currency(Currency.NGN)
                .subaccount(personBSubaccount.getSubaccountCode()) // Person B's subaccount
                .transactionCharge(platformFee)          // Your platform gets this
                .bearer(Bearer.SUBACCOUNT)               // Person B pays Paystack fees
                .reference("TXN_" + System.currentTimeMillis()) // Unique reference
                .callbackUrl("https://yoursite.com/payment/callback")
                .addMetadata("seller_id", "person_b_123")
                .addMetadata("buyer_id", "person_a_456")
                .build();

        PaystackResponse<TransactionInitData> initResponse = client.transactions().initialize(request);

        if (initResponse.isStatus()) {
            TransactionInitData data = initResponse.getData();
            logger.info("Payment initialized successfully!");
            logger.info("Authorization URL: {}" , data.getAuthorizationUrl());
            logger.info("Reference: {}" , data.getReference());

            // Redirect Person A to this URL to complete payment
            String paymentUrl = data.getAuthorizationUrl();
            logger.info(">>> Redirect customer to: {}" , paymentUrl);


            // ===== STEP 3: Verify Payment (After customer returns from payment) =====
            logger.info("=== Verifying Payment ===");
            verifyPayment(client, data.getReference(), totalAmount);
        }
    }

    /**
     * Create a subaccount for a seller (Person B)
     * Do this once when a seller registers on your platform
     */
    private static SubaccountData createSubaccountForSeller(PaystackClient client) {
        SubaccountCreateRequest request = SubaccountCreateRequest.builder()
                .businessName("Person B Shop")
                .settlementBank("058")  // GTBank code (get bank codes from Paystack)
                .accountNumber("0123456789")
                .percentageCharge(10)  // Default 10% (can override per transaction)
                .description("Seller account for Person B")
                .primaryContactEmail("[email protected]")
                .primaryContactName("Person B")
                .primaryContactPhone("+2348012345678")
                .build();

        PaystackResponse<SubaccountData> response = client.subaccounts().create(request);

        if (!response.isStatus()) {
            throw new RuntimeException("Failed to create subaccount: " + response.getMessage());
        }

        return response.getData();
    }

    /**
     * Verify payment after customer returns from checkout
     * Call this from your callback URL endpoint
     */
    private static void verifyPayment(PaystackClient client, String reference, BigDecimal expectedAmount) {
        PaystackResponse<TransactionData> verifyResponse = client.transactions().verify(reference);

        if (verifyResponse.isStatus()) {
            TransactionData transaction = verifyResponse.getData();

            logger.info("Transaction Status: {}" , transaction.getStatus());
            logger.info("Amount Paid: ₦ {}" , (transaction.getAmount() / 100.0));
            logger.info("Reference: {}" , transaction.getReference());
            logger.info("Payment Channel: {}" , transaction.getChannel());

            // IMPORTANT: Always verify these conditions before delivering value
            TransactionStatus status = transaction.getStatusEnum();
            long expectedAmountInKobo = expectedAmount.multiply(new BigDecimal("100")).longValue();

            if (status == TransactionStatus.SUCCESS) {
                if (transaction.getAmount() == expectedAmountInKobo) {
                    logger.info("✓ Payment successful and amount matches!");
                    logger.info("✓ Person B will receive ₦9,000 in their account");
                    logger.info("✓ You (platform) received ₦1,000");

                    // TODO: Deliver value to customer
                    // TODO: Mark transaction as completed in your database
                    // TODO: Send confirmation emails

                } else {
                    logger.info("✗ Amount mismatch! Expected: ₦ {} ,  Got: ₦ {}" , expectedAmount ,
                            (transaction.getAmount() / 100.0));
                }
            } else if (status == TransactionStatus.FAILED) {
                logger.info("✗ Payment failed: {}", transaction.getMessage());
            } else if (status == TransactionStatus.ABANDONED) {
                logger.info("✗ Payment was abandoned by customer");
            }
        }
    }
}

