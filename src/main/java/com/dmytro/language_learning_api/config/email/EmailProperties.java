package com.dmytro.language_learning_api.config.email;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Component
@ConfigurationProperties(prefix = "app.email")
@Setter
@Getter
public class EmailProperties {
    private String fromAddress;
    private String fromName;
    private String brevoApiKey;
}
