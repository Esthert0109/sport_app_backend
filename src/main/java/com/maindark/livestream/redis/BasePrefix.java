package com.maindark.livestream.redis;

public abstract class BasePrefix implements KeyPrefix{
    private int expireSeconds;
    private String prefix;

    public BasePrefix(String prefix){
        this(0,prefix);
    }

    public BasePrefix(int expireSeconds, String prefix) {
        this.expireSeconds = expireSeconds;
        this.prefix = prefix;
    }

    @Override
    public int expireSeconds() { // 0 never expire
        return expireSeconds;
    }

    @Override
    public String getPreFix() {
        String className = getClass().getSimpleName();
        return className + ":" + prefix;
    }
}
