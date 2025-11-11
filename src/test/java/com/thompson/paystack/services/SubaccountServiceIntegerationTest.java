package com.thompson.paystack.services;

import com.thompson.paystack.client.PaystackClient;
import com.thompson.paystack.models.request.SubaccountCreateRequest;
import com.thompson.paystack.models.response.PaystackResponse;
import com.thompson.paystack.models.response.SubaccountData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SubaccountServiceIntegerationTest {
    private PaystackClient paystackClient;

    String subaccountCode = "subaccountCode";

    @BeforeEach
    void setUp() {
        paystackClient = new PaystackClient("your key");
    }

    @Test
    void createAndFetchSubaccount_shouldSucceed() {
        // Arrange: Build a subaccount request (Paystack requires unique business name / email)
        SubaccountCreateRequest request = SubaccountCreateRequest.builder()
                .businessName("Test Business " + System.currentTimeMillis())
                .settlementBank("044")  // Access Bank (test value)
                .accountNumber("1234567890")
                .percentageCharge(0.25)
                .build();

        // Act: Create a subaccount
        PaystackResponse<SubaccountData> createResponse = paystackClient.subaccounts().create(request);

        assertTrue(createResponse.isStatus(), "Creation failed: " + createResponse.getMessage());
        assertNotNull(createResponse.getData());
        assertNotNull(createResponse.getData().getSubaccountCode());

        String createdSubaccountCode = createResponse.getData().getSubaccountCode();

        // Act : Fetch it
        PaystackResponse<SubaccountData> getResponse = paystackClient.subaccounts().get(createdSubaccountCode);

        // Assert
        assertTrue(getResponse.isStatus());
        assertEquals(subaccountCode, getResponse.getData().getSubaccountCode());
    }

    @Test
    void getSubaccount_shouldSucceed() {
        PaystackResponse<SubaccountData> getResponse = paystackClient.subaccounts().get(subaccountCode);

        assertTrue(getResponse.isStatus(), "Get should succeed");
        assertEquals(subaccountCode, getResponse.getData().getSubaccountCode());
    }

    @Test
    void getSubaccounts_shouldSucceed() {
        PaystackResponse<List<SubaccountData>> allResponse = paystackClient.subaccounts().getAll();

        System.out.println(allResponse);
        assertTrue(allResponse.isStatus(), "Fetching all subaccounts should succeed");
        assertNotNull(allResponse.getData());
        assertFalse(allResponse.getData().isEmpty(), "There should be at least one subaccount");

        boolean found = allResponse.getData().stream()
                .anyMatch(sub -> sub.getSubaccountCode().equals(subaccountCode));

        assertTrue(found, "Subaccount should have been fetched and subaccount: " +  subaccountCode + "should exist");

    }

    @Test
    void updateSubaccount_shouldSucceed(){
        SubaccountCreateRequest updateRequest = SubaccountCreateRequest.builder()
                .businessName("Updated Biz " + System.currentTimeMillis())
                .settlementBank("044")
                .accountNumber("1234567890")
                .percentageCharge(0.15)
                .verified(true)
                .active(true)
                .build();


        PaystackResponse<SubaccountData> updateResponse =
                paystackClient.subaccounts().update(subaccountCode, updateRequest);

        System.out.println(updateResponse);
        assertTrue(updateResponse.isStatus(), "Update should succeed");
        assertEquals(subaccountCode, updateResponse.getData().getSubaccountCode());
    }
}
