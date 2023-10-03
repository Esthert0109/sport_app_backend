package com.maindark.livestream.redis;

public class FootballMatchKey extends BasePrefix{
    public FootballMatchKey(String prefix) {
        super(prefix);
    }

    public static FootballMatchKey  matchLiveKey = new FootballMatchKey("matchLive");
    public static FootballMatchKey matchKey = new FootballMatchKey("match");
}
