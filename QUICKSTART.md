# paystack4j - Quick Start Guide

Get up and running with paystack4j in 5 minutes!

## Prerequisites

- Java 21 or higher installed
- Gradle installed (or use the wrapper)
- Paystack account (sign up at https://paystack.com)

## Step 1: Get Your API Keys

1. Go to https://dashboard.paystack.com/
2. Login or create an account
3. Navigate to Settings → API Keys & Webhooks
4. Copy your **Test Secret Key** (starts with `sk_test_`)

**⚠️ Important:** Never commit your secret key to version control!

## Step 2: Set Up Your Project

### Create New Project

```bash
mkdir my-payment-app
cd my-payment-app
gradle init --type java-application
```

### Add paystack4j Dependency

Edit `build.gradle`:

```gradle
dependencies {
    implementation 'com.thompson:paystack4j:1.0.0'
}
```

For now (since we haven't published to Maven Central), build paystack4j locally:

```bash
# In the paystack4j directory
./gradlew publishToMavenLocal

# Then in your app's build.gradle, add:
repositories {
    mavenLocal()  // Add this
    mavenCentral()
}
```

## Step 3: Your First Payment

Create `SimplePayment.java`:

```java
import com.thompson.paystack.client.PaystackClient;
import com.thompson.paystack.models.request.TransactionInitRequest;
import com.thompson.paystack.models.response.PaystackResponse;
import com.thompson.paystack.models.response.TransactionInitData;
import com.thompson.paystack.enums.Currency;

public class SimplePayment {
    public static void main(String[] args) {
        // Initialize client with your secret key
        PaystackClient client = new PaystackClient("sk_test_YOUR_KEY_HERE");
        
        // Create payment request
        TransactionInitRequest request = TransactionInitRequest.builder()
            .email("[email protected]")
            .amount(5000.00)  // ₦5,000
            .currency(Currency.NGN)
            .reference("TEST_" + System.currentTimeMillis())
            .build();
        
        // Initialize payment
        PaystackResponse<TransactionInitData> response = 
            client.transactions().initialize(request);
        
        // Get payment URL
        if (response.isStatus()) {
            String paymentUrl = response.getData().getAuthorizationUrl();
            System.out.println("Payment URL: " + paymentUrl);
            System.out.println("Reference: " + response.getData().getReference());
            
            // In a web app, redirect user to this URL
            // For testing, open it in your browser
        }
    }
}
```

Run it:

```bash
./gradlew run
```

You'll get a payment URL - open it in your browser to test!

## Step 4: Test the Payment

### Test Cards Provided by Paystack:

**Successful Payment:**
- Card: `4084084084084081`
- CVV: `408`
- Expiry: Any future date
- PIN: `0000`
- OTP: `123456`

**Failed Payment:**
- Card: `5060666666666666666`

### What Happens:

1. Customer is redirected to Paystack checkout
2. They enter card details
3. Complete authentication (OTP, PIN, 3D Secure)
4. Payment is processed
5. Customer is redirected back to your callback URL

## Step 5: Verify the Payment

Create `VerifyPayment.java`:

```java
import com.thompson.paystack.client.PaystackClient;
import com.thompson.paystack.models.response.PaystackResponse;
import com.thompson.paystack.models.response.TransactionData;
import com.thompson.paystack.enums.TransactionStatus;

public class VerifyPayment {
    public static void main(String[] args) {
        PaystackClient client = new PaystackClient("sk_test_YOUR_KEY_HERE");
        
        // Replace with actual reference from Step 3
        String reference = "TEST_1234567890";
        
        // Verify payment
        PaystackResponse<TransactionData> response = 
            client.transactions().verify(reference);
        
        if (response.isStatus()) {
            TransactionData transaction = response.getData();
            
            System.out.println("Status: " + transaction.getStatus());
            System.out.println("Amount: ₦" + (transaction.getAmount() / 100.0));
            System.out.println("Customer: " + transaction.getCustomer().getEmail());
            System.out.println("Paid at: " + transaction.getPaidAt());
            
            // Check if payment was successful
            if (transaction.getStatusEnum() == TransactionStatus.SUCCESS) {
                System.out.println("✅ Payment successful!");
                // Deliver your product/service here
            } else {
                System.out.println("❌ Payment not successful");
            }
        }
    }
}
```

## Step 6: Split Payment (Platform Fee)

Now for your use case - taking 10% platform fee!

```java
import com.thompson.paystack.client.PaystackClient;
import com.thompson.paystack.enums.Bearer;
import com.thompson.paystack.enums.Currency;
import com.thompson.paystack.models.request.SubaccountCreateRequest;
import com.thompson.paystack.models.request.TransactionInitRequest;
import com.thompson.paystack.models.response.*;
import java.math.BigDecimal;

public class SplitPayment {
    public static void main(String[] args) {
        PaystackClient client = new PaystackClient("sk_test_YOUR_KEY_HERE");
        
        // STEP 1: Create subaccount for seller (do once per seller)
        SubaccountCreateRequest subRequest = SubaccountCreateRequest.builder()
            .businessName("John's Store")
            .settlementBank("058")  // GTBank
            .accountNumber("0123456789")
            .percentageCharge(10.0)
            .primaryContactEmail("[email protected]")
            .build();
        
        PaystackResponse<SubaccountData> subResponse = 
            client.subaccounts().create(subRequest);
        
        String subaccountCode = subResponse.getData().getSubaccountCode();
        System.out.println("Subaccount: " + subaccountCode);
        
        // STEP 2: Initialize payment with split
        BigDecimal totalAmount = new BigDecimal("10000.00");  // ₦10,000
        BigDecimal platformFee = new BigDecimal("1000.00");   // 10% = ₦1,000
        
        TransactionInitRequest request = TransactionInitRequest.builder()
            .email("[email protected]")
            .amount(totalAmount)
            .currency(Currency.NGN)
            .subaccount(subaccountCode)           // Seller's subaccount
            .transactionCharge(platformFee)       // Your 10%
            .bearer(Bearer.SUBACCOUNT)            // Seller pays fees
            .reference("SPLIT_" + System.currentTimeMillis())
            .build();
        
        PaystackResponse<TransactionInitData> response = 
            client.transactions().initialize(request);
        
        System.out.println("Payment URL: " + 
            response.getData().getAuthorizationUrl());
        
        // Result after payment:
        // - Seller gets ₦9,000 in their bank account
        // - You get ₦1,000 in your Paystack account
        // - Automatic - no manual transfers needed!
    }
}
```

## Common Patterns

### 1. Environment Variables (Best Practice)

Instead of hardcoding API keys:

```java
String apiKey = System.getenv("PAYSTACK_SECRET_KEY");
PaystackClient client = new PaystackClient(apiKey);
```

Set in your environment:
```bash
export PAYSTACK_SECRET_KEY=sk_test_your_key_here
```

### 2. Spring Boot Integration

```java
@Configuration
public class PaystackConfig {
    
    @Value("${paystack.secret-key}")
    private String secretKey;
    
    @Bean
    public PaystackClient paystackClient() {
        return new PaystackClient(secretKey);
    }
}

@Service
public class PaymentService {
    
    @Autowired
    private PaystackClient paystackClient;
    
    public String initializePayment(String email, BigDecimal amount) {
        TransactionInitRequest request = TransactionInitRequest.builder()
            .email(email)
            .amount(amount)
            .build();
            
        PaystackResponse<TransactionInitData> response = 
            paystackClient.transactions().initialize(request);
            
        return response.getData().getAuthorizationUrl();
    }
}
```

### 3. Error Handling

```java
import com.thompson.paystack.exceptions.*;

try {
    PaystackResponse<TransactionInitData> response = 
        client.transactions().initialize(request);
        
} catch (PaystackApiException e) {
    // API returned an error
    System.err.println("API Error: " + e.getMessage());
    System.err.println("Status Code: " + e.getStatusCode());
    
} catch (PaystackAuthException e) {
    // Invalid API key
    System.err.println("Auth Error: " + e.getMessage());
    
} catch (PaystackException e) {
    // General error (network, etc.)
    System.err.println("Error: " + e.getMessage());
}
```

### 4. Callback Handler (Web App)

```java
@RestController
@RequestMapping("/api/payment")
public class PaymentController {
    
    @Autowired
    private PaystackClient paystackClient;
    
    @GetMapping("/callback")
    public String handleCallback(@RequestParam String reference) {
        try {
            PaystackResponse<TransactionData> response = 
                paystackClient.transactions().verify(reference);
            
            TransactionData transaction = response.getData();
            
            if (transaction.getStatusEnum() == TransactionStatus.SUCCESS) {
                // Payment successful
                // 1. Update order status in database
                // 2. Send confirmation email
                // 3. Deliver product/service
                return "redirect:/success?ref=" + reference;
            } else {
                return "redirect:/failed";
            }
            
        } catch (PaystackException e) {
            return "redirect:/error";
        }
    }
}
```

## Testing Checklist

- [ ] Initialize payment successfully
- [ ] Test with success card (4084084084084081)
- [ ] Test with failed card (5060666666666666666)
- [ ] Verify successful payment
- [ ] Verify failed payment
- [ ] Test split payment with subaccount
- [ ] Handle network errors gracefully
- [ ] Handle API errors gracefully

## Going to Production

### 1. Switch to Live Keys

```java
// Replace test key with live key
PaystackClient client = new PaystackClient("sk_live_your_live_key");
```

### 2. Security Checklist

- [ ] Never expose secret key in client-side code
- [ ] Use environment variables for API keys
- [ ] Verify webhook signatures (implement later)
- [ ] Always verify payments on server-side
- [ ] Check amount matches expected value
- [ ] Prevent double-fulfillment (check if already processed)
- [ ] Use HTTPS for all callbacks
- [ ] Store transaction references in database

### 3. Best Practices

- Always verify transactions server-side
- Store subaccount codes in your database
- Log all payment attempts and results
- Implement retry logic for network failures
- Set up monitoring and alerts
- Test thoroughly in test mode first
- Have a manual reconciliation process

## Next Steps

1. ✅ Complete this quick start
2. 📖 Read the full README.md
3. 🔍 Explore the example code
4. 🧪 Write unit tests for your integration
5. 🚀 Deploy to production
6. 📊 Set up webhook handlers (advanced)

## Need Help?

- **Library Issues:** Open an issue on GitHub
- **Paystack Questions:** Check https://paystack.com/docs/
- **Payment Questions:** Contact [email protected]

## Resources

- paystack4j Documentation: README.md
- Paystack API Docs: https://paystack.com/docs/api/
- Test Cards: https://paystack.com/docs/payments/test-payments/
- Bank Codes: https://paystack.com/docs/api/#miscellaneous-bank

---

**Happy coding with paystack4j! 🎉**