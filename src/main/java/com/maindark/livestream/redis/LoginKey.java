package com.maindark.livestream.redis;

public class LoginKey extends BasePrefix{
    public LoginKey(int expireSeconds,String prefix) {
        super(expireSeconds,prefix);
    }

    public static final int expiredSeconds = 3600 * 24 * 2;
    public static LoginKey token = new LoginKey(expiredSeconds,"tk");
}
