package com.maindark.livestream.redis;

public class FootballListKey extends BasePrefix{
    public FootballListKey(int expireSeconds, String prefix) {
        super(expireSeconds, prefix);
    }
    public static final int expiredSeconds = 3600 * 24;
    public static FootballListKey listKey = new FootballListKey(expiredSeconds,"list");
}
