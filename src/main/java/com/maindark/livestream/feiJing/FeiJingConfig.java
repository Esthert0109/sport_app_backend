package com.maindark.livestream.feiJing;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "feijing")
@Data
public class FeiJingConfig {
    private String secretKey;
    private String teamUrl;
    private String teamMatch;
    private String todayMatch;
    private String changeMatch;
    private String lineUp;
}
