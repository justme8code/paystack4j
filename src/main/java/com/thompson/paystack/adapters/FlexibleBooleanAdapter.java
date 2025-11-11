package com.thompson.paystack.adapters;

import com.google.gson.*;

import java.lang.reflect.Type;

public class FlexibleBooleanAdapter implements JsonDeserializer<Boolean> {
    @Override
    public Boolean deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {

        if (json.isJsonNull()) return null;

        if (json.isJsonPrimitive()) {
            JsonPrimitive primitive = json.getAsJsonPrimitive();

            if (primitive.isBoolean()) {
                return primitive.getAsBoolean();
            }

            if (primitive.isNumber()) {
                return primitive.getAsInt() != 0;
            }

            if (primitive.isString()) {
                String str = primitive.getAsString().toLowerCase();
                return str.equals("true") || str.equals("1");
            }
        }

        throw new JsonParseException("Invalid boolean value: " + json);
    }
}
