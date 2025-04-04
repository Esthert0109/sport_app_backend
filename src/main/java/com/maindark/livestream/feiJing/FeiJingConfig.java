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
    private String lineup;
    private String liveData;
    private String basketballTeam;
    private String basketballMatch;
    private String basketballTodayMatch;
    private String basketballChangeMatch;
    private String basketballInfo;
    private String footballInfo;
    private String basketballLineup;
    private String liveFootballAddress;
    private String liveBasketballAddress;
    private String liveSecretKey;
    private String liveAccessKey;
    private String liveHost;
    private String footballAnimation;
    private String basketballAnimation;
    private String animationSecretKey;
    private String animationAccessKey;

}
