package com.thompson.paystack.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.thompson.paystack.adapters.TransactionMetadataDeserializer;
import com.thompson.paystack.models.response.TransactionMetadata;

/**
 * Utility class for JSON serialization/deserialization
 */
public class JsonUtils {
    private static final Gson GSON = new GsonBuilder()
            .registerTypeAdapter(TransactionMetadata.class, new TransactionMetadataDeserializer())
            .create();

    // Private constructor to prevent instantiation
    private JsonUtils(){}

    /**
     * Convert object to JSON string
     *
     * @param obj Object to serialize
     * @return JSON string
     */
    public static String toJson(Object obj) {
        return GSON.toJson(obj);
    }

    /**
     * Convert JSON string to object
     *
     * @param json     JSON string
     * @param classOfT Class of the object
     * @param <T>      Type of the object
     * @return Deserialized object
     */
    public static <T> T fromJson(String json, Class<T> classOfT) {
        return GSON.fromJson(json, classOfT);
    }

    /**
     * Get the Gson instance
     *
     * @return Gson instance
     */
    public static Gson getGson() {
        return GSON;
    }
}
