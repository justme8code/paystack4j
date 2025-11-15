package com.thompson.paystack.models.response;

import lombok.Data;

import java.util.Map;

/**
 * Custom metadata attached to transaction
 */
@Data
public class TransactionMetadata {
    private Map<String, Object> customFields;
}
