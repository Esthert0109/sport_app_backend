package com.maindark.livestream.nami;

import lombok.Data;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "nami")
@Data
@Getter
public class NamiConfig {

    private String host;
    private String user;
    private String secretKey;


}
