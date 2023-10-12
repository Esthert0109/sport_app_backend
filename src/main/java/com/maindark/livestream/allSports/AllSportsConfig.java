package com.maindark.livestream.allSports;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "allsports")
@Data
public class AllSportsConfig {
    private String secretKey;
    private String leagues;
    private String host;
    private String teams;
    private String fixtures;

    public String getAllSportsApi(String url){
        return this.host + url + this.secretKey;
    }
}
