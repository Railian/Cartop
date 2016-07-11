package com.cartop.android.core.models;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

public enum RepeatType {

    MONTH("m"),
    WEAK("w"),
    DAY("d"),
    HOUR("h"),
    MINUTE("M"),
    SECOND("s");

    private String label;

    RepeatType(String label) {
        this.label = label;
    }

    private static RepeatType getByLabel(String label) {
        for (RepeatType repeatType : values())
            if (repeatType.label.equals(label)) return repeatType;
        return null;
    }

    public static class Deserializer implements JsonDeserializer<RepeatType> {

        @Override
        public RepeatType deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            return getByLabel(json.getAsString());
        }
    }
}
