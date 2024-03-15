package com.maindark.livestream.feiJing;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Data
@ConfigurationProperties(prefix = "feijing-basketball")
public class FeiJingBasketballConfig {
    private String teamMatch;
}
