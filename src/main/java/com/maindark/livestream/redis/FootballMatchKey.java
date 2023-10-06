package com.maindark.livestream.redis;

import com.maindark.livestream.vo.FootballMatchLineUpVo;

public class FootballMatchKey extends BasePrefix{
    public FootballMatchKey(String prefix) {
        super(prefix);
    }

    public static FootballMatchKey  matchLiveKey = new FootballMatchKey("matchLive");
    public static FootballMatchKey matchKey = new FootballMatchKey("match");

    public static FootballMatchKey matchVoKey =  new FootballMatchKey("matchVo");

    public static FootballMatchKey matchLiveVoKey = new FootballMatchKey("matchLiveVo");

    public static FootballMatchKey matchLineUpKey = new FootballMatchKey("matchLineUp");
}
