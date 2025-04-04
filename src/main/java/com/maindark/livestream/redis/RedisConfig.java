package com.maindark.livestream.redis;

import lombok.Data;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "redis")
@Data
@Getter
public class RedisConfig {
    private String host;
    private int port;
    private int timeout;
    private String user;
    private String password;
    private int poolMaxTotal;
    private int poolMaxdle;
    private int poolmaxWait;


}
