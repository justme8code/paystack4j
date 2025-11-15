# Contributing to paystack4j

Thank you for considering contributing to paystack4j! 🎉

## How to Contribute

### Reporting Bugs

If you find a bug, please open an issue with:
- A clear title and description
- Steps to reproduce the issue
- Expected vs actual behavior
- Your Java version and operating system
- Code samples if applicable

### Suggesting Features

Feature requests are welcome! Please open an issue with:
- A clear description of the feature
- Why you need it
- How it should work
- Example code showing how you'd like to use it

### Pull Requests

1. **Fork the repository**
   ```bash
   git clone https://github.com/yourusername/paystack4j.git
   cd paystack4j
   ```

2. **Create a feature branch**
   ```bash
   git checkout -b feature/my-new-feature
   ```

3. **Make your changes**
    - Follow the existing code style
    - Add tests for new functionality
    - Update documentation if needed

4. **Test your changes**
   ```bash
   ./gradlew test
   ./gradlew build
   ```

5. **Commit your changes**
   ```bash
   git commit -m "Add some feature"
   ```

6. **Push to your fork**
   ```bash
   git push origin feature/my-new-feature
   ```

7. **Open a Pull Request**
    - Describe what your PR does
    - Reference any related issues

## Code Style

- Follow Java naming conventions
- Use meaningful variable names
- Add JavaDoc comments to public methods
- Keep methods focused and small
- Write unit tests for new code

## Testing

Before submitting a PR:

```bash
# Run all tests
./gradlew test

# Build the project
./gradlew build

# Check code formatting
./gradlew check
```

## Areas Where We Need Help

- [ ] Unit tests for existing code
- [ ] Integration tests
- [ ] Transfer API implementation
- [ ] Refund API implementation
- [ ] Webhook signature verification
- [ ] Documentation improvements
- [ ] More examples
- [ ] Performance optimizations

## Questions?

Feel free to open an issue with the "question" label, or reach out to the maintainers.

## Code of Conduct

Be respectful and constructive. We're all here to learn and build something useful together.

## License

By contributing, you agree that your contributions will be licensed under the MIT License.

---

Thank you for helping make paystack4j better! 🚀