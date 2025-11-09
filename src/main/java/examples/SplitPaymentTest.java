package examples;

import com.thompson.paystack.client.PaystackClient;
import com.thompson.paystack.enums.Bearer;
import com.thompson.paystack.enums.Currency;
import com.thompson.paystack.models.request.SubaccountCreateRequest;
import com.thompson.paystack.models.request.TransactionInitRequest;
import com.thompson.paystack.models.response.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;

class SplitPaymentTest {
    private static final Logger logger = LoggerFactory.getLogger(SplitPaymentTest.class);

    public static void main(String[] args) {
        PaystackClient client = new PaystackClient(PaystackEnvKeyLoader.getPaystackSecretKey());

        // STEP 1: Create a test subaccount for "Person B"
        logger.info("=== Creating Subaccount for Person B ===\n");

        SubaccountCreateRequest subRequest = SubaccountCreateRequest.builder()
                .businessName("BOLAJI ORETAN THOMPSON")
                .settlementBank("171")  // GTBank (you can use any valid bank code)
                .accountNumber("9158497042")  // This is a test account
                .percentageCharge(10.0)
                .build();

        PaystackResponse<SubaccountData> subResponse =
                client.subaccounts().create(subRequest);

        if (subResponse.isStatus()) {
            String subaccountCode = subResponse.getData().getSubaccountCode();
            logger.info("✅ Subaccount created: {}" , subaccountCode);
            // Create split payment
            logger.info("=== Creating Split Payment ===\n");
            logger.info("Total Amount: ₦10,000");
            logger.info("Your Platform Fee: ₦1,000 (10%)");
            logger.info("Seller Receives: ₦9,000");

            BigDecimal totalAmount = new BigDecimal("10000.00");
            BigDecimal platformFee = new BigDecimal("1000.00");

            TransactionInitRequest request = TransactionInitRequest.builder()
                    .email("thompsonbolaji21@gmail.com")
                    .amount(totalAmount)
                    .currency(Currency.NGN)
                    .subaccount(subaccountCode)
                    .transactionCharge(platformFee)
                    .bearer(Bearer.SUBACCOUNT)  // Seller pays Paystack fees
                    .reference("SPLIT_" + System.currentTimeMillis())
                    .build();

            PaystackResponse<TransactionInitData> response =
                    client.transactions().initialize(request);

            if (response.isStatus()) {
                logger.info("✅ Split payment initialized!");
                logger.info("Payment URL: {}" ,response.getData().getAuthorizationUrl());
                logger.info("Reference: {}" ,response.getData().getReference());
                logger.info("💡 After customer pays:");
                logger.info("   - Seller gets ₦9,000 in their account");
                logger.info("   - You get ₦1,000 in your main account");
            }
        }
    }
}