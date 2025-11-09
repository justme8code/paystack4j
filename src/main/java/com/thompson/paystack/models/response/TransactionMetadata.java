package com.thompson.paystack.models.response;

import java.util.Map;

/**
 * Custom metadata attached to transaction
 */
public class TransactionMetadata {
    private Map<String, Object> customFields;

    public Map<String, Object> getCustomFields() {
        return customFields;
    }

    public void setCustomFields(Map<String, Object> customFields) {
        this.customFields = customFields;
    }
}
