package com.maindark.livestream.redis;

public class UserKey extends BasePrefix{


    private UserKey(String prefix) {
        super(prefix);
    }

    public static UserKey getById = new UserKey("id");
    public static UserKey getByName = new UserKey("username");

}
