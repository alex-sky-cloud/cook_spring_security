package com.jwt.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix ="token.time-life")
@EnableConfigurationProperties(TokenTimeLifeProperties.class)
@Component
@Data
public class TokenTimeLifeProperties {

    private Integer access;

    private Integer refresh;
}