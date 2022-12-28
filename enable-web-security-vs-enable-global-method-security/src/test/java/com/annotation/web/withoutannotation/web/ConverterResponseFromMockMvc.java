package com.annotation.web.withoutannotation.web;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.MvcResult;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Optional;

/**
 * Here is utilities that performs converting responses
 * when using MockMvc
 */
@Component
@Slf4j
public class ConverterResponseFromMockMvc {

    public static final String PREFIX_MESSAGE_ERROR = "!!!_Test : ";

    private static ObjectMapper objectMapper;

    static {
        objectMapper = configureObjectMapper();
    }

    private static ObjectMapper configureObjectMapper() {

        objectMapper = new ObjectMapper();

        return objectMapper
                .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .registerModule(new JavaTimeModule());
    }


    public <E> E getResultList(MvcResult result, Class<E> type) {

        E objectListFromResponse;
        try {
            String content = result.getResponse().getContentAsString();
            ObjectMapper objectMapper = new ObjectMapper();
            objectListFromResponse = objectMapper.readValue(content, type);

            return objectListFromResponse;

        } catch (UnsupportedEncodingException | JsonProcessingException e) {
            log.error(PREFIX_MESSAGE_ERROR + e.getLocalizedMessage());
        }

        return (E) Optional.empty();
    }


    /**
     * This method allows you to prepare the request body as a string and
     * then pass as an argument in a request to the server via MockMvc
     *
     * @param request - It is a type of Object that will be to send
     *                as RequestBody
     *                <b>objectMapper</b> - It is converter
     *                to serialize Java objects into JSON
     *                and deserialize JSON string into Java objects
     * @return requestBody as a string
     */
    public String requestBody(Object request) {
        try {
            return objectMapper.writeValueAsString(request);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * @param result        -  a result that is being get via MockMvc from rest service
     * @param responseClass - a type class' of object that It
     *                      will be presented in response
     *                      <b>objectMapper</b>   - It is converter
     *                      to serialize Java objects into JSON
     *                      and deserialize JSON string into Java objects
     * @param <T>           - a name that It needs for generic type
     * @return - response in expected a type of object
     */
    public <T> T parseResponse(MvcResult result, Class<T> responseClass) {

        try {
            String contentAsString = result
                    .getResponse()
                    .getContentAsString();

            return objectMapper.readValue(contentAsString, responseClass);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
