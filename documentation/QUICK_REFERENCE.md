# paystack4j Quick Reference

One-page cheat sheet for common operations.

## 🚀 Installation

```gradle
repositories {
    maven { url 'https://jitpack.io' }
}
dependencies {
    implementation 'com.github.YOUR_USERNAME:paystack4j:1.0.0'
}
```

---

## 📦 Import Statements

```java
import com.thompson.paystack.client.PaystackClient;
import com.thompson.paystack.models.request.*;
import com.thompson.paystack.models.response.*;
import com.thompson.paystack.enums.*;
```

---

## 🔧 Initialize Client

```java
PaystackClient client = new PaystackClient("sk_test_your_key");
```

---

## 💳 Simple Payment

```java
// Initialize payment
TransactionInitRequest request = TransactionInitRequest.builder()
    .email("[email protected]")
    .amount(5000.00)  // ₦5,000
    .currency(Currency.NGN)
    .reference("TXN_" + System.currentTimeMillis())
    .build();

PaystackResponse<TransactionInitData> response = 
    client.transactions().initialize(request);

// Get payment URL
String paymentUrl = response.getData().getAuthorizationUrl();
// Redirect customer to paymentUrl
```

---

## ✅ Verify Payment

```java
PaystackResponse<TransactionData> response = 
    client.transactions().verify("TXN_123456");

TransactionData tx = response.getData();

if (tx.getStatusEnum() == TransactionStatus.SUCCESS) {
    // Payment successful - deliver product
    long amount = tx.getAmount(); // Amount in kobo
    String email = tx.getCustomer().getEmail();
}
```

---

## 💰 Split Payment (10% Platform Fee)

### Step 1: Create Subaccount (Once per seller)

```java
SubaccountCreateRequest subRequest = SubaccountCreateRequest.builder()
    .businessName("Seller Name")
    .settlementBank("058")  // Bank code
    .accountNumber("0123456789")
    .percentageCharge(10.0)
    .build();

PaystackResponse<SubaccountData> subResponse = 
    client.subaccounts().create(subRequest);

String subaccountCode = subResponse.getData().getSubaccountCode();
// Save this code to database
```

### Step 2: Initialize Split Payment

```java
BigDecimal total = new BigDecimal("10000.00");    // ₦10,000
BigDecimal yourFee = new BigDecimal("1000.00");   // ₦1,000 (10%)

TransactionInitRequest request = TransactionInitRequest.builder()
    .email("[email protected]")
    .amount(total)
    .subaccount(subaccountCode)           // Seller's account
    .transactionCharge(yourFee)           // Your 10%
    .bearer(Bearer.SUBACCOUNT)            // Seller pays fees
    .reference("SPLIT_" + System.currentTimeMillis())
    .build();

// Result: Seller gets ₦9,000, you get ₦1,000
```

---

## 🎯 Common Patterns

### Environment Variable for API Key

```java
String apiKey = System.getenv("PAYSTACK_SECRET_KEY");
PaystackClient client = new PaystackClient(apiKey);
```

### With Callback URL

```java
.callbackUrl("https://yoursite.com/payment/callback")
```

### With Metadata

```java
.addMetadata("order_id", "12345")
.addMetadata("user_id", "user_789")
```

### Amount Conversion

```java
// Automatic conversion
.amount(100.50)  // ₦100.50 → 10050 kobo

// Manual kobo
.amountInKobo(10050)

// From kobo to Naira
BigDecimal naira = AmountUtils.fromKobo(10050); // 100.50
```

---

## 🔍 Transaction Status Check

```java
switch (transaction.getStatusEnum()) {
    case SUCCESS:
        // Payment completed
        break;
    case FAILED:
        // Payment failed
        break;
    case ABANDONED:
        // Customer didn't complete
        break;
    case PENDING:
        // Still processing
        break;
}
```

---

## 🛡️ Error Handling

```java
try {
    PaystackResponse<TransactionInitData> response = 
        client.transactions().initialize(request);
        
} catch (PaystackApiException e) {
    // API error (400, 401, etc.)
    System.err.println("Error: " + e.getMessage());
    System.err.println("Status: " + e.getStatusCode());
    
} catch (PaystackAuthException e) {
    // Invalid API key
    System.err.println("Auth failed: " + e.getMessage());
    
} catch (PaystackException e) {
    // Network or other error
    System.err.println("Error: " + e.getMessage());
}
```

---

## 🧪 Test Cards

### Success Card
```
Card: 4084084084084081
CVV: 408
Expiry: 12/25
PIN: 0000
OTP: 123456
```

### Failed Card
```
Card: 5060666666666666666
```

---

## 🏦 Common Bank Codes

```java
"044" // Access Bank
"058" // GTBank
"057" // Zenith Bank
"011" // First Bank
"033" // UBA
"214" // FCMB
"070" // Fidelity Bank
```

Full list: https://paystack.com/docs/api/#miscellaneous-bank

---

## 📊 Transaction Response Fields

```java
TransactionData tx = response.getData();

tx.getStatus()                    // "success", "failed", etc.
tx.getStatusEnum()                // TransactionStatus enum
tx.getReference()                 // Transaction reference
tx.getAmount()                    // Amount in kobo (long)
tx.getCurrency()                  // "NGN", "GHS", etc.
tx.getChannel()                   // "card", "bank", etc.
tx.getPaidAt()                    // Payment timestamp
tx.getCustomer().getEmail()       // Customer email
tx.getCustomer().getCustomerCode() // Paystack customer code
tx.getAuthorization().getLast4()  // Last 4 card digits
tx.getAuthorization().getBank()   // Card bank name
```

---

## 🔄 Subaccount Response Fields

```java
SubaccountData sub = response.getData();

sub.getSubaccountCode()           // "ACCT_xxxxxxxx"
sub.getBusinessName()             // Business name
sub.getSettlementBank()           // Bank code
sub.getAccountNumber()            // Account number
sub.getPercentageCharge()         // Default split %
sub.isActive()                    // Account status
```

---

## 💡 Best Practices

### 1. Always Verify Server-Side
```java
// ❌ NEVER trust client-side confirmation
// ✅ ALWAYS verify on your server
PaystackResponse<TransactionData> verify = 
    client.transactions().verify(reference);
```

### 2. Check Amount Matches
```java
long expectedKobo = expectedAmount
    .multiply(new BigDecimal("100"))
    .longValue();
    
if (tx.getAmount() != expectedKobo) {
    // Amount mismatch - investigate!
}
```

### 3. Prevent Double Fulfillment
```java
// Check database if already processed
if (isAlreadyProcessed(reference)) {
    return; // Don't deliver again
}
```

### 4. Use Unique References
```java
String ref = "TXN_" + userId + "_" + System.currentTimeMillis();
```

### 5. Never Expose Secret Key
```java
// ❌ Don't hardcode
PaystackClient client = new PaystackClient("sk_test_123...");

// ✅ Use environment variable
String key = System.getenv("PAYSTACK_SECRET_KEY");
PaystackClient client = new PaystackClient(key);
```

---

## 🌐 Useful Links

- **Paystack Dashboard:** https://dashboard.paystack.com
- **Paystack API Docs:** https://paystack.com/docs/api/
- **Test Cards:** https://paystack.com/docs/payments/test-payments/
- **Bank Codes:** https://paystack.com/docs/api/#miscellaneous-bank
- **paystack4j GitHub:** https://github.com/justme8code/paystack4j
- **JitPack:** https://jitpack.io/#justme8code/paystack4j
- **Paystack4j Examples:** https://github.com/justme8code/paystack4jexamples

---

## 📞 Support

- **Library Issues:** Open issue on GitHub
- **Paystack Support:** [email protected]
- **Documentation:** See README.md

---

**Happy coding with paystack4j! 🚀**