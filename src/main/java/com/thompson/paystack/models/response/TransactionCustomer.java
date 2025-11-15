package com.thompson.paystack.models.response;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

/**
 * Customer information from transaction
 */
@Data
public class TransactionCustomer {
    private long id;

    @SerializedName("first_name")
    private String firstName;

    @SerializedName("last_name")
    private String lastName;

    private String email;

    @SerializedName("customer_code")
    private String customerCode;

    private String phone;
}
