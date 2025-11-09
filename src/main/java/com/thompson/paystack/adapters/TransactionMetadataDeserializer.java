package com.thompson.paystack.adapters;

import com.google.gson.*;
import com.thompson.paystack.models.response.TransactionMetadata;
import com.thompson.paystack.utils.JsonUtils;

import java.lang.reflect.Type;

public class TransactionMetadataDeserializer implements JsonDeserializer<TransactionMetadata> {

    @Override
    public TransactionMetadata deserialize(JsonElement json, Type typeOfT,
                                           JsonDeserializationContext context) throws JsonParseException {
        // If metadata is null, empty string, or primitive → return null
        if (json == null || json.isJsonNull()) {
            return null;
        }

        if (json.isJsonPrimitive()) {
            String value = json.getAsString();
            if (value == null || value.trim().isEmpty()) {
                return null;
            }

            // Sometimes Paystack returns metadata as a JSON string → try parsing it
            try {
                JsonElement parsed = JsonParser.parseString(value);
                if (parsed.isJsonObject()) {
                    return JsonUtils.getGson().fromJson(parsed, TransactionMetadata.class);
                }
            } catch (JsonSyntaxException ignored) {
                // not a valid JSON string, ignore
            }
            return null;
        }

        // Normal case → proper JSON object
        if (json.isJsonObject()) {
            return JsonUtils.getGson().fromJson(json, TransactionMetadata.class);
        }

        return null;
    }
}
