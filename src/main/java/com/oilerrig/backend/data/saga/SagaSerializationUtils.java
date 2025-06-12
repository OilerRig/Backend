package com.oilerrig.backend.data.saga;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class SagaSerializationUtils {

    private static final Logger log = LoggerFactory.getLogger(SagaSerializationUtils.class);

    private static final ObjectMapper OBJECT_MAPPER;

    static {
        OBJECT_MAPPER = new ObjectMapper();
        OBJECT_MAPPER.registerModule(new JavaTimeModule());
        OBJECT_MAPPER.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        // OBJECT_MAPPER.enable(SerializationFeature.INDENT_OUTPUT);
    }

    public static <T> String serializeToJsonString(T object) {
        if (object == null) {
            return null;
        }
        try {
            return OBJECT_MAPPER.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            log.error("Failed to serialize object to JSON string: {}", object, e);
            throw new RuntimeException("Failed to serialize object to JSON string", e);
        }
    }

    public static <T> T deserializeFromJsonString(String jsonString, Class<T> clazz) {
        if (jsonString == null || jsonString.isEmpty()) {
            return null;
        }
        try {
            return OBJECT_MAPPER.readValue(jsonString, clazz);
        } catch (JsonProcessingException e) {
            log.error("Failed to deserialize JSON string to object of type {}: {}", clazz.getName(), jsonString, e);
            throw new RuntimeException("Failed to deserialize JSON string to object", e);
        }
    }

    public static <T> byte[] serializeToJsonBytes(T object) {
        if (object == null) {
            return null;
        }
        try {
            return OBJECT_MAPPER.writeValueAsBytes(object);
        } catch (JsonProcessingException e) {
            log.error("Failed to serialize object to JSON bytes: {}", object, e);
            throw new RuntimeException("Failed to serialize object to JSON bytes", e);
        }
    }

    public static <T> T deserializeFromJsonBytes(byte[] jsonBytes, Class<T> clazz) {
        if (jsonBytes == null || jsonBytes.length == 0) {
            return null;
        }
        try {
            return OBJECT_MAPPER.readValue(jsonBytes, clazz);
        } catch (IOException e) {
            log.error("Failed to deserialize JSON bytes to object of type {}: {}", clazz.getName(), new String(jsonBytes, StandardCharsets.UTF_8), e);
            throw new RuntimeException("Failed to deserialize JSON bytes to object", e);
        }
    }
}
