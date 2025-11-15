# Webhook Guide for paystack4j

Complete guide to handling Paystack webhooks with paystack4j.

## 📋 Table of Contents

- [What are Webhooks?](#what-are-webhooks)
- [Why Use Webhooks?](#why-use-webhooks)
- [Quick Start](#quick-start)
- [Webhook Events](#webhook-events)
- [Security](#security)
- [Examples](#examples)
- [Testing](#testing)
- [Best Practices](#best-practices)

---

## What are Webhooks?

Webhooks are **automatic notifications** sent by Paystack to your server when events occur (like successful payments, failed transfers, disputes, etc.).

**Think of it as:** Paystack calling your API instead of you constantly checking theirs.

---

## Why Use Webhooks?

### ❌ Without Webhooks:
- Customer pays and closes browser
- Payment succeeds but you never know
- Customer doesn't get their product
- Bad user experience

### ✅ With Webhooks:
- Customer pays
- Paystack instantly notifies your server
- You deliver the product immediately
- Customer happy even if they closed the browser!

---

## Quick Start

### Step 1: Create Webhook Endpoint

```java
@RestController
@RequestMapping("/api/paystack")
public class WebhookController {
    
    private final PaystackClient paystackClient;
    
    public WebhookController() {
        String secretKey = System.getenv("PAYSTACK_SECRET_KEY");
        this.paystackClient = new PaystackClient(secretKey);
    }
    
    @PostMapping("/webhook")
    public ResponseEntity<String> handleWebhook(
            @RequestBody String payload,
            @RequestHeader("x-paystack-signature") String signature) {
        
        // Verify and parse webhook
        WebhookHandler webhookHandler = paystackClient.webhooks();
        WebhookPayload webhook = webhookHandler.verifyAndParse(payload, signature);
        
        if (webhook == null) {
            return ResponseEntity.status(401).body("Invalid signature");
        }
        
        // Handle the event
        if (webhook.isChargeSuccess()) {
            TransactionData transaction = webhookHandler.parseAsTransaction(webhook);
            processPayment(transaction);
        }
        
        return ResponseEntity.ok("Webhook processed");
    }
    
    private void processPayment(TransactionData transaction) {
        // Your logic here
    }
}
```

### Step 2: Configure URL in Paystack Dashboard

1. Go to: https://dashboard.paystack.com/settings/developer
2. Click **Webhooks**
3. Add your URL: `https://yourdomain.com/api/paystack/webhook`
4. Select events to receive (or select all)
5. Save

### Step 3: Test It!

Make a test payment and watch your webhook endpoint receive notifications!

---

## Webhook Events

paystack4j supports all Paystack webhook events:

### Payment Events

| Event | When it fires |
|-------|---------------|
| `charge.success` | Payment completed successfully |
| `charge.failed` | Payment failed |

### Transfer Events

| Event | When it fires |
|-------|---------------|
| `transfer.success` | Transfer sent successfully |
| `transfer.failed` | Transfer failed |
| `transfer.reversed` | Transfer was reversed |

### Subscription Events

| Event | When it fires |
|-------|---------------|
| `subscription.create` | New subscription started |
| `subscription.disable` | Subscription cancelled |
| `invoice.create` | New invoice generated |
| `invoice.payment_failed` | Subscription payment failed |

### Dispute Events

| Event | When it fires |
|-------|---------------|
| `dispute.create` | Customer disputes a charge |
| `dispute.remind` | Reminder to respond to dispute |
| `dispute.resolve` | Dispute resolved |

---

## Security

### ⚠️ CRITICAL: Always Verify Signatures

**Never trust webhook data without verification!** Anyone can send fake data to your endpoint.

```java
// ❌ DANGEROUS - Don't do this!
@PostMapping("/webhook")
public String handleWebhook(@RequestBody String payload) {
    // Processing without verification = security risk!
    processPayment(payload);
}

// ✅ SAFE - Always do this!
@PostMapping("/webhook")
public String handleWebhook(
        @RequestBody String payload,
        @RequestHeader("x-paystack-signature") String signature) {
    
    WebhookHandler handler = paystackClient.webhooks();
    
    // Verify signature first
    if (!handler.verifySignature(payload, signature)) {
        return "Invalid signature";
    }
    
    // Now safe to process
    processPayment(payload);
}
```

### How Signature Verification Works

1. Paystack signs each webhook with your secret key using HMAC SHA512
2. Signature is sent in `x-paystack-signature` header
3. You compute the expected signature and compare
4. If they match, webhook is authentic

paystack4j handles all this for you automatically!

---

## Examples

### Example 1: Basic Webhook Handler

```java
PaystackClient client = new PaystackClient(secretKey);
WebhookHandler handler = client.webhooks();

@PostMapping("/webhook")
public String handleWebhook(String payload, String signature) {
    
    // Verify and parse in one call
    WebhookPayload webhook = handler.verifyAndParse(payload, signature);
    
    if (webhook == null) {
        return "Invalid";
    }
    
    // Check event type
    if (webhook.isChargeSuccess()) {
        TransactionData tx = handler.parseAsTransaction(webhook);
        System.out.println("Payment received: ₦" + (tx.getAmount() / 100.0));
    }
    
    return "OK";
}
```

### Example 2: Using WebhookListener Interface

```java
// Create a listener
class MyListener implements WebhookListener {
    
    @Override
    public void onChargeSuccess(TransactionData transaction) {
        // Payment received
        deliverProduct(transaction.getReference());
    }
    
    @Override
    public void onChargeFailed(TransactionData transaction) {
        // Payment failed
        notifyCustomer(transaction);
    }
}

// Set up dispatcher
WebhookDispatcher dispatcher = new WebhookDispatcher(secretKey);
dispatcher.addListener(new MyListener());

// In your controller
@PostMapping("/webhook")
public String handleWebhook(String payload, String signature) {
    dispatcher.dispatch(payload, signature);
    return "OK";
}
```

### Example 3: Multiple Event Handlers

```java
class PaymentListener implements WebhookListener {
    
    @Override
    public void onChargeSuccess(TransactionData tx) {
        // Verify with API (double-check)
        paystackClient.transactions().verify(tx.getReference());
        
        // Check not already processed
        if (isProcessed(tx.getReference())) {
            return;
        }
        
        // Process order
        updateOrderStatus(tx.getReference(), "paid");
        sendConfirmationEmail(tx.getCustomer().getEmail());
        deliverProduct(tx.getReference());
        markAsProcessed(tx.getReference());
    }
    
    @Override
    public void onTransferSuccess(WebhookPayload payload) {
        // Transfer completed
        System.out.println("Transfer successful!");
    }
    
    @Override
    public void onDisputeCreate(WebhookPayload payload) {
        // Customer disputed a charge
        notifySupport("New dispute created!");
    }
}
```

---

## Testing

### Test Locally with ngrok

Since Paystack needs a public URL, use ngrok for local testing:

```bash
# 1. Start your app locally
./gradlew bootRun

# 2. Expose with ngrok
ngrok http 8080

# 3. Use ngrok URL in Paystack dashboard
https://abc123.ngrok.io/api/paystack/webhook
```

### Manual Testing

Send a test webhook manually:

```bash
curl -X POST https://yourdomain.com/api/paystack/webhook \
  -H "Content-Type: application/json" \
  -H "x-paystack-signature: YOUR_COMPUTED_SIGNATURE" \
  -d '{
    "event": "charge.success",
    "data": {
      "reference": "TEST_123",
      "amount": 500000,
      "status": "success",
      "customer": {"email": "[email protected]"}
    }
  }'
```

### Paystack Test Environment

Paystack Dashboard has a "Send Test Webhook" feature:
1. Go to Settings → Webhooks
2. Click "Send test notification"
3. Choose event type
4. Webhook sent to your URL

---

## Best Practices

### 1. ✅ Always Verify Signatures

```java
if (!handler.verifySignature(payload, signature)) {
    return ResponseEntity.status(401).body("Invalid");
}
```

### 2. ✅ Verify with API Too

Don't trust webhook alone - double-check with API:

```java
@Override
public void onChargeSuccess(TransactionData tx) {
    // Verify with Paystack API
    PaystackResponse<TransactionData> response = 
        client.transactions().verify(tx.getReference());
    
    if (response.getData().getStatusEnum() == TransactionStatus.SUCCESS) {
        // Now safe to process
    }
}
```

### 3. ✅ Handle Idempotency

Paystack may send same webhook multiple times:

```java
private void processPayment(String reference) {
    // Check if already processed
    if (isAlreadyProcessed(reference)) {
        System.out.println("Already processed: " + reference);
        return; // Don't process again
    }
    
    // Process payment
    deliverProduct(reference);
    
    // Mark as processed
    markAsProcessed(reference);
}
```

### 4. ✅ Return 200 Quickly

Don't do heavy processing in webhook endpoint:

```java
@PostMapping("/webhook")
public ResponseEntity<String> handleWebhook(String payload, String signature) {
    
    // Verify
    if (!handler.verifySignature(payload, signature)) {
        return ResponseEntity.status(401).body("Invalid");
    }
    
    // Queue for async processing
    webhookQueue.add(payload);
    
    // Return 200 immediately
    return ResponseEntity.ok("Accepted");
}

// Process queue in background
@Scheduled(fixedDelay = 1000)
public void processQueue() {
    String payload = webhookQueue.poll();
    if (payload != null) {
        processWebhook(payload);
    }
}
```

### 5. ✅ Handle Failures Gracefully

Paystack retries if you don't return 200:
- After 1 minute
- After 5 minutes
- After 10 minutes
- Up to 10 times

Make sure your endpoint is reliable!

### 6. ✅ Log Everything

```java
@Override
public void onChargeSuccess(TransactionData tx) {
    logger.info("Webhook received: charge.success");
    logger.info("Reference: {}", tx.getReference());
    logger.info("Amount: {}", tx.getAmount());
    
    try {
        processPayment(tx);
        logger.info("Payment processed successfully");
    } catch (Exception e) {
        logger.error("Error processing payment", e);
        // Still return 200 to Paystack to avoid retries
    }
}
```

### 7. ✅ Monitor Your Webhooks

Track:
- How many webhooks received
- How many failed verification
- Processing time
- Any errors

Set up alerts for:
- Verification failures (possible attack)
- Processing errors
- Slow processing times

---

## Troubleshooting

### Webhook Not Being Received

**Check:**
- URL is correct in Paystack dashboard
- URL is publicly accessible (not localhost)
- Server is running
- Firewall not blocking requests
- Check Paystack webhook logs

### Signature Verification Fails

**Check:**
- Using correct secret key (test vs live)
- Reading raw request body (not parsed JSON)
- Signature header name is correct: `x-paystack-signature`
- No middleware modifying request body

### Webhook Received Multiple Times

**This is normal!** Paystack retries if:
- You don't return 200
- Your server is slow to respond
- Network issues

**Solution:** Implement idempotency (check if already processed)

---

## Advanced Topics

### Webhook Security Checklist

- [ ] ✅ Verify signature on every webhook
- [ ] ✅ Use HTTPS only (never HTTP)
- [ ] ✅ Verify with Paystack API after webhook
- [ ] ✅ Check amount matches expected value
- [ ] ✅ Implement idempotency
- [ ] ✅ Rate limit webhook endpoint
- [ ] ✅ Log all webhooks
- [ ] ✅ Monitor for anomalies

### Webhook vs Callback URL

| Feature | Webhook | Callback URL |
|---------|---------|--------------|
| Reliability | ✅ High | ❌ Low |
| Server-to-server | ✅ Yes | ❌ No |
| Works if customer closes browser | ✅ Yes | ❌ No |
| Security | ✅ Verifiable | ⚠️ Less secure |
| **Use for** | **Processing payments** | **Showing success page** |

**Best practice:** Use BOTH!
- Webhook → Actually process the order
- Callback → Show success message to customer

---

## Summary

Webhooks are **essential** for production applications. They provide:

✅ Real-time notifications  
✅ Reliable payment processing  
✅ Better user experience  
✅ Server-to-server communication

With paystack4j, implementing webhooks is simple and secure!

---

## Next Steps

1. ✅ Add webhook endpoint to your app
2. ✅ Configure URL in Paystack dashboard
3. ✅ Test with test payments
4. ✅ Monitor in production
5. ✅ Handle all relevant events

Need help? Check the examples or open an issue on GitHub!