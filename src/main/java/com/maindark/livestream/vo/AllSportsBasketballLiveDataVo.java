package com.maindark.livestream.vo;

import lombok.Data;

@Data
public class AllSportsBasketballLiveDataVo {
    private Long matchId;
    private Integer hFQuarter;
    private Integer hSQuarter;
    private Integer hTQuarter;
    private Integer h4Quarter;
    private Integer aFQuarter;
    private Integer aSQuarter;
    private Integer aTQuarter;
    private Integer a4Quarter;
    private String hBlocks;
    private String hFieldGoals;
    private String hFreeThrows;
    private String hPersonalFouls;
    private String hRebounds;
    private String hSteals;
    private String hThreePointGoals;
    private String hTurnOvers;
    private String aBlocks;
    private String aFieldGoals;
    private String aFreeThrows;
    private String aPersonalFouls;
    private String aRebounds;
    private String aSteals;
    private String aThreePointGoals;
    private String aTurnOvers;
    private Integer homeScore;
    private Integer awayScore;
}
