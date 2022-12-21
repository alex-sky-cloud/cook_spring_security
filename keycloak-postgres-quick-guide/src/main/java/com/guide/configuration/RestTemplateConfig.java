package com.guide.configuration;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

@Configuration
public class RestTemplateConfig {

    @Bean
    public RestTemplateBuilder restTemplateBuilder() {
        return new RestTemplateBuilder();
    }


    /**
     * Connection timeout is used when opening a communications
     * link to the remote resource.
     * A java.net.SocketTimeoutException is thrown if the timeout expires
     * before the connection can be established
     * <p>
     * Read timeout is used when reading from Input Stream
     * when a connection is established to a remote resource.
     * A java.net.SocketTimeoutException is also thrown
     * if the timeout expires before there is data available for reading
     */
    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder restTemplateBuilder) {

        int connectionTimeout = 3;
        int readTimeout = 2;

        return restTemplateBuilder
                .setConnectTimeout(Duration.ofSeconds(connectionTimeout))
                .setReadTimeout(Duration.ofSeconds(readTimeout))
                .build();
    }
}
