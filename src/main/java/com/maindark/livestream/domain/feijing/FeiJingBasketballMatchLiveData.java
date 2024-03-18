package com.maindark.livestream.domain.feijing;

import lombok.Data;

@Data
public class FeiJingBasketballMatchLiveData {
    private Integer matchId;
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
}
