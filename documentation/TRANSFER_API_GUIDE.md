# **Transfer API** - Send Money to Bank Accounts
Hold customer payments, then transfer to sellers when ready.

```java
// Create recipient
TransferRecipientRequest recipient = TransferRecipientRequest.builder()
    .name("John Doe")
    .accountNumber("0123456789")
    .bankCode("058")
    .type(RecipientType.NUBAN)  // Nigeria
    .build();

String recipientCode = client.transfers()
    .createRecipient(recipient)
    .getData().getRecipientCode();

// Transfer money later
TransferInitRequest transfer = TransferInitRequest.builder()
    .amount(9000.00)  // ₦9,000
    .recipient(recipientCode)
    .reason("Payment for service")
    .build();

client.transfers().initiate(transfer);
```

### 2. **Charge Authorization** - Recurring Payments
Charge saved cards without checkout page.

```java
// Get auth code from first payment
String authCode = transaction.getAuthorization().getAuthorizationCode();

// Charge again later
ChargeAuthorizationRequest charge = ChargeAuthorizationRequest.builder()
    .authorizationCode(authCode)
    .email("[email protected]")
    .amount(5000.00)
    .build();

client.transactions().chargeAuthorization(charge);
```

### 3. **Multi-Country Support**
Support for Ghana, South Africa mobile money.

```java
.type(RecipientType.MOBILE_MONEY)  // Ghana
.type(RecipientType.BASA)          // South Africa
```

## 📦 What's Included

- `TransferService` - Send money operations
- `ChargeAuthorizationRequest` - Recurring charges
- `RecipientType` enum - Multi-country support
- Transfer verification

## 🎯 Use Cases

✅ **Escrow/Marketplace** - Hold money, release when confirmed  
✅ **Subscriptions** - Auto-charge monthly  
✅ **Installments** - Charge in parts  
✅ **Freelance Platforms** - Pay after work confirmed

## 🔄 Migration from v1.1.x

No breaking changes! Just add new features:

```java
client.transfers()  // New!
client.transactions().chargeAuthorization()  // New!
```

---

**Full changelog:** CHANGELOG.md