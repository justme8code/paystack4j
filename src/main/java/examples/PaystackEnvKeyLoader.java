package examples;

import io.github.cdimascio.dotenv.Dotenv;

public class PaystackEnvKeyLoader {
    private static final Dotenv dotenv = Dotenv.load();

    private PaystackEnvKeyLoader() {}

    public static String getPaystackPublicKey() {
        String apiKey = System.getenv("PAYSTACK_PUBLIC_KEY");
        if (apiKey == null) {
            throw new RuntimeException("Environment variable PAYSTACK_PUBLIC_KEY is not set");
        }
        return apiKey;
    }

    public static String getPaystackSecretKey() {
        return dotenv.get("PAYSTACK_SECRET_KEY");
    }
}
