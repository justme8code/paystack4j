# Changelog

All notable changes to paystack4j will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [1.0.0] - 2025-01-09

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
- SL4J 2.0.17 for Logging
- DotEnvJava 3.2.0 for loading env files.

---

## [Unreleased]

### Planned Features
- Transfer API (send money to customers)
- Refund API
- Customer management
- Plans and subscriptions
- Bulk charges
- Webhook signature verification
- Async API support
- More payment channels (Bank Transfer, USSD, QR)

---

## Version History

### Version Format
- **MAJOR** version for incompatible API changes
- **MINOR** version for new functionality (backward compatible)
- **PATCH** version for bug fixes (backward compatible)

### How to Upgrade

```gradle
dependencies {
    implementation 'com.thompson:paystack4j:1.0.0'
}
```

---

## Support

- **Documentation**: See README.md
- **Issues**: https://github.com/justme8code/paystack4j/issues
- **Discussions**: https://github.com/justme8code/paystack4j/discussions