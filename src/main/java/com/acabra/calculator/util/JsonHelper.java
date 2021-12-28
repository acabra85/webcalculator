package com.acabra.calculator.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.dropwizard.jackson.Jackson;
import lombok.extern.slf4j.Slf4j;


import java.io.IOException;
import java.util.Optional;

/**
 * Created by Agustin on 10/10/2016.
 */
@Slf4j
public class JsonHelper {

    private static final ObjectMapper MAPPER = Jackson.newObjectMapper();


    public static String toJsonString(Object object) {
        try {
            return MAPPER.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            logger.error("error", e);
            return "{}";
        }
    }

    public static <T> Optional<T> fromJsonString(String body, Class<T> clazz) {
        try {
            return Optional.of(MAPPER.readValue(body, clazz));
        } catch (IOException e) {
            logger.error("error", e);
            return Optional.empty();
        }
    }
}
