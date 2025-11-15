# Changelog

All notable changes to paystack4j will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [1.2.0] - 2025-11-15

### Added
- **Transfer API**: Send money to bank accounts
    - `TransferService` for transfer operations
    - Create transfer recipients
    - Initiate transfers
    - Verify transfer status
- **Charge Authorization**: Recurring payments with saved cards
    - `ChargeAuthorizationRequest` model
    - `TransactionService.chargeAuthorization()` method
- **Multi-Country Support**
    - `RecipientType` enum (NUBAN, MOBILE_MONEY, BASA)
    - Support for Ghana mobile money
    - Support for South African bank transfers
- Transfer models: `TransferData`, `TransferRecipientData`

### Use Cases
- Escrow/marketplace platforms (hold and release funds)
- Subscription billing (auto-charge saved cards)
- Installment payments
- Freelance platforms (pay after confirmation)

---

## [1.1.2] - 2025-11-11

### Fixed
- **Critical**: Fixed webhook signature verification logic inversion
- `WebhookHandler.verifyAndParse()` now correctly returns null for invalid signatures

---

## [1.1.1] - 2025-11-11

### Fixed
- Minor bug fixes and improvements

---

## [1.1.0] - 2025-11-10

### Added
- **Webhook Support** with signature verification
    - `WebhookHandler` for processing webhooks
    - `WebhookListener` interface for event handling
    - `WebhookDispatcher` for routing events to multiple listeners
    - `WebhookPayload` model
    - `WebhookEvent` enum with all Paystack events
    - `WebhookSignatureVerifier` for security (HMAC SHA512)
- Comprehensive webhook documentation (WEBHOOK_GUIDE.md)
- Multiple webhook handling examples

### Security
- HMAC SHA512 signature verification
- Constant-time comparison to prevent timing attacks
- Protection against fake webhooks

---

## [1.0.0] - 2025-11-09

### Added
- Initial release of paystack4j
- Transaction initialization support
- Payment verification support
- Subaccount creation and management
- Split payment functionality with platform fees
- Automatic currency conversion (Naira to Kobo)
- Builder pattern for request objects
- Comprehensive error handling with custom exceptions
- Type-safe enums for Currency, Bearer, and TransactionStatus
- Full JavaDoc documentation
- Complete examples and quick start guide

### Features
- **TransactionService**: Initialize and verify payments
- **SubaccountService**: Create and manage subaccounts
- **Split Payments**: Automatic platform fee deduction
- **Amount Utils**: Seamless currency conversion
- **Error Handling**: PaystackException hierarchy
- **Java 8+ Support**: Compatible with Java 8 and above

### Dependencies
- OkHttp 4.12.0 for HTTP client
- Gson 2.10.1 for JSON processing
- JUnit 4.13.2 for testing
- SLF4J 2.0.17 for Logging
- DotEnvJava 3.2.0 for loading env files

---

## [Unreleased]

### Planned Features
- Refund API
- Customer management
- Plans and subscriptions API
- Bulk charges
- Async API support
- More payment channels (Bank Transfer, USSD, QR)
- Disputes API

---

## Version History

### Version Format
- **MAJOR** version for incompatible API changes
- **MINOR** version for new functionality (backward compatible)
- **PATCH** version for bug fixes (backward compatible)

### How to Upgrade

```gradle
dependencies {
    implementation 'com.github.justme8code:paystack4j:1.2.0'
}
```

---

## Support

- **Documentation**: See README.md
- **Issues**: https://github.com/justme8code/paystack4j/issues
- **Discussions**: https://github.com/justme8code/paystack4j/discussions