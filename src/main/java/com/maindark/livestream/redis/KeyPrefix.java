package com.maindark.livestream.redis;

public interface KeyPrefix {
    public int expireSeconds();
    public String getPreFix();

}
