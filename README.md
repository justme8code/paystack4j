# paystack4j

[![JAVA](https://raw.githubusercontent.com/github/explore/refs/heads/main/topics/java/java.png)](https://www.java.com) [![PAYSTACK](https://avatars.githubusercontent.com/u/14998667?s=200&v=4)](http://paystack.com/)

[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)
[![Java](https://img.shields.io/badge/Java-21%2B-blue.svg)](https://www.java.com)

A modern, easy-to-use Java library for integrating Paystack payment gateway with split payment support.

## Features

✅ **Transaction Initialization** - Create payment links for customers  
✅ **Payment Verification** - Verify completed transactions  
✅ **Subaccount Management** - Create and manage seller subaccounts  
✅ **Split Payments** - Automatically split payments between platform and sellers  
✅ **Webhook Support** - Secure webhook handling with signature verification  
✅ **Type-Safe** - Full type safety with enums and models  
✅ **Java 8+ Compatible** - Works with Java 8 and above  
✅ **Builder Pattern** - Easy-to-use fluent API

## Requirements

- Java 21 or higher
- Gradle (for building)

## Installation

### Gradle

Add JitPack repository to your `build.gradle`:

```gradle
repositories {
    mavenCentral()
    maven { url 'https://jitpack.io' }
}
```

Add the dependency:

```gradle
dependencies {
    implementation 'com.github.YOUR_USERNAME:paystack4j:1.0.0'
}
```

Replace `YOUR_USERNAME` with your GitHub username.

### Maven

Add JitPack repository to your `pom.xml`:

```xml
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>
```

Add the dependency:

```xml
<dependency>
    <groupId>com.github.YOUR_USERNAME</groupId>
    <artifactId>paystack4j</artifactId>
    <version>1.0.0</version>
</dependency>
```

### Manual Installation

Until published to JitPack, you can install locally:

1. Clone this repository
2. Run `./gradlew publishToMavenLocal`
3. Add `mavenLocal()` to your repositories

## Quick Start

### 1. Initialize the Client

```java
PaystackClient client = new PaystackClient("sk_test_your_secret_key");
```

### 2. Create a Payment

```java
TransactionInitRequest request = TransactionInitRequest.builder()
    .email("[email protected]")
    .amount(10000.00)  // ₦10,000
    .currency(Currency.NGN)
    .reference("TXN_" + System.currentTimeMillis())
    .build();

PaystackResponse<TransactionInitData> response = client.transactions().initialize(request);

// Redirect customer to payment page
String paymentUrl = response.getData().getAuthorizationUrl();
```

### 3. Verify Payment

```java
PaystackResponse<TransactionData> verification = 
    client.transactions().verify("transaction_reference");

if (verification.getData().getStatusEnum() == TransactionStatus.SUCCESS) {
    // Payment successful - deliver value
}
```

## Split Payments (Platform Fee)

Use this when you want to automatically split payments between your platform and sellers.

**Scenario:** Customer pays ₦10,000, your platform takes 10% (₦1,000), seller gets ₦9,000

### Step 1: Create Subaccount for Seller (Once)

```java
SubaccountCreateRequest subRequest = SubaccountCreateRequest.builder()
    .businessName("Seller Shop")
    .settlementBank("058")  // Bank code
    .accountNumber("0123456789")
    .percentageCharge(10.0)  // Default split
    .build();

PaystackResponse<SubaccountData> subResponse = 
    client.subaccounts().create(subRequest);

String subaccountCode = subResponse.getData().getSubaccountCode();
// Save this code to your database linked to the seller
```

### Step 2: Initialize Payment with Split

```java
TransactionInitRequest request = TransactionInitRequest.builder()
    .email("[email protected]")
    .amount(10000.00)  // Total amount
    .subaccount("ACCT_xxxxxxxxx")  // Seller's subaccount
    .transactionCharge(1000.00)    // Your platform fee (₦1,000)
    .bearer(Bearer.SUBACCOUNT)     // Seller pays Paystack fees
    .build();

PaystackResponse<TransactionInitData> response = 
    client.transactions().initialize(request);
```

**Result:**
- Seller receives ₦9,000 in their bank account
- You receive ₦1,000 in your main Paystack account
- Automatic settlement - no manual transfers needed!

## API Reference

### PaystackClient

Main entry point for the library.

```java
PaystackClient client = new PaystackClient(secretKey);
client.transactions()  // Access transaction operations
client.subaccounts()   // Access subaccount operations
```

### TransactionService

#### `initialize(TransactionInitRequest request)`

Create a new payment transaction.

**Parameters:**
- `email` (required) - Customer's email
- `amount` (required) - Amount in major currency units (e.g., 100.50)
- `currency` - Currency code (default: NGN)
- `reference` - Unique transaction reference
- `subaccount` - Subaccount code for split payments
- `transactionCharge` - Platform fee amount
- `bearer` - Who pays Paystack fees (ACCOUNT or SUBACCOUNT)
- `callbackUrl` - URL to redirect customer after payment
- `metadata` - Custom data to attach to transaction

**Returns:** `PaystackResponse<TransactionInitData>`
- `authorizationUrl` - Redirect customer here to pay
- `accessCode` - Payment access code
- `reference` - Transaction reference

#### `verify(String reference)`

Verify a transaction status.

**Parameters:**
- `reference` - Transaction reference to verify

**Returns:** `PaystackResponse<TransactionData>`
- Contains complete transaction details including status

### SubaccountService

#### `create(SubaccountCreateRequest request)`

Create a new subaccount for a seller.

**Parameters:**
- `businessName` (required) - Seller's business name
- `settlementBank` (required) - Bank code (e.g., "058" for GTBank)
- `accountNumber` (required) - Bank account number
- `percentageCharge` - Default percentage split
- `primaryContactEmail` - Contact email
- `primaryContactName` - Contact name
- `primaryContactPhone` - Contact phone

**Returns:** `PaystackResponse<SubaccountData>`
- `subaccountCode` - Use this for split payments

#### `get(String subaccountCode)`

Fetch subaccount details.

## Amount Handling

The library automatically handles currency conversion:

```java
// All these work:
.amount(100.50)                    // Double
.amount(new BigDecimal("100.50"))  // BigDecimal (recommended)
.amountInKobo(10050)              // Direct kobo (if needed)
```

**Note:** Paystack requires amounts in kobo (smallest unit). ₦100 = 10,000 kobo

## Error Handling

```java
try {
    PaystackResponse<TransactionInitData> response = 
        client.transactions().initialize(request);
    
} catch (PaystackApiException e) {
    // API returned an error
    System.out.println("Error: " + e.getMessage());
    System.out.println("Status Code: " + e.getStatusCode());
    
} catch (PaystackAuthException e) {
    // Authentication failed (invalid API key)
    System.out.println("Auth Error: " + e.getMessage());
    
} catch (PaystackException e) {
    // General error (network, etc.)
    System.out.println("Error: " + e.getMessage());
}
```

## Best Practices

### 1. Always Verify Payments

```java
TransactionData transaction = verifyResponse.getData();

// Check status
if (transaction.getStatusEnum() != TransactionStatus.SUCCESS) {
    return; // Don't deliver value
}

// Check amount matches expected
long expectedKobo = expectedAmount.multiply(new BigDecimal("100")).longValue();
if (transaction.getAmount() != expectedKobo) {
    // Amount mismatch - investigate!
}

// Check you haven't already processed this transaction
// (prevents double fulfillment)
```

### 2. Use Unique References

```java
String reference = "TXN_" + userId + "_" + System.currentTimeMillis();
```

### 3. Store Subaccount Codes

Save the `subaccountCode` in your database linked to each seller. You'll need it for every transaction.

### 4. Handle Webhooks

For production, implement Paystack webhooks to get real-time payment notifications instead of relying only on callback URLs.

### 5. Test Mode

Use test API keys (start with `sk_test_`) during development. Paystack provides test cards for testing.

## Testing

Paystack provides test cards you can use:

**Success:** `4084084084084081`  
**Declined:** `5060666666666666666`

## Bank Codes

Common Nigerian bank codes:

- Access Bank: `044`
- GTBank: `058`
- Zenith Bank: `057`
- First Bank: `011`
- UBA: `033`

[Full list of bank codes](https://paystack.com/docs/api/#miscellaneous-bank)

## Configuration

### Custom Base URL (for testing)

```java
PaystackConfig config = new PaystackConfig("sk_test_xxx", "https://custom-url.com");
PaystackClient client = new PaystackClient(config);
```

### Custom HTTP Client

```java
OkHttpClient httpClient = new OkHttpClient.Builder()
    .connectTimeout(60, TimeUnit.SECONDS)
    .build();

PaystackClient client = new PaystackClient(config, httpClient);
```

## Examples

See the `PaymentExample.java` file for complete working examples including:
- Creating subaccounts
- Initializing split payments
- Verifying transactions
- Handling callbacks
See the example page https://github.com/justme8code/paystack4jexamples

## Support

- **Paystack Documentation:** https://paystack.com/docs/api/
- **Paystack Support:** [email protected]

## License

MIT License

## Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

## Roadmap

Future features planned:
- [ ] Transfer API (send money to customers)
- [ ] Customer management
- [ ] Plans and Subscriptions
- [ ] Refunds
- [ ] Bulk charges
- [ ] Async API support
- [ ] Webhook signature verification

## Author

Thompson - paystack4j

---

**Note:** This library is not officially affiliated with Paystack. It's a community-built integration library.