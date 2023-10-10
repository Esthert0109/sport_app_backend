package com.maindark.livestream.redis;

import com.maindark.livestream.vo.FootballMatchLineUpVo;

public class FootballMatchKey extends BasePrefix{
    private FootballMatchKey(String prefix) {
        super(prefix);
    }

    public FootballMatchKey(int expireSeconds, String prefix) {
        super(expireSeconds, prefix);
    }
    public static final int expiredSeconds = 3600 * 24;
    public static FootballMatchKey  matchLiveKey = new FootballMatchKey(expiredSeconds,"matchLive");
    public static FootballMatchKey matchKey = new FootballMatchKey("match");

    public static FootballMatchKey matchVoKey =  new FootballMatchKey(expiredSeconds,"matchVo");

    public static FootballMatchKey matchLiveVoKey = new FootballMatchKey(expiredSeconds,"matchLiveVo");

    public static FootballMatchKey matchLineUpKey = new FootballMatchKey(expiredSeconds,"matchLineUp");
}
