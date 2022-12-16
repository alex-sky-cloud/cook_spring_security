package com.jwt.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix ="app.security.jwt")
@EnableConfigurationProperties(KeyStoreProperties.class)
@Component
@Data
public class KeyStoreProperties {

    private String keystoreLocation;
}