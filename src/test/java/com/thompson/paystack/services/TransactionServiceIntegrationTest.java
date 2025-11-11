package com.thompson.paystack.services;

import com.thompson.paystack.client.PaystackClient;
import com.thompson.paystack.models.request.TransactionInitRequest;
import com.thompson.paystack.models.response.PaystackResponse;
import com.thompson.paystack.models.response.TransactionData;
import com.thompson.paystack.models.response.TransactionInitData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TransactionServiceIntegrationTest {

    private PaystackClient paystackClient;
    private TransactionService transactionService;

    @BeforeEach
    void setUp() {
        // Use your test key
        paystackClient = new PaystackClient("sk_test_f8b1a6723502d6c46244879703d952e1af937dd2");
        transactionService = paystackClient.transactions();
    }

    @Test
    void initializeTransaction_shouldSucceed() {
        TransactionInitRequest request = TransactionInitRequest.builder()
                .amount(1000) // in kobo (₦10.00)
                .email("test@example.com")
                .build();

        PaystackResponse<TransactionInitData> response = transactionService.initialize(request);

        assertTrue(response.isStatus(), "Initialization should succeed");
        assertNotNull(response.getData());
        assertNotNull(response.getData().getAuthorizationUrl());
        assertNotNull(response.getData().getReference());

        System.out.println("Authorization URL: " + response.getData().getAuthorizationUrl());
        System.out.println("Reference: " + response.getData().getReference());
    }

    @Test
    void verifyTransaction_shouldSucceed() {
        // You need a valid reference returned from initialize
        TransactionInitRequest request = TransactionInitRequest.builder()
                .amount(1000)
                .email("test@example.com")
                .build();

        PaystackResponse<TransactionInitData> initResponse = transactionService.initialize(request);
        String reference = initResponse.getData().getReference();

        // Verify immediately (in real case, payment should happen first)
        PaystackResponse<TransactionData> verifyResponse = transactionService.verify(reference);

        assertTrue(verifyResponse.isStatus(), "Verification should succeed");
        assertNotNull(verifyResponse.getData());
        assertEquals(reference, verifyResponse.getData().getReference());

        System.out.println("Transaction Status: " + verifyResponse.getData().getStatus());
    }
}
