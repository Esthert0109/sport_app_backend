package com.maindark.livestream.redis;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "redis")
public class RedisConfig {
    private String host;
    private int port;
    private int timeout;
    private String user;
    private String password;
    private int poolMaxTotal;
    private int poolMaxdle;
    private int poolmaxWait;

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getPoolMaxTotal() {
        return poolMaxTotal;
    }

    public void setPoolMaxTotal(int poolMaxTotal) {
        this.poolMaxTotal = poolMaxTotal;
    }

    public int getPoolMaxdle() {
        return poolMaxdle;
    }

    public void setPoolMaxdle(int poolMaxdle) {
        this.poolMaxdle = poolMaxdle;
    }

    public int getPoolmaxWait() {
        return poolmaxWait;
    }

    public void setPoolmaxWait(int poolmaxWait) {
        this.poolmaxWait = poolmaxWait;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }
}
