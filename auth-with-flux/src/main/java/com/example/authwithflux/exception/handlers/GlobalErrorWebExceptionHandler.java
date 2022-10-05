package com.example.authwithflux.exception.handlers;

import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.boot.autoconfigure.web.reactive.error.AbstractErrorWebExceptionHandler;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.context.ApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.http.ReactiveHttpOutputMessage;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserter;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.*;
import reactor.core.publisher.Mono;

import java.util.Map;

import static org.springframework.boot.web.error.ErrorAttributeOptions.Include.MESSAGE;

@Component
public class GlobalErrorWebExceptionHandler extends AbstractErrorWebExceptionHandler {

    public GlobalErrorWebExceptionHandler(
            ErrorAttributes errorAttributes,
            ApplicationContext applicationContext,
            ServerCodecConfigurer serverCodecConfigurer
    ) {

        super(errorAttributes, new WebProperties.Resources(), applicationContext);
        super.setMessageWriters(serverCodecConfigurer.getWriters());
        super.setMessageReaders(serverCodecConfigurer.getReaders());

    }

    @Override
    protected RouterFunction<ServerResponse> getRoutingFunction(final ErrorAttributes errorAttributes) {

        HandlerFunction<ServerResponse> renderErrorResponse = this::renderErrorResponse;
        RequestPredicate requestPredicate = RequestPredicates.all();

        return RouterFunctions.route(requestPredicate, renderErrorResponse);
    }


    private Mono<ServerResponse> renderErrorResponse(ServerRequest request) {
        Map<String, Object> error = getErrorAttributes(request, ErrorAttributeOptions.of(MESSAGE));

        int httpStatus = getHttpStatus(error);

        BodyInserter<Map<String, Object>, ReactiveHttpOutputMessage> reactiveHttpOutputMessageBodyInserter =
                BodyInserters.fromValue(error);

        return ServerResponse
                .status(httpStatus)
                .contentType(MediaType.APPLICATION_JSON)
                .body(reactiveHttpOutputMessageBodyInserter);
    }

    private int getHttpStatus(Map<String, Object> errorAttributes) {
        return (int) errorAttributes.get("status");
    }
}